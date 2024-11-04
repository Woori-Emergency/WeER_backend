package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long userId;
    private String loginId;
    private String name;
    private String password;
    private String role;
    private String email;
    private String tel;
    private Date createdAt;
    private Date modifiedAt;

    // Getters and Setters
}
