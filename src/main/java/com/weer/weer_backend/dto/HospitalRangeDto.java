package com.weer.weer_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRangeDto {
  private String hospitalName;
  private Double latitude;
  private Double longitude;
  private Integer roadDistance;
  private Integer duration;
  private int availableBeds;
  private int totalBeds;
}
