package com.weer.weer_backend.enums;


import lombok.Getter;

@Getter
public enum Gender {
    UNKNOWN("알 수 없음"),
    FEMALE("여성"),
    MALE("남성");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
