package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ERAnnouncementDTO;
import com.weer.weer_backend.dto.HospitalDTO;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.service.HospitalInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<ERAnnouncementDTO>> getAnnouncement(
      @RequestParam(name = "hospitalid") Long hospitalId) {
    long startTime = System.currentTimeMillis();
    ResponseEntity<List<ERAnnouncementDTO>> response = ResponseEntity.ok(
        hospitalInfoService.getAnnounce(hospitalId));
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getAnnounce: {} ms", duration);
    return response;
  }

  @PostMapping("/info")
  public ResponseEntity<List<HospitalDTO>> filterHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestBody HospitalFilterDto filter) {
    long startTime = System.currentTimeMillis();
    ResponseEntity<List<HospitalDTO>> response = ResponseEntity.ok(
        hospitalInfoService.filteredHospitals(lat, lon, filter));
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getFilterHospital : {} ms", duration);
    return response;
  }

  @GetMapping("/location")
  public ResponseEntity<List<HospitalRangeDto>> getHospitalLocation(@RequestParam Double lat
      , @RequestParam Double lon, @RequestParam Integer range) {
    long startTime = System.currentTimeMillis();
    ResponseEntity<List<HospitalRangeDto>> response = ResponseEntity.ok(
        hospitalInfoService.getRangeAllHospital(lat, lon, range));
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getHospitalLocation : {} ms", duration);
    return response;
  }

  @GetMapping("/detail")
  public ResponseEntity<HospitalDTO> getHospitalDetail(
      @RequestParam(name = "hospitalid") Long hospitalId) {
    long startTime = System.currentTimeMillis();
    ResponseEntity<HospitalDTO> response = ResponseEntity.ok(
        hospitalInfoService.getHospitalDetail(hospitalId));
    long endTime = System.currentTimeMillis();  // 종료 시간
    long duration = endTime - startTime;  // 실행 시간 계산
    log.info("Execution time of getHospitalDetail : {} ms", duration);
    return response;
  }


}
