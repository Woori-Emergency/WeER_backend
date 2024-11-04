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
@Table(name = "INFECTION_CONTROL")
public class InfectionControl extends BaseEntity{

    @Id
    @Column(name = "INFECTION_CONTROL_ID")
    private Long infectionControlId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "ISOLATION_BEDS")
    private Integer isolationBeds;

    private Boolean availability;

}
