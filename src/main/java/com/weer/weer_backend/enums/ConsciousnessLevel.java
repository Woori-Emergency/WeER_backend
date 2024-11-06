package com.weer.weer_backend.enums;

public enum ConsciousnessLevel {
    ALERT("A", "Alert-명료"),
    VERBAL("V", "Verbal-언어반응"),
    PAIN("P", "Pain-통증반응"),
    UNRESPONSIVE("U", "Unresponsive-무반응");

    private final String code;
    private final String description;

    ConsciousnessLevel(String code, String description) {
        this.code = code;
        this.description = description;
    }
}