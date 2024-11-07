package com.weer.weer_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String name;
    private String loginId;
    private String tel;
    private String organization;
    private LocalDateTime createdAt;

    // 생성자, Getter, Setter
}