package com.weer.weer_backend.enums;

// 미상 추가
public enum AgeGroup {
    INFANT_TODDLER("영유아"),
    TEEN("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FOURTIES("40대"),
    FIFTIES("50대"),
    SIXTIES("60대"),
    SEVENTIES_PLUS("70대 이상");

    private final String label;

    AgeGroup(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}