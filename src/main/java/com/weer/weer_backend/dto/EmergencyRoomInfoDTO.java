package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmergencyRoomInfoDTO {
    private Long emergencyId;
    private Long hospitalId;
    private Integer erCapacity;
    private Integer availableErBeds;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
