package com.weer.weer_backend.enums;

import lombok.Getter;

@Getter
public enum Condition {
    DISEASE("질병"),
    NON_DISEASE("질병 외");

    private final String condition;

    Condition(String condition) {
        this.condition = condition;
    }

}
