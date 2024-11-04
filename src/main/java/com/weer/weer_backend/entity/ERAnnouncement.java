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
@Table(name = "ER_ANNOUNCEMENT")
public class ERAnnouncement extends BaseEntity{

    @Id
    @Column(name = "ANNOUNCEMENT_ID")
    private Long announcementId;

    @Column(name = "HOSPITAL_ID")
    private Long hospitalId;

    private String title;

    private String message;

    private String status;

}
