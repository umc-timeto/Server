package com.umc.timeto.dailyLog.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Achievement {
    PERFECT(1, "완벽하게 지켰어요."),
    MOSTLY(2, "큰 틀에서 잘 따랐어요."),
    HALF(3, "절반 이상은 지켰어요."),
    SOMEWHAT(4, "계획이 자주 어긋났어요."),
    DIFFERENT(5, "계획과는 아주 다른 하루였어요.")
    ;

    private final int code;
    private final String description;
}
