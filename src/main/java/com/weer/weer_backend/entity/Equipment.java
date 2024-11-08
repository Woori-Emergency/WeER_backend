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
@Table(name = "EQUIPMENT")
public class Equipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;  // 장비 고유 ID

    @Column(name = "HPID", unique = true)
    private String hpid;  // 병원 식별 코드 (ID)

    @Column(name = "HVVENTIAYN")
    private Boolean hvventiAYN;  // 인공호흡기 일반 사용 가능 여부

    @Column(name = "HVVENTISOAYN")
    private Boolean hvventisoAYN;  // 인공호흡기 조산아용 사용 가능 여부

    @Column(name = "HVINCUAYN")
    private Boolean hvinCUAYN;  // 인큐베이터 사용 가능 여부

    @Column(name = "HVCRRTAYN")
    private Boolean hvcrrTAYN;  // CRRT (신대체요법기) 사용 가능 여부

    @Column(name = "HVECMOAYN")
    private Boolean hvecmoAYN;  // ECMO (체외막 산소공급 장치) 사용 가능 여부

    @Column(name = "HVHYPHOAYN")
    private Boolean hvhypoAYN;  // 중심체온조절 장치 사용 가능 여부

    @Column(name = "HVOXYAYN")
    private Boolean hvoxyAYN;  // 고압 산소 치료기 사용 가능 여부

    @Column(name = "HVCTAYN")
    private Boolean hvctAYN;  // CT 스캔 사용 가능 여부

    @Column(name = "HVMRIAYN")
    private Boolean hvmriAYN;  // MRI 사용 가능 여부

    @Column(name = "HVANGIOAYN")
    private Boolean hvangioAYN;  // 혈관촬영기 사용 가능 여부

    @Column(name = "HVS30")
    private Integer hvs30;  // 인공호흡기 일반 수량 (기준)

    @Column(name = "HVS31")
    private Integer hvs31;  // 인공호흡기 조산아용 수량 (기준)

    @Column(name = "HVS32")
    private Integer hvs32;  // 인큐베이터 수량 (기준)

    @Column(name = "HVS33")
    private Integer hvs33;  // CRRT (기준) 수량

    @Column(name = "HVS34")
    private Integer hvs34;  // ECMO (기준) 수량

    @Column(name = "HVS35")
    private Integer hvs35;  // 중심체온조절 장치 (기준) 수량

    @Column(name = "HVS37")
    private Integer hvs37;  // 고압 산소 치료기 (기준) 수량

    @Column(name = "HVS27")
    private Integer hvs27;  // CT 수량 (기준)

    @Column(name = "HVS28")
    private Integer hvs28;  // MRI 수량 (기준)

    @Column(name = "HVS29")
    private Integer hvs29;  // 혈관촬영기 수량 (기준)
}
