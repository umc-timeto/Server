package com.umc.timeto.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutResponse {

    @Schema(description = "로그아웃 결과", example = "로그아웃 성공")
    private String message;
}
