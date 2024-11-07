package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "ICU")
public class Icu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ICU_ID")
    private Long icuId;  // 중환자실 고유 ID

    @Column(name = "HVCC")
    private Integer hvcc;  // 신경과 병상 수

    @Column(name = "HVNCC")
    private Integer hvncc;  // 신생아 병상 수

    @Column(name = "HVCCC")
    private Integer hvccc;  // 흉부외과 병상 수

    @Column(name = "HVICC")
    private Integer hvicc;  // 일반 병상 수

    @Column(name = "HV2")
    private Integer hv2;  // 내과 병상 수

    @Column(name = "HV3")
    private Integer hv3;  // 외과 병상 수

    @Column(name = "HV6")
    private Integer hv6;  // 신경외과 병상 수

    @Column(name = "HV8")
    private Integer hv8;  // 화상 병상 수

    @Column(name = "HV9")
    private Integer hv9;  // 외상 병상 수

    @Column(name = "HV32")
    private Integer hv32;  // 소아 병상 수

    @Column(name = "HV34")
    private Integer hv34;  // 심장내과 병상 수

    @Column(name = "HV35")
    private Integer hv35;  // 음압 격리 병상 수

    @Column(name = "HVS11")
    private Integer hvs11;  // 신경과 (기준) 병상 수

    @Column(name = "HVS08")
    private Integer hvS08;  // 신생아 (기준) 병상 수

    @Column(name = "HVS16")
    private Integer hvs16;  // 흉부외과 (기준) 병상 수

    @Column(name = "HVS17")
    private Integer hvs17;  // 일반 (기준) 병상 수

    @Column(name = "HVS06")
    private Integer hvs06;  // 내과 (기준) 병상 수

    @Column(name = "HVS07")
    private Integer hvs07;  // 외과 (기준) 병상 수

    @Column(name = "HVS12")
    private Integer hvs12;  // 신경외과 (기준) 병상 수

    @Column(name = "HVS13")
    private Integer hvs13;  // 화상 (기준) 병상 수

    @Column(name = "HVS14")
    private Integer hvs14;  // 외상 (기준) 병상 수

    @Column(name = "HVS09")
    private Integer hvS09;  // 소아 (기준) 병상 수

    @Column(name = "HVS15")
    private Integer hvs15;  // 심장내과 (기준) 병상 수

    @Column(name = "HVS18")
    private Integer hvs18;  // 음압 격리 (기준) 병상 수

}
