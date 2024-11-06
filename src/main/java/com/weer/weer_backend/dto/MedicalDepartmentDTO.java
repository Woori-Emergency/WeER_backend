package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MedicalDepartmentDTO {
    private Long departmentId;
    private Long hospitalId;
    private String departmentName;
    private Boolean specialistAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and Setters
}
