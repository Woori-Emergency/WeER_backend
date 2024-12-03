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
import java.util.Optional;
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

  private boolean applyEmergencyFilters(Hospital hospital, HospitalFilterDto filter) {
    return (!filter.isHvec() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHvec() != null && emergency.getHvec() > 0).orElse(false))
        && (!filter.isHv27() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHv27() != null && emergency.getHv27() > 0).orElse(false))
        && (!filter.isHv29() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHv29() != null && emergency.getHv29() > 0).orElse(false))
        && (!filter.isHv30() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHv30() != null && emergency.getHv30() > 0).orElse(false))
        && (!filter.isHv28() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHv28() != null && emergency.getHv28() > 0).orElse(false))
        && (!filter.isHv15() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHv15() != null && emergency.getHv15() > 0).orElse(false))
        && (!filter.isHv16() || Optional.ofNullable(hospital.getEmergencyId())
        .map(emergency -> emergency.getHv16() != null && emergency.getHv16() > 0).orElse(false));
  }

  private boolean applyICUFilters(Hospital hospital, HospitalFilterDto filter) {
    return (!filter.isHvcc() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHvcc() != null && icu.getHvcc() > 0).orElse(false))
        && (!filter.isHvncc() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHvncc() != null && icu.getHvncc() > 0).orElse(false))
        && (!filter.isHvccc() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHvccc() != null && icu.getHvccc() > 0).orElse(false))
        && (!filter.isHvicc() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHvicc() != null && icu.getHvicc() > 0).orElse(false))
        && (!filter.isHv2() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv2() != null && icu.getHv2() > 0).orElse(false))
        && (!filter.isHv3() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv3() != null && icu.getHv3() > 0).orElse(false))
        && (!filter.isHv6() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv6() != null && icu.getHv6() > 0).orElse(false))
        && (!filter.isHv8() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv8() != null && icu.getHv8() > 0).orElse(false))
        && (!filter.isHv9() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv9() != null && icu.getHv9() > 0).orElse(false))
        && (!filter.isHv32() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv32() != null && icu.getHv32() > 0).orElse(false))
        && (!filter.isHv34() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv34() != null && icu.getHv34() > 0).orElse(false))
        && (!filter.isHv35() || Optional.ofNullable(hospital.getIcuId())
        .map(icu -> icu.getHv35() != null && icu.getHv35() > 0).orElse(false));
  }

  private boolean applyEquipmentFilters(Hospital hospital, HospitalFilterDto filter) {
    return (!filter.isHvventiAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvventiAYN())).orElse(false))
        && (!filter.isHvventisoAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvventisoAYN())).orElse(false))
        && (!filter.isHvinCUAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvinCUAYN())).orElse(false))
        && (!filter.isHvcrrTAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvcrrTAYN())).orElse(false))
        && (!filter.isHvecmoAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvecmoAYN())).orElse(false))
        && (!filter.isHvhypoAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvhypoAYN())).orElse(false))
        && (!filter.isHvoxyAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvoxyAYN())).orElse(false))
        && (!filter.isHvctAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvctAYN())).orElse(false))
        && (!filter.isHvmriAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvmriAYN())).orElse(false))
        && (!filter.isHvangioAYN() || Optional.ofNullable(hospital.getEquipmentId())
        .map(equipment -> Boolean.TRUE.equals(equipment.getHvangioAYN())).orElse(false));
  }


  public HospitalDTO getHospitalDetail(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HOSPITAL));
    return HospitalDTO.from(hospital);
  }

  public List<HospitalDistanceDto> getDistanceAllHospital(Double latitude, Double longitude,
      int range) {
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
}
