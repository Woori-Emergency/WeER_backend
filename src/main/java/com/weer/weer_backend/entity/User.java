package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "USER_info")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;
    private String loginId;
    private String name;
    private String password;
    private String role;
    private String email;
    private String tel;
    private String organization;
    private boolean approved;
}
