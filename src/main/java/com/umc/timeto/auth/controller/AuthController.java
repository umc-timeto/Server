package com.umc.timeto.auth.controller;

import com.umc.timeto.auth.dto.*;
import com.umc.timeto.auth.service.AuthService;
import com.umc.timeto.global.apiPayload.code.ResponseCode;
import com.umc.timeto.global.apiPayload.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (request.getAuthorizationCode() == null ||
                request.getAuthorizationCode().isBlank()) {

            return ResponseEntity
                    .status(ResponseCode.COMMON400.getStatus())
                    .body(new ResponseDTO<>(ResponseCode.COMMON400));
        }

        AuthService.LoginResult result =
                authService.kakaoLogin(request.getAuthorizationCode());

        ResponseCode responseCode = result.isNewMember()
                ? ResponseCode.COMMON201
                : ResponseCode.COMMON200;

        return ResponseEntity
                .status(responseCode.getStatus())
                .body(new ResponseDTO<>(responseCode, result.response()));
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<LogoutResponse>> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        System.out.println("Authorization header = " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(ResponseCode.COMMON401.getStatus())
                    .body(new ResponseDTO<>(ResponseCode.COMMON401));
        }

        String accessToken = authorization.substring(7).trim();
        System.out.println("accessToken = " + accessToken);

        authService.logout(accessToken);

        return ResponseEntity
                .status(ResponseCode.COMMON200.getStatus())
                .body(new ResponseDTO<>(ResponseCode.COMMON200, new LogoutResponse("로그아웃 성공")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<TokenRefreshResponse>> refresh(
            @RequestBody TokenRefreshRequest request
    ) {
        String refreshToken = request.refreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity
                    .status(ResponseCode.COMMON401.getStatus())
                    .body(new ResponseDTO<>(ResponseCode.COMMON401));
        }

        try {
            TokenRefreshResponse response = authService.refresh(refreshToken.trim());

            return ResponseEntity
                    .status(ResponseCode.AUTH200.getStatus())
                    .body(new ResponseDTO<>(ResponseCode.AUTH200, response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(ResponseCode.COMMON401.getStatus())
                    .body(new ResponseDTO<>(ResponseCode.COMMON401));
        }
    }

}
