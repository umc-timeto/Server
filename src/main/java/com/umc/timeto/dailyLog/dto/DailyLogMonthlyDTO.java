package com.umc.timeto.dailyLog.dto;

import com.umc.timeto.dailyLog.enums.Satisfaction;

import java.time.LocalDate;

public record DailyLogMonthlyDTO(
        Long logId,
        LocalDate date,
        Satisfaction satisfaction
) {}
