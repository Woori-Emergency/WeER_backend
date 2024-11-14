package com.weer.weer_backend.dto;

import com.weer.weer_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenDto {
  private String grantType;
  private String role;
  private String accessToken;
  private String refreshToken;
}