package com.weer.weer_backend.entity;

import com.weer.weer_backend.enums.AgeGroup;
import com.weer.weer_backend.enums.Condition;
import com.weer.weer_backend.enums.ConsciousnessLevel;
import com.weer.weer_backend.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "PATIENT_CONDITION")
public class PatientCondition extends BaseEntity {

    @Id
    @Column(name = "PATIENT_CONDITION_ID")
    private Long patientconditionid;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    private Integer bloodPressure;  // 혈압
    private Integer heartRate;   // 맥박
    private Double temperature;  // 체온
    private Integer respiration; // 호흡수

    @Enumerated(EnumType.STRING)
    private Condition condition; // 질병 / 질병 외

    @Enumerated(EnumType.STRING)
    private ConsciousnessLevel consciousnessLevel; // 의식 레벨
}
