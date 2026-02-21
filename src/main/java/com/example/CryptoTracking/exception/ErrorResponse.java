package com.example.CryptoTracking.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int code,
        String message,
        String path,
        Object details
) {
    public ErrorResponse(int code, String message, String path) {
        this(Instant.now(), code, message, path, null);
    }
}
