package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientConditionResponseDTO {
    private Long patientconditionid;
    private Long userId;
    private Gender gender;
    private AgeGroup ageGroup;
    private Integer bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Integer respiration;
    private Medical medical;
    private ConsciousnessLevel consciousnessLevel;
    private TransportStatus transportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Entity를 ResponseDTO로 변환하는 정적 메서드
    public static PatientConditionResponseDTO fromEntity(PatientCondition entity) {
        return PatientConditionResponseDTO.builder()
                .patientconditionid(entity.getPatientconditionid())
                .userId(entity.getUserId())
                .gender(entity.getGender())
                .ageGroup(entity.getAgeGroup())
                .bloodPressure(entity.getBloodPressure())
                .heartRate(entity.getHeartRate())
                .temperature(entity.getTemperature())
                .respiration(entity.getRespiration())
                .medical(entity.getMedical())
                .consciousnessLevel(entity.getConsciousnessLevel())
                .transportStatus(entity.getTransportStatus())
                .createdAt(entity.getCreated_at())
                .modifiedAt(entity.getModified_at())
                .build();
    }
}