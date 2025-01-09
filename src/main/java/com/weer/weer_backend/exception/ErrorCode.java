package com.weer.weer_backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  //login
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST,"아이디나 패스워드를 확인해 주세요"),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"회원을 찾을 수 없습니다."),
  PENDING_ACCOUNT(HttpStatus.BAD_REQUEST,"회원가입 요청 심사중입니다."),
  UNAPPROVED_ACCOUNT(HttpStatus.BAD_REQUEST,"반려된 계정입니다."),

  // ACCESS
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "로그인이 필요한 접근입니다."),

  // SignUp
  DATABASE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원정보 DB 저장에 실패하였습니다."),
  DUPLICATED_SIGNUP(HttpStatus.BAD_REQUEST, "중복된 정보가 존재합니다."),

  //Hospital
  NOT_FOUND_HOSPITAL(HttpStatus.BAD_REQUEST, "해당하는 병원을 찾을 수 없습니다."),
  NOT_FOUND_EMERGENCY(HttpStatus.BAD_REQUEST, "해당하는 응급실을 찾을 수 없습니다."),

  //Patient
  PATIENT_SAVE_FAIL(HttpStatus.BAD_REQUEST,"환자 상태 저장에 실패했습니다."),
  PATIENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "환자 정보가 존재하지 않습니다."),

  //locationInfo
  NOT_FOUND_ROUTE(HttpStatus.BAD_REQUEST,"경로를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
