package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class InfectionControlDTO {
    private Long infectionControlId;
    private Long hospitalId;
    private Integer isolationBeds;
    private Boolean availability;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and Setters
}
