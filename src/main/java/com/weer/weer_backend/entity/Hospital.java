package com.weer.weer_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "HOSPITAL")
public class Hospital extends BaseEntity {

    @Id
    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;

    @Column(name = "ICU_ID")
    private Long icuId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMERGENCY_ID")
    private EmergencyRoomInfo emergencyId;

    @Column(name = "HPID")
    private String hpid;  // 병원 식별 코드 (ID)

    @Column(name = "NAME")
    private String name;  // 병원 이름

    @Column(name = "ADDRESS")
    private String address;  // 병원 주소

    @Column(name = "CITY")
    private String city;  // 시도

    @Column(name = "STATE")
    private String state;  // 구

    @Column(name = "TEL")
    private String tel;  // 대표 전화번호

    @Column(name = "ER_TEL")
    private String erTel;  // 응급실 전화번호

    @Column(name = "LATITUDE")
    private Double latitude;  // 위도

    @Column(name = "LONGITUDE")
    private Double longitude;  // 경도

}
