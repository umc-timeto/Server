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
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpResponse;


@Component
@RequiredArgsConstructor
public class KakaoClient {

    private final RestTemplate restTemplate = createRestTemplate();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret:}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private RestTemplate createRestTemplate() {
        RestTemplate template = new RestTemplate();
        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
        return template;
    }

    private void validateKakaoConfig() {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("kakao.client-id 설정이 비어있습니다. (REST API 키가 들어가야 함)");
        }
        if (redirectUri == null || redirectUri.isBlank()) {
            throw new IllegalStateException("kakao.redirect-uri 설정이 비어있습니다. (인가코드 받을 때 사용한 redirect_uri와 동일해야 함)");
        }
    }

    // 인가코드로 카카오 access token 발급
    public String getAccessToken(String authorizationCode) {
        validateKakaoConfig();

        if (authorizationCode == null || authorizationCode.isBlank()) {
            throw new IllegalArgumentException("authorizationCode가 비어있습니다.");
        }

        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

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

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException(
                    "카카오 토큰 발급 실패 - status=" + response.getStatusCode()
                            + ", clientIdEmpty=" + (clientId == null || clientId.isBlank())
                            + ", redirectUri=" + redirectUri
                            + ", codeLength=" + authorizationCode.length()
            );
        }

        KakaoTokenResponse body = response.getBody();
        if (body == null || body.getAccessToken() == null || body.getAccessToken().isBlank()) {
            throw new IllegalStateException("카카오 access_token 응답 누락");
        }

        return body.getAccessToken();
    }

    // 카카오 access token으로 사용자 정보 조회
    public KakaoUserInfo getUserInfo(String kakaoAccessToken) {
        if (kakaoAccessToken == null || kakaoAccessToken.isBlank()) {
            throw new IllegalArgumentException("kakaoAccessToken이 비어있습니다.");
        }

        String meUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoMeResponse> response = restTemplate.exchange(
                meUrl,
                HttpMethod.GET,
                requestEntity,
                KakaoMeResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("카카오 유저 조회 실패 - status=" + response.getStatusCode());
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

        if (email == null || email.isBlank()) {
            email = "kakao_" + kakaoId + "@kakao.local";
        }

        if (nickname == null || nickname.isBlank()) {
            nickname = "kakaoUser";
        }

        return new KakaoUserInfo(kakaoId, email, nickname);
    }
}
