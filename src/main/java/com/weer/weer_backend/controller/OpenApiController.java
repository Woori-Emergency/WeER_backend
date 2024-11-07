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

    // 개별 시/구에 대해 응급실 정보를 요청하고 저장하는 엔드포인트
//    @GetMapping("/api/emergency")
//    public String getEmergencyInfo(
//            @RequestParam String stage1,
//            @RequestParam String stage2) {
//
//        log.info("getEmergencyInfo called with stage1={}, stage2={}", stage1, stage2);
//
//        return openApiService.getEmergencyInfoAndSave(stage1, stage2, 1, 10);
//    }

    // 특정 시의 모든 구에 대해 응급실 정보를 요청하고 저장하는 엔드포인트
    @GetMapping("/api/emergency/seoul")
    public String getEmergencyInfoForAllDistricts() {
        int pageNo = 1;
        int numOfRows = 10;
        String stage1 = "서울특별시";
        log.info("getEmergencyInfoForAllDistricts called with stage1={}, pageNo={}, numOfRows={}",
                stage1, pageNo, numOfRows);

        openApiService.getEmergencyInfoForAllDistricts(stage1, pageNo, numOfRows);
        return "서울특별시의 모든 구에 대한 데이터 요청 및 저장 완료";
    }
}
