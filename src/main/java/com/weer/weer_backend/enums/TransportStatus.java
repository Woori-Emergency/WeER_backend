package com.weer.weer_backend.enums;

import lombok.Getter;

@Getter
public enum TransportStatus {
    IN_PROGRESS("이송 중"),
    COMPLETED("이송 완료");

    private final String status;

    TransportStatus(String status) {
        this.status = status;
    }
}
