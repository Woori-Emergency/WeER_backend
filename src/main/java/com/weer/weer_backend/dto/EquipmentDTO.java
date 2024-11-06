package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EquipmentDTO {
    private Long equipmentId;
    private Long hospitalId;
    private String equipmentName;
    private Boolean availability;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
