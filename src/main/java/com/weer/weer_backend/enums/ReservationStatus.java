package com.weer.weer_backend.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    APPROVED("approved"),
    DECLINED("declined"),
    CANCELLED("cancelled"),
    PENDING("pending");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }
}
