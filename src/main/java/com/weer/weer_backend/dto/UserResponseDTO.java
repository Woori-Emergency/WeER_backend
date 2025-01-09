package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String name;
    private String loginId;
    private String tel;
    private String organization;
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .name(user.getName())
                .loginId(user.getLoginId())
                .tel(user.getTel())
                .organization(user.getOrganization())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
