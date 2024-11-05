package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalRangeDto {
  private String hospitalName;
  private Double latitude;
  private Double longitude;
  private Double distance;
  private int availableBeds;
  private int totalBeds;
}
