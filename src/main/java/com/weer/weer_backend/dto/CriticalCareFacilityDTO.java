package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CriticalCareFacilityDTO {
    private Long criticalCareId;
    private Long hospitalId;
    private String facilityType;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and Setters
}
