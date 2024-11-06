package com.weer.weer_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ERAnnouncementDTO {
    private Long announcementId;
    private Long hospitalId;
    private String title;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
