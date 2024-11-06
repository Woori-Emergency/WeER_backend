package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientConditionDTO {
    private Gender gender;
    private AgeGroup ageGroup;
    private Integer bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Integer respiration;
    private Medical medical;
    private ConsciousnessLevel consciousnessLevel;

    public PatientCondition toEntity(Long userId) {
        return PatientCondition.builder()
                .userId(userId)  // 로그인한 사용자 ID
                .transportStatus(TransportStatus.IN_PROGRESS)  // 초기 이송 상태
                .gender(this.gender)
                .ageGroup(this.ageGroup)
                .bloodPressure(this.bloodPressure)
                .heartRate(this.heartRate)
                .temperature(this.temperature)
                .respiration(this.respiration)
                .medical(this.medical)
                .consciousnessLevel(this.consciousnessLevel)

                .build();
    }
}