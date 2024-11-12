package com.weer.weer_backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Approve {
    PENDING("대기"),
    APPROVED("승인"),
    UNAPPROVED("반려");

    String description;
}
