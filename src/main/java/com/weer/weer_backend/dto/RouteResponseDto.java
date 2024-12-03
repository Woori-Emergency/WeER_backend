package com.weer.weer_backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponseDto {
  private String transId;
  private List<RouteDto> routes;

  public MapInfoResponseDto to(){
    RouteDto routeDto = this.routes.stream().findFirst()
        .orElseThrow(IllegalArgumentException::new);
    return MapInfoResponseDto.builder()
        .distance(routeDto.getSummary().getDistance())
        .duration(routeDto.getSummary().getDuration())
        .build();
  }
}
