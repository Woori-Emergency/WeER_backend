package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class EmergencyRoomInfoDTO {
    private Long emergencyId;
    private Long hospitalId;
    private Integer erCapacity;
    private Integer availableErBeds;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and Setters
}
