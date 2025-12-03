package com.pink.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E400001", "잘못된 입력값입니다."),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "E400002", "필수 필드가 누락되었습니다."),

    // 404 Not Found
    FUNCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "함수를 찾을 수 없습니다."),
    EXECUTION_NOT_FOUND(HttpStatus.NOT_FOUND, "E404002", "실행 기록을 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404999", "요청한 리소스를 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500001", "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500002", "데이터베이스 오류가 발생했습니다."),
    EXECUTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E500003", "함수 실행 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
