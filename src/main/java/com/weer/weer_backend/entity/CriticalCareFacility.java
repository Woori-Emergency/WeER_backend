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
@Table(name = "CRITICAL_CARE_FACILITY")
public class CriticalCareFacility extends BaseEntity{

    @Id
    @Column(name = "CRITICAL_CARE_ID")
    private Long criticalCareId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "FACILITY_TYPE")
    private String facilityType;

    private Boolean available;

}
