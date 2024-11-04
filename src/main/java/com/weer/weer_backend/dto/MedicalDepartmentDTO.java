package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MedicalDepartmentDTO {
    private Long departmentId;
    private Long hospitalId;
    private String departmentName;
    private Boolean specialistAvailable;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
