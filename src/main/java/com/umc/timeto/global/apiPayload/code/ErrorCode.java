package com.umc.timeto.global.apiPayload.code;

import com.umc.timeto.global.apiPayload.dto.ErrorResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST - 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    BLOCK_TIME_CONFLICT(HttpStatus.BAD_REQUEST, "이미 해당 시간에 블록이 존재합니다."),
    INVALID_INDEX(HttpStatus.BAD_REQUEST, "인덱스 범위를 벗어났습니다"),
    // Auth - (400)
    AUTH_EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "이메일 정보가 없어 로그인할 수 없습니다."),
    AUTH_INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 제공자입니다."),
    AUTH_INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "인가 코드가 유효하지 않습니다."),
    AUTH_INVALID_REDIRECT_URI(HttpStatus.BAD_REQUEST, "리다이렉트 URI가 유효하지 않습니다."),

    /**
     * 401 UNAUTHORIZED - 인증 실패
     */
    AUTHORIZATION_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 필요합니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    AUTH_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."),
    AUTH_REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    AUTH_TOKEN_FORGED(HttpStatus.UNAUTHORIZED, "위조된 토큰입니다."),

    /**
     * 403 FORBIDDEN - 권한 없음
     */
    GOAL_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 작성한 목표가 아닙니다."),
    LOG_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 작성한 일지가 아닙니다."),
    AUTH_WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN, "탈퇴한 계정입니다."),



    /**
     * 404 NOT_FOUND - 요청한 리소스를 찾을 수 없음
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 유저가 존재하지 않습니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 목표가 존재하지 않습니다."),
    FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 폴더가 존재하지 않습니다."),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 아이디를 가진 할 일이 존재하지 않습니다."),
    BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 아이디를 가진 블록이 존재하지 않습니다."),
    LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 일지가 존재하지 않습니다."),


    /**
     * 406 NOT_ACCEPTABLE - 허용되지 않는 요청 형식
     */


    /**
     * 409 CONFLICT - 요청 충돌
     */
    AUTH_MEMBER_DUPLICATE(HttpStatus.CONFLICT, "이미 가입된 회원입니다."),
    AUTH_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    AUTH_SOCIAL_ID_DUPLICATE(HttpStatus.CONFLICT, "이미 연결된 소셜 계정입니다."),


    // Kakao - Response/Parsing (500)
    KAKAO_RESPONSE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 응답 처리 중 오류가 발생했습니다."),

    /**
     * 502 BAD_GATEWAY - 이트웨이 또는 프록시 서버 오류
     */
    KAKAO_USER_API_FAILED(HttpStatus.BAD_GATEWAY, "카카오 사용자 정보 조회에 실패했습니다.");


    private final HttpStatus status;
    private final String message;

    public ErrorResponseDTO getReasonHttpStatus() {
        return ErrorResponseDTO.builder()
                .message(message)
                .status(status.value())
                .isSuccess(false)
                .error(this.name())
                .build()
                ;
    }
}
