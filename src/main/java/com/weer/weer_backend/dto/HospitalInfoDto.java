package com.weer.weer_backend.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalInfoDto {
  private String hospitalName;
  private String hospitalAddress;
  private String postalCode;
  private String phoneNumber;
  private List<String> departmentName;
  private List<Boolean> specialistAvailability;
  private Double latitude;
  private Double longitude;
  private Double distance;
  private int availableBeds;
  private int specialistAvailable;
  private int modifiedDate;
}
