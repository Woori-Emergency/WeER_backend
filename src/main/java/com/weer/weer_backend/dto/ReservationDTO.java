package com.weer.weer_backend.dto;

import com.weer.weer_backend.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.ToString;

@Data
@Builder
@ToString
public class ReservationDTO {
    private Long reservationId;
    private Long hospitalId;
    private Long patientconditionId;
    private ReservationStatus reservationStatus;
}

