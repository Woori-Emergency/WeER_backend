package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentDTO {
    private Long equipmentId;       // 장비 고유 ID
    private Boolean hvventiAYN;     // 인공호흡기 일반 사용 가능 여부
    private Boolean hvventisoAYN;   // 인공호흡기 조산아용 사용 가능 여부
    private Boolean hvinCUAYN;      // 인큐베이터 사용 가능 여부
    private Boolean hvcrrTAYN;      // CRRT (신대체요법기) 사용 가능 여부
    private Boolean hvecmoAYN;      // ECMO (체외막 산소공급 장치) 사용 가능 여부
    private Boolean hvhypoAYN;      // 중심체온조절 장치 사용 가능 여부
    private Boolean hvoxyAYN;       // 고압 산소 치료기 사용 가능 여부
    private Boolean hvctAYN;        // CT 스캔 사용 가능 여부
    private Boolean hvmriAYN;       // MRI 사용 가능 여부
    private Boolean hvangioAYN;     // 혈관촬영기 사용 가능 여부
    private Integer hvs30;          // 인공호흡기 일반 수량 (기준)
    private Integer hvs31;          // 인공호흡기 조산아용 수량 (기준)
    private Integer hvs32;          // 인큐베이터 수량 (기준)
    private Integer hvs33;          // CRRT 수량 (기준)
    private Integer hvs34;          // ECMO 수량 (기준)
    private Integer hvs35;          // 중심체온조절 장치 수량 (기준)
    private Integer hvs37;          // 고압 산소 치료기 수량 (기준)
    private Integer hvs27;          // CT 수량 (기준)
    private Integer hvs28;          // MRI 수량 (기준)
    private Integer hvs29;          // 혈관촬영기 수량 (기준)
    private LocalDateTime createdAt;         // 데이터 생성 일자
    private LocalDateTime  modifiedAt;        // 데이터 수정 일자
}
