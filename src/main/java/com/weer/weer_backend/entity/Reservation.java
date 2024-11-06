package com.weer.weer_backend.entity;

import com.weer.weer_backend.enums.ReservationStatus;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT_CONDITION_ID")
    private PatientCondition patientCondition;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public void changeStatus(ReservationStatus newStatus){
        this.reservationStatus = newStatus;
    }
}
