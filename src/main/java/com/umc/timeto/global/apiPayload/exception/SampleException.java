package com.umc.timeto.global.apiPayload.exception;

import com.umc.timeto.global.apiPayload.code.ErrorCode;

public class SampleException extends GlobalException{
    public SampleException(ErrorCode code) {
        super(code);
    }
}