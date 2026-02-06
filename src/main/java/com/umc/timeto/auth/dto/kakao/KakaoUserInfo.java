package com.umc.timeto.auth.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 카카오 API 응답을 서비스에서 사용하기 쉽게 변환한 사용자 정보 DTO
@Getter
@AllArgsConstructor
public class KakaoUserInfo {

    private Long kakaoId;
    private String email;
    private String name;
}
