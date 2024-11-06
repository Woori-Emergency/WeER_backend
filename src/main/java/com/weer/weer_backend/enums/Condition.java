package com.weer.weer_backend.enums;

public enum Condition {
    DISEASE("질병"),
    NONDISEASE("질병 외");

    private final String condition;

    Condition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
