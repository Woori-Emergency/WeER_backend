package com.weer.weer_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ERAnnouncementDTO {
    private Long announcementId;
    private Long hospitalId;
    private String title;
    private String message;
    private String status;
    private Date createdAt;
    private Date modifiedAt;
}
