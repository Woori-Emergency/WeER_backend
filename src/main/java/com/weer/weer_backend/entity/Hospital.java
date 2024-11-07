package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "HOSPITAL", uniqueConstraints = @UniqueConstraint(columnNames = "HPID"))
public class Hospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQUIPMENT_ID")
    private Equipment equipmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ICU_ID")
    private Icu icuId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMERGENCY_ID")
    private Emergency emergencyId;

    @Column(name = "HPID", unique = true)
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
