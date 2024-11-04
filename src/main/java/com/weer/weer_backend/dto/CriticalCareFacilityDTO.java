package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CriticalCareFacilityDTO {
    private Long criticalCareId;
    private Long hospitalId;
    private String facilityType;
    private Boolean available;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
