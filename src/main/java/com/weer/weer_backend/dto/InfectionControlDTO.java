package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InfectionControlDTO {
    private Long infectionControlId;
    private Long hospitalId;
    private Integer isolationBeds;
    private Boolean availability;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
