package com.umc.timeto.global.apiPayload.exception;

import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.dto.ErrorResponseDTO;

public class GlobalException extends RuntimeException {
    private ErrorCode code;

    public GlobalException(ErrorCode code) {
        this.code = code;
    }

    public ErrorResponseDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
