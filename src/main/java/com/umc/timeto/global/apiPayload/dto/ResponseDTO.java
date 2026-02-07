package com.umc.timeto.global.apiPayload.dto;

import com.umc.timeto.global.apiPayload.code.ResponseCode;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
public class ResponseDTO<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private Integer status;

    @Schema(description = "응답 코드", example = "COMMON200")
    private String code;

    @Schema(description = "응답 메시지", example = "요청에 성공하였습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public ResponseDTO(ResponseCode responseCode, T data) {
        this.status = responseCode.getStatus().value();
        this.code = responseCode.name();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public ResponseDTO(ResponseCode responseCode) {
        this.status = responseCode.getStatus().value();
        this.code = responseCode.name();
        this.message = responseCode.getMessage();
        this.data = null;
    }
}
