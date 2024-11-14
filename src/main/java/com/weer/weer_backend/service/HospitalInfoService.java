package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.ERAnnouncementDTO;
import com.weer.weer_backend.dto.HospitalDTO;
import com.weer.weer_backend.dto.HospitalDistanceDto;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.ERAnnouncementRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalInfoService {

  private final MapService mapService;
  private final HospitalRepository hospitalRepository;
  private final ERAnnouncementRepository erAnnouncementRepository;

  private static final double EARTH_RADIUS = 6371.0;

  public List<ERAnnouncementDTO> getAnnounce(long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HOSPITAL));
    return erAnnouncementRepository.findAllByHospitalId(hospital)
        .stream()
        .map(ERAnnouncementDTO::from)
        .collect(Collectors.toList());
  }

  public List<HospitalDistanceDto> filteredHospitals(Double latitude, Double longitude
      , HospitalFilterDto hospitalFilterDto) {
    List<Hospital> hospitals = filterHospital(hospitalFilterDto);

    return getDistanceAndTimeHospitals(hospitals, latitude, longitude);
  }

  private List<Hospital> filterHospital(HospitalFilterDto filter) {
    List<Hospital> hospitals = hospitalRepository.findByCityAndState(filter.getCity(),
        filter.getState());

    return hospitals.parallelStream()
        .filter(h -> hasEssentialIds(h)
            && applyEmergencyFilters(h, filter)
            && applyICUFilters(h, filter)
            && applyEquipmentFilters(h, filter)).toList();
  }

  private boolean hasEssentialIds(Hospital h) {
    return h.getIcuId() != null || h.getEmergencyId() != null || h.getEquipmentId() != null;
  }

  private boolean applyEmergencyFilters(Hospital h, HospitalFilterDto filter) {
    return (!filter.isHvec() || h.getEmergencyId().getHvec() > 0)
        && (!filter.isHv27() || h.getEmergencyId().getHv27() > 0)
        && (!filter.isHv29() || h.getEmergencyId().getHv29() > 0)
        && (!filter.isHv30() || h.getEmergencyId().getHv30() > 0)
        && (!filter.isHv28() || h.getEmergencyId().getHv28() > 0)
        && (!filter.isHv15() || h.getEmergencyId().getHv15() > 0)
        && (!filter.isHv16() || h.getEmergencyId().getHv16() > 0);
  }

  private boolean applyICUFilters(Hospital h, HospitalFilterDto filter) {
    return (!filter.isHvcc() || h.getIcuId().getHvcc() > 0)
        && (!filter.isHvncc() || h.getIcuId().getHvncc() > 0)
        && (!filter.isHvccc() || h.getIcuId().getHvccc() > 0)
        && (!filter.isHvicc() || h.getIcuId().getHvicc() > 0)
        && (!filter.isHv2() || h.getIcuId().getHv2() > 0)
        && (!filter.isHv3() || h.getIcuId().getHv3() > 0)
        && (!filter.isHv6() || h.getIcuId().getHv6() > 0)
        && (!filter.isHv8() || h.getIcuId().getHv8() > 0)
        && (!filter.isHv9() || h.getIcuId().getHv9() > 0)
        && (!filter.isHv32() || h.getIcuId().getHv32() > 0)
        && (!filter.isHv34() || h.getIcuId().getHv34() > 0)
        && (!filter.isHv35() || h.getIcuId().getHv35() > 0);
  }

  private boolean applyEquipmentFilters(Hospital h, HospitalFilterDto filter) {
    return (!filter.isHvventiAYN() || h.getEquipmentId().getHvventiAYN())
        && (!filter.isHvventisoAYN() || h.getEquipmentId().getHvventisoAYN())
        && (!filter.isHvinCUAYN() || h.getEquipmentId().getHvinCUAYN())
        && (!filter.isHvcrrTAYN() || h.getEquipmentId().getHvcrrTAYN())
        && (!filter.isHvecmoAYN() || h.getEquipmentId().getHvecmoAYN())
        && (!filter.isHvhypoAYN() || h.getEquipmentId().getHvhypoAYN())
        && (!filter.isHvoxyAYN() || h.getEquipmentId().getHvoxyAYN())
        && (!filter.isHvctAYN() || h.getEquipmentId().getHvctAYN())
        && (!filter.isHvmriAYN() || h.getEquipmentId().getHvmriAYN())
        && (!filter.isHvangioAYN() || h.getEquipmentId().getHvangioAYN());
  }

  public HospitalDTO getHospitalDetail(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HOSPITAL));
    return HospitalDTO.from(hospital);
  }

  public List<HospitalDistanceDto> getDistanceAllHospital(Double latitude, Double longitude, int range) {
    final int METERS_IN_KILOMETER = 1000;
    int rangeMeters = range * METERS_IN_KILOMETER;

    // 범위 내 병원 조회
    List<Hospital> rangeHospitals = hospitalRepository.findRangeHospital(latitude, longitude,
        rangeMeters);
    return getDistanceAndTimeHospitals(rangeHospitals, latitude, longitude);
  }

  private List<HospitalDistanceDto> getDistanceAndTimeHospitals(List<Hospital> hospitals,
      Double latitude, Double longitude) {
    List<CompletableFuture<HospitalDistanceDto>> futureList = new ArrayList<>();

    // 각 병원에 대해 비동기적으로 지도 정보 요청
    for (Hospital hospital : hospitals) {
      if (hospital.getEmergencyId() == null || hospital.getEquipmentId() == null
          || hospital.getIcuId() == null) {
        continue;
      }

      CompletableFuture<HospitalDistanceDto> future = mapService.getMapInfo(latitude, longitude,
              hospital.getLatitude(), hospital.getLongitude())
          .thenApply(mapInfo -> {
            if (mapInfo == null) {
              log.warn("Map info is null for hospital: {}", hospital.getHospitalId());
              return null;
            }
            return HospitalDistanceDto.from(hospital, mapInfo.getDistance(), mapInfo.getDuration());
          })
          .exceptionally(ex -> {
            log.error("Exception occurred while processing hospital: {}", hospital.getHospitalId(),
                ex);
            return null;
          });
      futureList.add(future);
    }

    // 모든 비동기 작업이 완료될 때까지 대기
    CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

    // 완료된 결과 수집
    List<HospitalDistanceDto> rangeHospitalList = new ArrayList<>();
    for (CompletableFuture<HospitalDistanceDto> future : futureList) {
      try {
        HospitalDistanceDto dto = future.get();
        if (dto != null) {
          rangeHospitalList.add(dto);
        }
      } catch (Exception e) {
        log.warn("Failed to get HospitalDistanceDto: {}", e.getMessage());
      }
    }

    log.info("Completed fetching map info for all hospitals in range.");
    return rangeHospitalList;
  }

  @Transactional(readOnly = true)
  public List<HospitalRangeDto> getRangeAllHospital(Double latitude, Double longitude, int range) {
    final int METERS_IN_KILOMETER = 1000;
    int rangeMeters = range * METERS_IN_KILOMETER;

    // 범위 내 병원 조회
    List<Hospital> rangeHospitals = hospitalRepository.findRangeHospital(latitude, longitude,
        rangeMeters);

    List<CompletableFuture<HospitalRangeDto>> futureList = new ArrayList<>();

    // 각 병원에 대해 비동기적으로 지도 정보 요청
    for (Hospital hospital : rangeHospitals) {
      if (hospital.getEmergencyId() == null || hospital.getEquipmentId() == null
          || hospital.getIcuId() == null) {
        continue;
      }

      CompletableFuture<HospitalRangeDto> future = mapService.getMapInfo(latitude, longitude,
              hospital.getLatitude(), hospital.getLongitude())
          .thenApply(mapInfo -> {
            if (mapInfo == null) {
              log.warn("Map info is null for hospital: {}", hospital.getHospitalId());
              return null;
            }
            return HospitalRangeDto.builder()
                .hospitalName(hospital.getName())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .roadDistance(mapInfo.getDistance())
                .duration(mapInfo.getDuration())
                .availableBeds(hospital.getEmergencyId().getHvec())
                .totalBeds(hospital.getEmergencyId().getHvs01())
                .build();
          })
          .exceptionally(ex -> {
            log.error("Exception occurred while processing hospital: {}", hospital.getHospitalId(),
                ex);
            return null;
          });
      futureList.add(future);
    }

    // 모든 비동기 작업이 완료될 때까지 대기
    CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

    // 완료된 결과 수집
    List<HospitalRangeDto> rangeHospitalList = new ArrayList<>();
    for (CompletableFuture<HospitalRangeDto> future : futureList) {
      try {
        HospitalRangeDto dto = future.get();
        if (dto != null) {
          rangeHospitalList.add(dto);
        }
      } catch (Exception e) {
        log.warn("Failed to get HospitalRangeDto: {}", e.getMessage());
      }
    }

    log.info("Completed fetching map info for all hospitals in range.");
    return rangeHospitalList;
  }

  private Double getDistance(Double latitude, Double longitude, Double targetLat,
      Double targetLon) {
    double lat1Rad = Math.toRadians(latitude);
    double lon1Rad = Math.toRadians(longitude);
    double lat2Rad = Math.toRadians(targetLat);
    double lon2Rad = Math.toRadians(targetLon);

    double deltaLat = lat2Rad - lat1Rad;
    double deltaLon = lon2Rad - lon1Rad;

    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
        Math.cos(lat1Rad) * Math.cos(lat2Rad) *
            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // Km
    return EARTH_RADIUS * c;
  }
}
