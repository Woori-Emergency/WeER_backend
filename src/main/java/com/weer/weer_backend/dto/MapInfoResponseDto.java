package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MapInfoResponseDto {
  private Integer distance;
  private Integer duration;
}