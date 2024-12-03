package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.weer.weer_backend.service.HospitalApiService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OpenApiController {
    private final HospitalApiService openApiService;

    // 서울특별시의 모든 구에 대해 응급실 정보를 요청하고 저장하는 엔드포인트
    @GetMapping("/api/hospital/seoul")
    public ApiResponse<String> getEmergencyInfoForAllDistricts() {

        log.info("getEmergencyInfoForAllDistricts called");

        openApiService.getHospitalInfoForAllDistricts();
        return ApiResponse.success("서울특별시의 모든 구에 대한 데이터 요청 및 저장 완료");
    }
}
