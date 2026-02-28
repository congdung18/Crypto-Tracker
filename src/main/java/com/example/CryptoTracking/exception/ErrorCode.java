package com.example.CryptoTracking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Unexpected system exception
    SYSTEM_UNCATEGORIZED_EXCEPTION(9999, "Uncategorized system error, please try again", HttpStatus.INTERNAL_SERVER_ERROR),

    // Application exception
    // Error code: 10xx
    APP_API_FETCH_EXCEPTION(1000, "API fetch error", HttpStatus.BAD_GATEWAY),
    APP_RESOURCE_NOT_FOUND(1001, "Resource not found in Database", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
