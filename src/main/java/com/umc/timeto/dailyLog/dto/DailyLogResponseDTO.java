package com.umc.timeto.dailyLog.dto;

import com.umc.timeto.dailyLog.enums.Achievement;
import com.umc.timeto.dailyLog.enums.Satisfaction;

import java.time.LocalDate;

public record DailyLogResponseDTO(
        Long logId,
        Satisfaction answer1,
        Achievement answer2,
        String answer3,
        LocalDate createdAt
){}
