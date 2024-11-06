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
    private OpenApiService emergencyService;

    @GetMapping("/api/emergency")
    public String getEmergencyInfo(
            @RequestParam String stage1,
            @RequestParam String stage2) {

        log.info("getEmergencyInfo called with stage1={}, stage2={}", stage1, stage2);

        return emergencyService.getEmergencyInfo(stage1, stage2);
    }
}
