package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "EMERGENCY")
public class Emergency extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMERGENCY_ID")
    private Long emergencyId;  // 응급실 고유 ID

    @Column(name = "HPID", unique = true)
    private String hpid;  // 병원 식별 코드 (ID)

    @Column(name = "HVEC")
    private Integer hvec;  // 일반

    @Column(name = "HV27")
    private Integer hv27;  // 코호트 격리

    @Column(name = "HV29")
    private Integer hv29;  // 음압 격리

    @Column(name = "HV30")
    private Integer hv30;  // 일반 격리

    @Column(name = "HV28")
    private Integer hv28;  // 소아 격리

    @Column(name = "HV15")
    private Integer hv15;  // 소아 음압 격리

    @Column(name = "HV16")
    private Integer hv16;  // 소아 일반 격리

    @Column(name = "HVS01")
    private Integer hvs01;  // 일반 병상 수 (기준)

    @Column(name = "HVS59")
    private Integer hvs59;  // 코로나 격리 병상 수 (기준)

    @Column(name = "HVS52")
    private Integer hvs52;  // 음압 격리 병상 수 (기준)

    @Column(name = "HVS51")
    private Integer hvs51;  // 일반 격리 병상 수 (기준)

    @Column(name = "HVS02")
    private Integer hvs02;  // 소아 격리 병상 수 (기준)

    @Column(name = "HVS48")
    private Integer hvs48;  // 소아 음압 격리 병상 수 (기준)

    @Column(name = "HVS49")
    private Integer hvs49;  // 소아 일반 격리 병상 수 (기준)

}
