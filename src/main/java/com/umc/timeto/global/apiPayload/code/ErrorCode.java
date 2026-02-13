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

    /**
     * 401 UNAUTHORIZED - 인증 실패
     */


    /**
     * 403 FORBIDDEN - 권한 없음
     */
    GOAL_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 작성한 목표가 아닙니다."),
    LOG_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 작성한 일지가 아닙니다."),


    /**
     * 404 NOT_FOUND - 요청한 리소스를 찾을 수 없음
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 유저가 존재하지 않습니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 목표가 존재하지 않습니다."),
    FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 폴더가 존재하지 않습니다."),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 아이디를 가진 할 일이 존재하지 않습니다."),
    LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디를 가진 일지가 존재하지 않습니다."),


    /**
     * 406 NOT_ACCEPTABLE - 허용되지 않는 요청 형식
     */


    /**
     * 409 CONFLICT - 요청 충돌
     */
    DUPLICATE_DAILY_LOG(HttpStatus.CONFLICT, "일지는 하루에 하나만 작성할 수 있습니다.")


    /**
     * 502 BAD_GATEWAY - 이트웨이 또는 프록시 서버 오류
     */

    ;

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
