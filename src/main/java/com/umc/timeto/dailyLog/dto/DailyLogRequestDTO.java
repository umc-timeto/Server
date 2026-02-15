package com.umc.timeto.dailyLog.dto;

import com.umc.timeto.dailyLog.enums.Achievement;
import com.umc.timeto.dailyLog.enums.Satisfaction;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyLogRequestDTO {
    private Satisfaction answer1;
    private Achievement answer2;

    @Size(max = 255)
    private String answer3;
}
