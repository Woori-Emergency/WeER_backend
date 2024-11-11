package com.weer.weer_backend.event;

public class DataUpdateCompleteEvent {
    private final String message;

    public DataUpdateCompleteEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
