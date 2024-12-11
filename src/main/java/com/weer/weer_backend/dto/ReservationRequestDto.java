package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReservationRequestDto {
  private Long patientconditionId;
  private Long hospitalId;

  public Reservation from(){
    return Reservation.builder()
        .hospitalId(this.hospitalId)
        .patientconditionid(this.patientconditionId)
        .reservationStatus(ReservationStatus.PENDING)
        .build();
  }
}
