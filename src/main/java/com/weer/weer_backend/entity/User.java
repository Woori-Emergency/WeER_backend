package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "USER")
public class User extends BaseEntity{

    @Id
    @Column(name = "USER_ID")
    private Long userId;
    private String loginId;
    private String name;
    private String password;
    private String role;
    private String email;
    private String tel;
    private String certificate;
    private String organization;

}
