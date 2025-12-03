package com.codeit_team01.sb07_hrbank_team01.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;

    public CustomException(ErrorCode errorCode, String details) {
        super(details);
        this.errorCode = errorCode;
        this.details = details;
    }
}
