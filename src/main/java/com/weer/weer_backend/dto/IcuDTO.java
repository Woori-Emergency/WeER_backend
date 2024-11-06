package com.weer.weer_backend.dto;

import lombok.Data;

@Data
public class IcuDTO {
    private Long icuId;           // 중환자실 고유 ID
    private Integer hvcc;         // 신경과 병상 수
    private Integer hvncc;        // 신생아 병상 수
    private Integer hvccc;        // 흉부외과 병상 수
    private Integer hvicc;        // 일반 병상 수
    private Integer hv2;          // 내과 병상 수
    private Integer hv3;          // 외과 병상 수
    private Integer hv6;          // 신경외과 병상 수
    private Integer hv8;          // 화상 병상 수
    private Integer hv9;          // 외상 병상 수
    private Integer hv32;         // 소아 병상 수
    private Integer hv34;         // 심장내과 병상 수
    private Integer hv35;         // 음압 격리 병상 수
    private Integer hvs11;        // 신경과 (기준) 병상 수
    private Integer hvs08;        // 신생아 (기준) 병상 수
    private Integer hvs16;        // 흉부외과 (기준) 병상 수
    private Integer hvs17;        // 일반 (기준) 병상 수
    private Integer hvs06;        // 내과 (기준) 병상 수
    private Integer hvs07;        // 외과 (기준) 병상 수
    private Integer hvs12;        // 신경외과 (기준) 병상 수
    private Integer hvs13;        // 화상 (기준) 병상 수
    private Integer hvs14;        // 외상 (기준) 병상 수
    private Integer hvs09;        // 소아 (기준) 병상 수
    private Integer hvs15;        // 심장내과 (기준) 병상 수
    private Integer hvs18;        // 음압 격리 (기준) 병상 수
}
