package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.ERAnnouncement;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ERAnnouncementDTO {
    private Long announcementId;   // 공지사항 고유 ID
    private Long hospitalId;       // 병원 고유 ID
    private String msgType;        // 메시지 구분
    private String message;        // 공지 메시지 내용
    private String diseaseType;    // 중증 질환명
    private LocalDateTime createdAt; // 등록 일시

    public static ERAnnouncementDTO from(ERAnnouncement erAnnouncement){
        return ERAnnouncementDTO.builder()
            .announcementId(erAnnouncement.getAnnouncementId())
            .hospitalId(erAnnouncement.getHospitalId())
            .msgType(erAnnouncement.getMsgType())
            .message(erAnnouncement.getMessage())
            .diseaseType(erAnnouncement.getDiseaseType())
            .createdAt(erAnnouncement.getCreatedAt()).build();
    }
}
