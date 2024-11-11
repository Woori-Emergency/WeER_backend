package com.weer.weer_backend.entity;

import com.weer.weer_backend.enums.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "RESERVATION")
public class Reservation extends BaseEntity{
    @Id
    @Column(name = "RESERVATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "PATIENT_CONDITION_ID")
    private Long patientconditionid;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public Reservation changeStatus(ReservationStatus newStatus){
        this.reservationStatus = newStatus;
        return this;
    }
}
