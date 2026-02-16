package com.umc.timeto.auth.controller;

import com.umc.timeto.auth.dto.KakaoLoginRequest;
import com.umc.timeto.auth.dto.KakaoLoginResponse;
import com.umc.timeto.auth.dto.LogoutResponse;
import com.umc.timeto.auth.dto.TokenRefreshRequest;
import com.umc.timeto.auth.dto.TokenRefreshResponse;
import com.umc.timeto.auth.service.AuthService;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 카카오 로그인
    @PostMapping("/kakao/login")
    public ResponseEntity<ResponseDTO<KakaoLoginResponse>> kakaoLogin(
            @RequestBody KakaoLoginRequest request
    ) {
        if (request.getAuthorizationCode() == null || request.getAuthorizationCode().isBlank()) {
            return ResponseEntity
                    .status(ResponseCode.COMMON400.getStatus())
                    .body(new ResponseDTO<>(ResponseCode.COMMON400));
        }

        AuthService.LoginResult result = authService.kakaoLogin(request.getAuthorizationCode());

        ResponseCode responseCode = result.isNewMember()
                ? ResponseCode.AUTH_KAKAO_SIGNUP_SUCCESS
                : ResponseCode.AUTH_KAKAO_LOGIN_SUCCESS;

        return ResponseEntity
                .status(responseCode.getStatus())
                .body(new ResponseDTO<>(responseCode, result.response()));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<LogoutResponse>> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new GlobalException(ErrorCode.AUTHORIZATION_HEADER_MISSING);
        }

        String accessToken = authorization.substring(7).trim();
        authService.logout(accessToken);

        return ResponseEntity
                .status(ResponseCode.AUTH_LOGOUT_SUCCESS.getStatus())
                .body(new ResponseDTO<>(ResponseCode.AUTH_LOGOUT_SUCCESS, new LogoutResponse("로그아웃 성공")));
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<TokenRefreshResponse>> refresh(
            @RequestBody TokenRefreshRequest request
    ) {
        String refreshToken = request.refreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        }

        TokenRefreshResponse response = authService.refresh(refreshToken.trim());

        return ResponseEntity
                .status(ResponseCode.AUTH_TOKEN_REISSUE_SUCCESS.getStatus())
                .body(new ResponseDTO<>(ResponseCode.AUTH_TOKEN_REISSUE_SUCCESS, response));
    }

    // 회원 탈퇴(소프트 딜리트)
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<String>> deleteAccount(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new GlobalException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        Object principal = authentication.getPrincipal();
        Long memberId;

        if (principal instanceof Long) {
            memberId = (Long) principal;
        } else if (principal instanceof String) {
            memberId = Long.parseLong((String) principal);
        } else {
            throw new GlobalException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        authService.requestDelete(memberId);

        return ResponseEntity
                .status(ResponseCode.AUTH_DELETE_SUCCESS.getStatus())
                .body(new ResponseDTO<>(ResponseCode.AUTH_DELETE_SUCCESS, "성공"));
    }
}
