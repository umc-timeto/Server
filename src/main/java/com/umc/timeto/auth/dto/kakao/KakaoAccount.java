package com.umc.timeto.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

// 카카오 사용자 계정 정보를 담는 응답 DTO
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccount {

    private String email;
    private KakaoProfile profile;
}
