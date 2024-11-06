package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.dto.RouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {

  private final MapInterface mapInterface;
  @Value("${kakao.api-key}")
  private String apiKey;

  public MapInfoResponseDto getMapInfo(double originLat, double originLon
      , double destLat, double destLon) {
    RouteResponseDto mapInfo = mapInterface.getMapInfo(originLat + "," + originLon
        , destLat + "," + destLon, apiKey);
    return mapInfo.to();
  }
}

