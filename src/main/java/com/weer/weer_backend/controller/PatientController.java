package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.PatientConditionDTO;
import com.weer.weer_backend.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/hospital/patient")
    public ResponseEntity<Object> createPatientCondition(@RequestParam Long userId, @RequestBody PatientConditionDTO patientConditionDTO) {
        return patientService.createPatientCondition(userId, patientConditionDTO);
    }
}
