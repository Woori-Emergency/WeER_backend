package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.BaseEntity;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.enums.AgeGroup;
import com.weer.weer_backend.enums.Condition;
import com.weer.weer_backend.enums.ConsciousnessLevel;
import com.weer.weer_backend.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
public class PatientConditionDTO extends BaseEntity {
    private Long patientconditionid;
    private Long userId;
    private Gender gender;
    private AgeGroup ageGroup;
    private Integer bloodPressure;  // 혈압
    private Integer heartRate;   // 맥박
    private Double temperature;  // 체온
    private Integer respiration; // 호흡수
    private Condition condition; // 질병 / 질병 외
    private ConsciousnessLevel consciousnessLevel; // 의식 레벨

    public PatientCondition toEntity(){
        return PatientCondition.builder()
                .patientconditionid(this.patientconditionid)
                .userId(this.userId)
                .gender(this.gender)
                .ageGroup(this.ageGroup)
                .bloodPressure(this.bloodPressure)
                .heartRate(this.heartRate)
                .temperature(this.temperature)
                .respiration(this.respiration)
                .condition(this.condition)
                .consciousnessLevel(this.consciousnessLevel)
                .build();
    }
}
