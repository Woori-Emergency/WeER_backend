package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.service.EmergencyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyApiController {

    @Autowired
    private EmergencyApiService emergencyApiService;

    /**
     * 서울특별시의 응급실 정보를 외부 API에서 가져와 저장하는 엔드포인트
     * @return API 호출 및 데이터 저장 결과 메시지
     */
    @GetMapping("/seoul")
    public ApiResponse<String> fetchAndSaveEmergencyInfo() {
        return ApiResponse.success(emergencyApiService.getEmergencyInfoForAllDistricts());
    }

}
