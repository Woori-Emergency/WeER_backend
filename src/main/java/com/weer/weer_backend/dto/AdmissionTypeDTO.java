package com.weer.weer_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
public class AdmissionTypeDTO {
    private Long admissionId;
    private Long hospitalId;
    private String admissionType;
    private Integer availableBeds;
    private Integer totalBeds;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
