package com.umc.timeto.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRefreshResponse {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "새로 발급된 accessToken", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "새로 발급된 refreshToken", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;
}
