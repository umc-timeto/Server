package com.umc.timeto.todo.util;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {
    private static final Pattern H_PATTERN = Pattern.compile("(\\d+)\\s*[Hh]");
    private static final Pattern M_PATTERN = Pattern.compile("(\\d+)\\s*[Mm]");

    public static LocalTime parseToLocalTime(String raw) {
        if (raw == null) throw new IllegalArgumentException("duration is null");

        String s = raw.trim();
        int hours = 0;
        int minutes = 0;

        Matcher hm = H_PATTERN.matcher(s);
        if (hm.find()) hours = Integer.parseInt(hm.group(1));

        Matcher mm = M_PATTERN.matcher(s);
        if (mm.find()) minutes = Integer.parseInt(mm.group(1));

        // "90m" 같은 경우 hours가 0이고 minutes만 있음 → 1h30m로 환산
        if (hours == 0 && minutes >= 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }

        if (hours == 0 && minutes == 0) {
            throw new IllegalArgumentException("Invalid duration format: " + raw);
        }

        // LocalTime은 0~23 제한. 24시간 넘어갈 수 있으면 Duration/초단위 컬럼으로 바꾸는 게 좋음.
        if (hours > 23) {
            throw new IllegalArgumentException("Duration exceeds 23 hours (TIME column limitation). raw=" + raw);
        }

        return LocalTime.of(hours, minutes);
    }

    private DurationParser() {}
}
