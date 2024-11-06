package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HospitalDTO {
    private Long hospitalId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
