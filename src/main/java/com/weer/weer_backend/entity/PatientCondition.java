package com.weer.weer_backend.entity;

import com.weer.weer_backend.enums.*;
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

    @Column(name = "USER_ID")
    private Long userId;

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

    @Enumerated(EnumType.STRING)
    private TransportStatus transportStatus; // 이송 현황
}
