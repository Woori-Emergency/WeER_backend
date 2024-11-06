package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class EmergencyDTO {
    private Long emergencyId;          // 응급실 고유 ID
    private Integer hvec;              // 일반 격리 병상 수
    private Integer hv27;              // 코로나 격리 병상 수
    private Integer hv29;              // 음압 격리 병상 수
    private Integer hv30;              // 일반 격리 병상 수
    private Integer hv28;              // 소아 격리 병상 수
    private Integer hv15;              // 소아 음압 격리 병상 수
    private Integer hv16;              // 소아 일반 격리 병상 수
    private Integer hvs01;             // 일반 격리 병상 수 (기준)
    private Integer hvs59;             // 코로나 격리 병상 수 (기준)
    private Integer hvs52;             // 음압 격리 병상 수 (기준)
    private Integer hvs51;             // 일반 격리 병상 수 (기준)
    private Integer hvs02;             // 소아 격리 병상 수 (기준)
    private Integer hvs48;             // 소아 음압 격리 병상 수 (기준)
    private Integer hvs49;             // 소아 일반 격리 병상 수 (기준)
    private LocalDateTime createdAt;            // 데이터 생성 일자
    private LocalDateTime  modifiedAt;           // 데이터 수정 일자
}
