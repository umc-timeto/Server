package com.umc.timeto.auth.controller;

import com.umc.timeto.auth.dto.KakaoLoginRequest;
import com.umc.timeto.auth.dto.KakaoLoginResponse;
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
}
