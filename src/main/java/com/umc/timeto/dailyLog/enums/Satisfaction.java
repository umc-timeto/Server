package com.umc.timeto.dailyLog.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Satisfaction {
    GREAT(1, "만족스러웠어요"),
    SOSO(2, "그저 그랬어요"),
    BAD(3, "아쉬웠어요");

    private final int code;
    private final String description;
}
