package com.weer.weer_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "ER_ANNOUNCEMENT")
public class ERAnnouncement {

    @Id
    @Column(name = "ANNOUNCEMENT_ID")
    private Long announcementId;  // 공지사항 고유 ID

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;  // 병원 고유 ID

    @Column(name = "MSG_TYPE")
    private String msgType;  // 메시지 구분

    @Column(name = "MESSAGE")
    private String message;  // 공지 메시지 내용

    @Column(name = "DISEASE_TYPE")
    private String diseaseType;  // 중증 질환명

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;  // 등록 일시
}
