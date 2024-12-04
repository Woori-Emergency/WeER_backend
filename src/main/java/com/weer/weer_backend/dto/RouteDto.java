package com.weer.weer_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
  private int resultCode;
  private String resultMsg;
  private SummaryDto summary;
}
