package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "MEDICAL_DEPARTMENT")
public class
MedicalDepartment extends BaseEntity{

    @Id
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "SPECIALIST_AVAILABLE")
    private Boolean specialistAvailable;

}
