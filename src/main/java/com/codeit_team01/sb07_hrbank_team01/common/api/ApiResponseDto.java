package com.codeit_team01.sb07_hrbank_team01.common.api;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ApiResponseDto<T>(
        String timestamp,
        int status,
        String message,
        T data
) {

    public static <T> ApiResponseDto<T> success(T data) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponseDto<T> success(String message) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> ApiResponseDto<T> of(HttpStatus status, String message,  T data) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }

}
