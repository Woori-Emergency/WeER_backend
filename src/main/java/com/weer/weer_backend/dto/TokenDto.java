package com.weer.weer_backend.dto;

import lombok.*;

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
