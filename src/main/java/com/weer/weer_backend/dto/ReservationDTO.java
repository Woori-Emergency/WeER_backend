package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ReservationDTO {
    private Long reservationId;
    private Long hospitalId;
    private Long userId;
    private PatientCondition patientCondition;
    private ReservationStatus reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and Setters
}

