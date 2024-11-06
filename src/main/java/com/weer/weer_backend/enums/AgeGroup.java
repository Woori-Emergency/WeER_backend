package com.weer.weer_backend.enums;

import lombok.Getter;

@Getter
public enum AgeGroup {
    UNKNOWN("알 수 없음"),
    INFANT_TODDLER("영유아"),
    TEEN("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES("60대"),
    SEVENTIES_PLUS("70대 이상");

    private final String label;

    AgeGroup(String label) {
        this.label = label;
    }
}