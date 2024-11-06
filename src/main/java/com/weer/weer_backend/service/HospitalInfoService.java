package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.entity.AdmissionType;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.AdmissionTypeRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalInfoService {

  private final MapService mapService;
  private final HospitalRepository hospitalRepository;
  private final AdmissionTypeRepository admissionTypeRepository;

  private static final double EARTH_RADIUS = 6371.0;

  public List<HospitalRangeDto> getRangeAllHospital(Double latitude, Double longitude, int range) {
    List<Hospital> hospitalList = hospitalRepository.findAll();
    List<HospitalRangeDto> rangeHospitalList = new ArrayList<>();
    for(Hospital hospital : hospitalList) {
      double distance = getDistance(latitude,longitude,hospital);
      if(distance <= range) {
        AdmissionType admissionType = admissionTypeRepository
            .findAdmissionTypeByHospitalId(hospital.getHospitalId())
            .orElseThrow(IllegalArgumentException::new);

        MapInfoResponseDto mapInfo = getMapInfo(latitude, longitude, hospital);

        HospitalRangeDto hospitalRangeDto = HospitalRangeDto.builder()
            .hospitalName(hospital.getName())
            .latitude(hospital.getLatitude())
            .longitude(hospital.getLongitude())
            .distance(distance)
            .roadDistance(mapInfo.getDistance())
            .duration(mapInfo.getDuration())
            .availableBeds(admissionType.getAvailableBeds())
            .totalBeds(admissionType.getTotalBeds())
            .build();
        rangeHospitalList.add(hospitalRangeDto);
      }
    }
    return rangeHospitalList;
  }
  private MapInfoResponseDto getMapInfo(Double latitude, Double longitude, Hospital hospital) {
    return mapService.getMapInfo(latitude,longitude, hospital.getLatitude()
    , hospital.getLongitude());
  }

  private Double getDistance(Double latitude, Double longitude, Hospital hospital) {
    double lat1Rad = Math.toRadians(latitude);
    double lon1Rad = Math.toRadians(longitude);
    double lat2Rad = Math.toRadians(hospital.getLatitude());
    double lon2Rad = Math.toRadians(hospital.getLongitude());

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
