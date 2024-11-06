package com.weer.weer_backend.enums;

//알 수 없음
public enum Gender {
    FEMALE("여성"),
    MALE("남성");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return gender;
    }
}
