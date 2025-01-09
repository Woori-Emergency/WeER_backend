package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class UserDTO {
    private String loginId;
    private String name;
    private String password;
    private String email;
    private String tel;
    private String certificate;
    private String organization;
}
