package com.weer.weer_backend.enums;

import lombok.Getter;

@Getter
public enum Medical {
    DISEASE("질병"),
    NON_DISEASE("질병 외");

    private final String medical;
    Medical(String medical) {
        this.medical = medical;
    }

}
