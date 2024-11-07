package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalFilterDto {
  private String city; // 시
  private String state; // 구

  //응급실
  private boolean hvec;  // 일반 격리 병상
  private boolean hv27;  // 코로나 격리 병상
  private boolean hv29;  // 음압 격리 병상
  private boolean hv30;  // 일반 격리 병상
  private boolean hv28;  // 소아 격리 병상
  private boolean hv15;  // 소아 음압 격리 병상
  private boolean hv16;  // 소아 일반 격리 병상

  //중환자실
  private boolean hvcc;  // 신경과 병상 수
  private boolean hvncc;  // 신생아 병상 수
  private boolean hvccc;  // 흉부외과 병상 수
  private boolean hvicc;  // 일반 병상 수
  private boolean hv2;  // 내과 병상 수
  private boolean hv3;  // 외과 병상 수
  private boolean hv6;  // 신경외과 병상 수
  private boolean hv8;  // 화상 병상 수
  private boolean hv9;  // 외상 병상 수
  private boolean hv32;  // 소아 병상 수
  private boolean hv34;  // 심장내과 병상 수
  private boolean hv35;  // 음압 격리 병상 수

  //장비
  private boolean hvventiAYN;  // 인공호흡기 일반 사용 가능 여부
  private boolean hvventisoAYN;  // 인공호흡기 조산아용 사용 가능 여부
  private boolean hvinCUAYN;  // 인큐베이터 사용 가능 여부
  private boolean hvcrrTAYN;  // CRRT (신대체요법기) 사용 가능 여부
  private boolean hvecmoAYN;  // ECMO (체외막 산소공급 장치) 사용 가능 여부
  private boolean hvhypoAYN;  // 중심체온조절 장치 사용 가능 여부
  private boolean hvoxyAYN;  // 고압 산소 치료기 사용 가능 여부
  private boolean hvctAYN;  // CT 스캔 사용 가능 여부
  private boolean hvmriAYN;  // MRI 사용 가능 여부
  private boolean hvangioAYN; // 혈관촬영기 사용 가능 여부
}
