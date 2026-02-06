package com.umc.timeto.global.apiPayload.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
    // Common
    COMMON200(HttpStatus.OK, "요청에 성공하였습니다."),
    COMMON201(HttpStatus.CREATED, "회원 가입 및 로그인 성공"),
    COMMON400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON401(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    // Auth
    AUTH200(HttpStatus.OK, "토큰 재발급에 성공하였습니다."),


    // Auth
    AUTH_LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다.");

    private final HttpStatus status;
    private final String message;
}
