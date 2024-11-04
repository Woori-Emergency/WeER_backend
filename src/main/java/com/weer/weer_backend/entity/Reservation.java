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
@Table(name = "RESERVATION")
public class Reservation extends BaseEntity{

    @Id
    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "PATIENT_STATUS")
    private String patientStatus;

    private String status;

}
