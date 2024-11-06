package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    private Long userId;
    private String loginId;
    private String name;
    private String password;
    private String role;
    private String email;
    private String tel;
    private String certificate;
    private String organization;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    // Getters and Setters
}
