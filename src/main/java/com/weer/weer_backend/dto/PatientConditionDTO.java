package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientConditionDTO {

    @NotNull(message = "성별은 필수 입력값입니다")
    private Gender gender;
    @NotNull(message = "연령대는 필수 입력값입니다")
    private AgeGroup ageGroup;
    private Integer bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Integer respiration;
    private Medical medical;
    private ConsciousnessLevel consciousnessLevel;

    public PatientCondition toEntity(Long userId) {
        return PatientCondition.builder()
                .userId(userId)
                .transportStatus(TransportStatus.IN_PROGRESS)
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