package com.umc.timeto.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 서비스의 카카오 로그인 요청(인가코드)을 받기 위한 DTO
@Getter
@NoArgsConstructor
public class KakaoLoginRequest {
    private String authorizationCode;
}
