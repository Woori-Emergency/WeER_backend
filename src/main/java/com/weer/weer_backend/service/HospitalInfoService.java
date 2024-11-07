package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.ERAnnouncementDTO;
import com.weer.weer_backend.dto.HospitalDTO;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.ERAnnouncementRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalInfoService {

  private final MapService mapService;
  private final HospitalRepository hospitalRepository;
  private final ERAnnouncementRepository erAnnouncementRepository;

  private static final double EARTH_RADIUS = 6371.0;

  public List<ERAnnouncementDTO> getAnnounce(long hospitalId){
    return erAnnouncementRepository.findAllByHospitalId(hospitalId)
        .stream()
        .map(ERAnnouncementDTO::from)
        .collect(Collectors.toList());
  }

  public List<HospitalDTO> filteredHospitals(Double latitude, Double longitude
      , HospitalFilterDto hospitalFilterDto) {
    List<HospitalDTO> hospitalDTOS = filterHospital(hospitalFilterDto);

    hospitalDTOS.sort(Comparator.comparingDouble(h ->
        getDistance(latitude, longitude, h.getLatitude(), h.getLongitude())));

    return hospitalDTOS;
  }

  public List<HospitalDTO> filterHospital(HospitalFilterDto filter) {
    List<Hospital> hospitals = hospitalRepository.findByCityAndAndState(filter.getCity()
        , filter.getState());

    return hospitals.stream()

        // Emergency filters
        .filter(h -> !filter.isHvec() || h.getEmergencyId().getHvec() > 0)
        .filter(h -> !filter.isHv27() || h.getEmergencyId().getHv27() > 0)
        .filter(h -> !filter.isHv29() || h.getEmergencyId().getHv29() > 0)
        .filter(h -> !filter.isHv30() || h.getEmergencyId().getHv30() > 0)
        .filter(h -> !filter.isHv28() || h.getEmergencyId().getHv28() > 0)
        .filter(h -> !filter.isHv15() || h.getEmergencyId().getHv15() > 0)
        .filter(h -> !filter.isHv16() || h.getEmergencyId().getHv16() > 0)

        // ICU filters
        .filter(h -> !filter.isHvcc() || h.getIcuId().getHvcc() > 0)
        .filter(h -> !filter.isHvncc() || h.getIcuId().getHvncc() > 0)
        .filter(h -> !filter.isHvccc() || h.getIcuId().getHvccc() > 0)
        .filter(h -> !filter.isHvicc() || h.getIcuId().getHvicc() > 0)
        .filter(h -> !filter.isHv2() || h.getIcuId().getHv2() > 0)
        .filter(h -> !filter.isHv3() || h.getIcuId().getHv3() > 0)
        .filter(h -> !filter.isHv6() || h.getIcuId().getHv6() > 0)
        .filter(h -> !filter.isHv8() || h.getIcuId().getHv8() > 0)
        .filter(h -> !filter.isHv9() || h.getIcuId().getHv9() > 0)
        .filter(h -> !filter.isHv32() || h.getIcuId().getHv32() > 0)
        .filter(h -> !filter.isHv34() || h.getIcuId().getHv34() > 0)
        .filter(h -> !filter.isHv35() || h.getIcuId().getHv35() > 0)

        // Equipment filters
        .filter(h -> !filter.isHvventiAYN() || h.getEquipmentId().getHvventiAYN())
        .filter(h -> !filter.isHvventisoAYN() || h.getEquipmentId().getHvventisoAYN())
        .filter(h -> !filter.isHvinCUAYN() || h.getEquipmentId().getHvinCUAYN())
        .filter(h -> !filter.isHvcrrTAYN() || h.getEquipmentId().getHvcrrTAYN())
        .filter(h -> !filter.isHvecmoAYN() || h.getEquipmentId().getHvecmoAYN())
        .filter(h -> !filter.isHvhypoAYN() || h.getEquipmentId().getHvhypoAYN())
        .filter(h -> !filter.isHvoxyAYN() || h.getEquipmentId().getHvoxyAYN())
        .filter(h -> !filter.isHvctAYN() || h.getEquipmentId().getHvctAYN())
        .filter(h -> !filter.isHvmriAYN() || h.getEquipmentId().getHvmriAYN())
        .filter(h -> !filter.isHvangioAYN() || h.getEquipmentId().getHvangioAYN())

        // Map to HospitalDTO
        .map(HospitalDTO::from)
        .collect(Collectors.toList());
  }


  public HospitalDTO getHospitalDetail(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(IllegalArgumentException::new);
    return HospitalDTO.from(hospital);
  }

  public List<HospitalRangeDto> getRangeAllHospital(Double latitude, Double longitude, int range) {
    List<Hospital> rangeHospitals = getRangeHospitals(latitude, longitude, range);
    List<HospitalRangeDto> rangeHospitalList = new ArrayList<>();

    for (Hospital hospital : rangeHospitals) {
      MapInfoResponseDto mapInfo = getMapInfo(latitude, longitude, hospital);

      HospitalRangeDto hospitalRangeDto = HospitalRangeDto.builder()
          .hospitalName(hospital.getName())
          .latitude(hospital.getLatitude())
          .longitude(hospital.getLongitude())
          .roadDistance(mapInfo.getDistance())
          .duration(mapInfo.getDuration())
          .availableBeds(hospital.getEmergencyId().getHvec())
          .totalBeds(hospital.getEmergencyId().getHvs01())
          .build();
      rangeHospitalList.add(hospitalRangeDto);
    }

    return rangeHospitalList;
  }

  private List<Hospital> getRangeHospitals(Double latitude, Double longitude, int range) {
    List<Hospital> hospitalList = hospitalRepository.findAll();
    List<Hospital> rangeHospitalList = new ArrayList<>();
    for (Hospital hospital : hospitalList) {
      double distance = getDistance(latitude, longitude, hospital.getLatitude(), hospital.getLongitude());
      if (distance <= range) {
        rangeHospitalList.add(hospital);
      }
    }
    return rangeHospitalList;
  }

  private MapInfoResponseDto getMapInfo(Double latitude, Double longitude, Hospital hospital) {
    return mapService.getMapInfo(latitude, longitude, hospital.getLatitude()
        , hospital.getLongitude());
  }

  private Double getDistance(Double latitude, Double longitude, Double targetLat, Double targetLon) {
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
