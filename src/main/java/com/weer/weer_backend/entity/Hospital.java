package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "HOSPITAL")
public class Hospital extends BaseEntity{

    @Id
    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    private String name;
    private String address;
    private String city;
    private String state;
    private String postalCode;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    private Double latitude;

    private Double longitude;


}
