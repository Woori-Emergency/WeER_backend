package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDTO {
    private Long reservationId;
    private Long hospitalId;
    private Long userId;
    private String patientName;
    private String patientStatus;
    private String status;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
