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
@Table(name = "EQUIPMENT")
public class Equipment extends BaseEntity{

    @Id
    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "EQUIPMENT_NAME")
    private String equipmentName;

    private Boolean availability;

}
