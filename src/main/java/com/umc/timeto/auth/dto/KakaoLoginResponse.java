package com.umc.timeto.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 카카오 로그인 성공 시 서비스에서 발급한 토큰 정보를 담은 응답 DTO
@Getter
@AllArgsConstructor
public class KakaoLoginResponse {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
}
