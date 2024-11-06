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
@Table(name = "EMERGENCY_ROOM_INFO")
public class EmergencyRoomInfo extends BaseEntity{

    @Id
    @Column(name = "EMERGENCY_ID")
    private Long emergencyId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "ER_CAPACITY")
    private Integer erCapacity;

    @Column(name = "AVAILABLE_ER_BEDS")
    private Integer availableErBeds;

}
