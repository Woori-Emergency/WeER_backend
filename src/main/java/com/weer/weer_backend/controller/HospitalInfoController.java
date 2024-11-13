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
    long startTime = System.currentTimeMillis();
    List<ERAnnouncementDTO> response = hospitalInfoService.getAnnounce(hospitalId);
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getAnnounce: {} ms", duration);
    return ApiResponse.success(response);
  }

  @PostMapping("/info")
  public ApiResponse<List<HospitalDTO>> filterHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestBody HospitalFilterDto filter) {
    long startTime = System.currentTimeMillis();
    List<HospitalDTO> response = hospitalInfoService.filteredHospitals(lat, lon, filter);
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getFilterHospital : {} ms", duration);
    return ApiResponse.success(response);
  }

  @GetMapping("/location")
  public ApiResponse<List<HospitalRangeDto>> getHospitalLocation(@RequestParam Double lat
      , @RequestParam Double lon, @RequestParam Integer range) {
    long startTime = System.currentTimeMillis();
    List<HospitalRangeDto> response =
        hospitalInfoService.getRangeAllHospital(lat, lon, range);
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getHospitalLocation : {} ms", duration);
    return ApiResponse.success(response);
  }

  @GetMapping("/detail")
  public ApiResponse<HospitalDTO> getHospitalDetail(
      @RequestParam(name = "hospitalid") Long hospitalId) {
    long startTime = System.currentTimeMillis();
    HospitalDTO response = hospitalInfoService.getHospitalDetail(hospitalId);
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getHospitalDetail : {} ms", duration);
    return ApiResponse.success(response);
  }
}
