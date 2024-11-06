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
  private int result_code;
  private String result_msg;
  private SummaryDto summary;
}
