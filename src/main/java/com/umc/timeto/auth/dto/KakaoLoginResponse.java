package com.umc.timeto.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 카카오 로그인 성공 시 서비스에서 발급한 토큰 정보를 담은 응답 DTO
@Getter
@AllArgsConstructor
public class KakaoLoginResponse {
    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(
            description = "JWT 액세스 토큰",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String accessToken;

    @Schema(
            description = "JWT 리프레시 토큰",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String refreshToken;
}
