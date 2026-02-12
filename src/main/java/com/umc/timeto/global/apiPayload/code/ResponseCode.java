package com.umc.timeto.global.apiPayload.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
    /**
     * Goal
     */

    SUCCESS_ADD_GOAL(HttpStatus.CREATED, "목표를 성공적으로 등록했습니다."),
    SUCCESS_GET_GOALLIST(HttpStatus.OK, "목표 리스트를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_GOAL(HttpStatus.OK, "목표를 성공적으로 수정했습니다."),
    SUCCESS_DELETE_GOAL(HttpStatus.OK, "목표를 성공적으로 삭제했습니다."),

    /**
     * Folder
     */
    SUCCESS_ADD_FOLDER(HttpStatus.CREATED, "폴더를 성공적으로 등록했습니다."),
    SUCCESS_GET_FOLDERLIST(HttpStatus.OK, "폴더 리스트를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_FOLDER(HttpStatus.OK, "폴더를 성공적으로 수정했습니다."),
    SUCCESS_DELETE_FOLDER(HttpStatus.OK, "폴더를 성공적으로 삭제했습니다."),


    /**
     * block
     */
    SUCCESS_ADD_BLOCK(HttpStatus.CREATED, "블록을 성공적으로 등록했습니다."),
    SUCCESS_GET_BLOCKLIST(HttpStatus.OK, "블록 리스트를 성공적으로 불러왔습니다."),
    SUCCESS_GET_BLOCK_NUMBER(HttpStatus.OK, "블록 수를 성공적으로 불러왔습니다."),

    // Common
    COMMON200(HttpStatus.OK, "요청에 성공하였습니다."),
    COMMON201(HttpStatus.CREATED, "회원 가입 및 로그인 성공"),
    COMMON400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON401(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    // Auth
    AUTH200(HttpStatus.OK, "토큰 재발급에 성공하였습니다."),


    // Auth
    AUTH_LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
