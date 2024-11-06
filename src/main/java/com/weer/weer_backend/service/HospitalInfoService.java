package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.HospitalRangeDto;
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

  private final HospitalRepository hospitalRepository;
  private final AdmissionTypeRepository admissionTypeRepository;

  public List<HospitalRangeDto> getRangeAllHospital(Double latitude, Double longitude, int range) {
    List<Hospital> hospitalList = hospitalRepository.findAll();
    List<HospitalRangeDto> rangeHospitalList = new ArrayList<>();
    for(Hospital hospital : hospitalList) {
      double distance = getDistance(latitude,longitude,hospital);
      if(distance <= range) {
        AdmissionType admissionType = admissionTypeRepository
            .findAdmissionTypeByHospitalId(hospital.getHospitalId())
            .orElseThrow(IllegalArgumentException::new);

        HospitalRangeDto hospitalRangeDto = HospitalRangeDto.builder()
            .hospitalName(hospital.getName())
            .latitude(hospital.getLatitude())
            .longitude(hospital.getLongitude())
            .distance(distance)
            .availableBeds(admissionType.getAvailableBeds())
            .totalBeds(admissionType.getTotalBeds())
            .build();
        rangeHospitalList.add(hospitalRangeDto);
      }
    }
    return rangeHospitalList;
  }



  private Double getDistance(Double latitude, Double longitude, Hospital hospital) {
    return Math.sqrt(Math.pow(latitude - hospital.getLatitude(),2) +
        Math.pow(longitude - hospital.getLongitude(),2));
  }
}
