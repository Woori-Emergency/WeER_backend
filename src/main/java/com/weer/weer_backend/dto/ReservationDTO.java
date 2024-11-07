package com.weer.weer_backend.dto;

import com.weer.weer_backend.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDTO {
    private Long reservationId;
    private Long hospitalId;
    private Long patientconditionId;
    private ReservationStatus reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

