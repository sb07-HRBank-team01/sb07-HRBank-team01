package com.codeit_team01.sb07_hrbank_team01.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // DEP
    DEP_NOT_FOUND(HttpStatus.NOT_FOUND, "DEP001", "DEP001"),

    // EMP
    EMP_NOT_FOUND(HttpStatus.NOT_FOUND, "EMP001", "직원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "EMP002", "이메일은 중복될 수 없습니다."),

    // FILE
    DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "디렉토리 생성에 실패했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F002", "파일을 찾을 수 없습니다."),
    FILE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F003", "파일 처리 중 오류가 발생했습니다.");

    // 이력

    // 백업

    private final HttpStatus status;
    private final String code;
    private final String message;
}
