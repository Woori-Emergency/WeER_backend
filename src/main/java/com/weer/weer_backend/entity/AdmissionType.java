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
@Table(name = "ADMISSION_TYPE")
public class AdmissionType extends BaseEntity{

    @Id
    @Column(name = "ADMISSION_ID")
    private Long admissionId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "ADMISSION_TYPE")
    private String admissionType;

    @Column(name = "AVAILABLE_BEDS")
    private Integer availableBeds;

    @Column(name = "TOTAL_BEDS")
    private Integer totalBeds;

}
