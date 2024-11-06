package com.weer.weer_backend.enums;

public enum ReservationStatus {
    APPROVED("approved"),
    DECLINED("declined"),
    PENDING("pending");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
