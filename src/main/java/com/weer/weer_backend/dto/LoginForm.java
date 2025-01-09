package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginForm {
  private String loginId;
  private String password;
}
