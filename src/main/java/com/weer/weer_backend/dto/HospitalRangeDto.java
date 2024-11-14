package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.Hospital;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalRangeDto {
  private String hospitalName;
  private Double latitude;
  private Double longitude;
  private Integer roadDistance;
  private Integer duration;
  private int availableBeds;
  private int totalBeds;
}
