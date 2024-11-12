package com.weer.weer_backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  //register
  ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST,"이미 가입된 회원입니다"),

  //login
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST,"아이디나 패스워드를 확인해 주세요"),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"회원을 찾을 수 없습니다."),
  UNAPPROVED_ACCOUNT(HttpStatus.BAD_REQUEST,"반려된 계정입니다."),

  //Hospital
  NOT_FOUND_HOSPITAL(HttpStatus.BAD_REQUEST, "해당하는 병원을 찾을 수 없습니다."),
  NOT_FOUND_EMERGENCY(HttpStatus.BAD_REQUEST, "해당하는 응급실을 찾을 수 없습니다."),

  //locationInfo
  NOT_FOUND_ROUTE(HttpStatus.BAD_REQUEST,"경로를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
