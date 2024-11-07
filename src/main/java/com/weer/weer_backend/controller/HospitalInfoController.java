package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ERAnnouncementDTO;
import com.weer.weer_backend.dto.HospitalDTO;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.service.HospitalInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
public class HospitalInfoController {

  private final HospitalInfoService hospitalInfoService;

  @GetMapping("/announcement")
  public ResponseEntity<List<ERAnnouncementDTO>> getAnnouncement(@RequestParam("hospitalId") Long hospitalId) {
    return ResponseEntity.ok(hospitalInfoService.getAnnounce(hospitalId));
  }

  @PostMapping("/info")
  public ResponseEntity<List<HospitalDTO>> filterHospital(@RequestParam Double lat
      , @RequestParam Double lon, @RequestBody HospitalFilterDto filter){
    return ResponseEntity.ok(hospitalInfoService.filteredHospitals(lat,lon,filter));
  }

  @GetMapping("/location")
  public ResponseEntity<List<HospitalRangeDto>> getHospitalLocation(@RequestParam Double lat
      , @RequestParam Double lon, @RequestParam Integer range){
    return ResponseEntity.ok(hospitalInfoService.getRangeAllHospital(lat, lon, range));
  }

  @GetMapping("/detail")
  public ResponseEntity<HospitalDTO> getHospitalDetail(@RequestParam Long id){
    return ResponseEntity.ok(hospitalInfoService.getHospitalDetail(id));
  }


}
