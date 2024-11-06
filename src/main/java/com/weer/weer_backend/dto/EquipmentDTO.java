package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class EquipmentDTO {
    private Long equipmentId;
    private Long hospitalId;
    private String equipmentName;
    private Boolean availability;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and Setters
}
