package com.umc.timeto.block.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class BusinessDayPolicy {

    private static final LocalTime DAY_START = LocalTime.of(5, 0);

    public LocalDateTime startOfBusinessDay(LocalDateTime base) {
        LocalDate date = base.toLocalDate();

        if (base.toLocalTime().isBefore(DAY_START)) {
            date = date.minusDays(1);
        }

        return date.atTime(DAY_START);
    }

    public LocalDateTime endOfBusinessDay(LocalDateTime base) {
        return startOfBusinessDay(base).plusDays(1);
    }
}

