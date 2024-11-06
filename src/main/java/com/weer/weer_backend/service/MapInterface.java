package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.RouteResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoMapApi", url= "https://apis-navi.kakaomobility.com/v1/directions")
public interface MapInterface {
  @GetMapping()
  RouteResponseDto getMapInfo(@RequestParam("origin") String origin
      , @RequestParam("destination") String destination
      ,@RequestHeader("Authorization") String apiKey);
}
