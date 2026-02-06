package com.umc.timeto.auth.client;

import com.umc.timeto.auth.dto.kakao.KakaoMeResponse;
import com.umc.timeto.auth.dto.kakao.KakaoTokenResponse;
import com.umc.timeto.auth.dto.kakao.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//카카오 서버와 직접 통신해서 인가코드 → 사용자 정보를 가져오는 외부 API 전용 클라이언트
@Component
@RequiredArgsConstructor
public class KakaoClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret:}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 인가코드로 카카오 access token 발급
    public String getAccessToken(String authorizationCode) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", authorizationCode);

        if (clientSecret != null && !clientSecret.isBlank()) {
            formData.add("client_secret", clientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                KakaoTokenResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("카카오 토큰 발급 실패");
        }

        String accessToken = response.getBody().getAccessToken();
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("카카오 access_token 응답 누락");
        }

        return accessToken;
    }

    // 카카오 access token으로 사용자 정보 조회
    public KakaoUserInfo getUserInfo(String kakaoAccessToken) {
        String meUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoMeResponse> response = restTemplate.exchange(
                meUrl,
                HttpMethod.GET,
                requestEntity,
                KakaoMeResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("카카오 유저 조회 실패");
        }

        KakaoMeResponse body = response.getBody();

        Long kakaoId = body.getId();
        if (kakaoId == null) {
            throw new IllegalStateException("카카오 id 응답 누락");
        }

        String email = null;
        String nickname = null;

        if (body.getKakaoAccount() != null) {
            email = body.getKakaoAccount().getEmail();

            if (body.getKakaoAccount().getProfile() != null) {
                nickname = body.getKakaoAccount().getProfile().getNickname();
            }
        }

        // 이메일은 카카오 동의/설정에 따라 null 가능 → 임시 이메일 처리
        if (email == null || email.isBlank()) {
            email = "kakao_" + kakaoId + "@kakao.local";
        }

        if (nickname == null || nickname.isBlank()) {
            nickname = "kakaoUser";
        }

        return new KakaoUserInfo(kakaoId, email, nickname);
    }
}
