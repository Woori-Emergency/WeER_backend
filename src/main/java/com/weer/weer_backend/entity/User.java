package com.weer.weer_backend.entity;

import com.weer.weer_backend.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "USER")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL에서 auto increment 설정
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "LOGIN_ID")
    private String loginId;

    private String name;
    private String password;
    private String email;
    private String tel;
    private String certificate;
    private String organization;
    private Boolean approved; // 승인 여부 필드 추가
    @Enumerated(EnumType.STRING)
    private Role role;
}
