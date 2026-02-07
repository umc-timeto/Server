package com.umc.timeto.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

// 카카오 사용자 조회 API의 전체 응답을 매핑하는 DTO
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoMeResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}
