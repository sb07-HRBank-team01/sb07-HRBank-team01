package com.codeit_team01.sb07_hrbank_team01.common.exception.dto;

import com.codeit_team01.sb07_hrbank_team01.common.exception.CustomException;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDto(
        String timestamp,
        int status,
        String message,
        String details
) {
    public static ErrorResponseDto from(CustomException e) {

        String combinedMessage = String.format("[%s] %s",
                e.getErrorCode().getCode(), e.getErrorCode().getMessage());

        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(e.getErrorCode().getStatus().value())
                .message(combinedMessage)
                .details(e.getDetails())
                .build();
    }

    public static ErrorResponseDto of(HttpStatus status, String message, String details) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .message(message)
                .details(details)
                .build();
    }
}
