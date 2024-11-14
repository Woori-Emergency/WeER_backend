package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.dto.ERAnnouncementDTO;
import com.weer.weer_backend.dto.HospitalDTO;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.service.HospitalInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ApiResponse<List<HospitalDTO>> filterHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestBody HospitalFilterDto filter) {
    List<HospitalDTO> response = hospitalInfoService.filteredHospitals(lat, lon, filter);
    return ApiResponse.success(response);
  }

  @PostMapping("/distance")
  public ApiResponse<List<HospitalDTO>> distanceHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestParam int range) {
    List<HospitalDTO> response = hospitalInfoService.getDistanceAllHospital(lat, lon, range);
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
