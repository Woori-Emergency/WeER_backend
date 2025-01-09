package com.weer.weer_backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  MEMBER("일반 회원"),
  ADMIN("관리자"),
  HOSPITAL_ADMIN("병원 관리자");

  String description;
}
