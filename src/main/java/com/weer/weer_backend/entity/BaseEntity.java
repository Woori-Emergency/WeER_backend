package com.weer.weer_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)  // DB 컬럼명은 변경하지 않음
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")  // DB 컬럼명은 변경하지 않음
    private LocalDateTime modifiedAt;
}