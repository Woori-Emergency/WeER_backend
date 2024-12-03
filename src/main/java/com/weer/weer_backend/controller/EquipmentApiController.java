package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.service.EquipmentApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipment")
public class EquipmentApiController {
    private final EquipmentApiService equipmentApiService;

    /**
     * 서울특별시의 장비 정보를 외부 API에서 가져와 저장하는 엔드포인트
     * @return API 호출 및 데이터 저장 결과 메시지
     */
    @GetMapping("/seoul")
    public ApiResponse<String> fetchAndSaveEquipmentInfo() {
        return ApiResponse.success(equipmentApiService.getEquipmentInfoForAllDistricts());
    }
}
