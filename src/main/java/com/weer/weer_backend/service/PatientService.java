package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.PatientConditionDTO;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.repository.PatientConditionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientConditionRepository patientConditionRepository;

    public PatientService(PatientConditionRepository patientConditionRepository) {
        this.patientConditionRepository = patientConditionRepository;
    }

    @Transactional
    public ResponseEntity<Object> createPatientCondition(Long userId, PatientConditionDTO patientConditionDTO) {
        PatientCondition newPatientCondition = patientConditionDTO.toEntity(userId);
        PatientCondition savePatient = patientConditionRepository.save(newPatientCondition);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savePatient);
    }
}
