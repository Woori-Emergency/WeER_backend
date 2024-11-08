package com.weer.weer_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.weer.weer_backend.service.OpenApiService;

@Slf4j
@RestController
public class OpenApiController {

    @Autowired
    private OpenApiService openApiService;

    // 서울특별시의 모든 구에 대해 응급실 정보를 요청하고 저장하는 엔드포인트
    @GetMapping("/api/hospital/seoul")
    public String getEmergencyInfoForAllDistricts() {

        log.info("getEmergencyInfoForAllDistricts called");

        openApiService.getHospitalInfoForAllDistricts();
        return "서울특별시의 모든 구에 대한 데이터 요청 및 저장 완료";
    }
}
