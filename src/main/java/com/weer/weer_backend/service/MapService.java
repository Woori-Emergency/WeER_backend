package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.dto.RouteResponseDto;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapService {

  private final MapInterface mapInterface;
  @Value("${kakao.api-key}")
  private String apiKey;

  public MapInfoResponseDto getMapInfo(double originLat, double originLon
      , double destLat, double destLon) {
    String origin = originLon + "," + originLat;
    String dest = destLon + "," + destLat;
    RouteResponseDto mapInfo = mapInterface.getMapInfo(origin,dest, apiKey);
    if(mapInfo.getRoutes().getFirst().getSummary() == null) return null;
    return mapInfo.to();
  }
}

