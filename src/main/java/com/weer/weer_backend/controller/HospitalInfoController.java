package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.*;
import com.weer.weer_backend.service.HospitalInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
public class HospitalInfoController {

  private final HospitalInfoService hospitalInfoService;

  @GetMapping("/announcement")
  public ApiResponse<List<ERAnnouncementDTO>> getAnnouncement(
      @RequestParam(name = "hospitalid") Long hospitalId) {
    List<ERAnnouncementDTO> response = hospitalInfoService.getAnnounce(hospitalId);
    return ApiResponse.success(response);
  }

  @PostMapping("/info")
  public ApiResponse<List<HospitalDistanceDto>> filterHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestBody HospitalFilterDto filter) {
    List<HospitalDistanceDto> response = hospitalInfoService.filteredHospitals(lat, lon, filter);
    return ApiResponse.success(response);
  }

  @PostMapping("/distance")
  public ApiResponse<List<HospitalDistanceDto>> distanceHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestParam int range) {
    List<HospitalDistanceDto> response = hospitalInfoService.getDistanceAllHospital(lat, lon, range);
    return ApiResponse.success(response);
  }

  @GetMapping("/location")
  public ApiResponse<List<HospitalRangeDto>> getHospitalLocation(@RequestParam Double lat
      , @RequestParam Double lon, @RequestParam Integer range) {
    List<HospitalRangeDto> response =
        hospitalInfoService.getRangeAllHospital(lat, lon, range);
    return ApiResponse.success(response);
  }

  @GetMapping("/detail")
  public ApiResponse<HospitalDTO> getHospitalDetail(
      @RequestParam(name = "hospitalid") Long hospitalId) {
    HospitalDTO response = hospitalInfoService.getHospitalDetail(hospitalId);
    return ApiResponse.success(response);
  }
}
