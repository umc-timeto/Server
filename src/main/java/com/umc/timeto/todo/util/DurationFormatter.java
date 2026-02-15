package com.umc.timeto.todo.util;

import java.time.LocalTime;

public class DurationFormatter {
    public static String format(LocalTime time) {
        if (time == null) return null;

        int h = time.getHour();
        int m = time.getMinute();

        if (h > 0 && m > 0) return h + "H " + m + "M";
        if (h > 0) return h + "H";
        return m + "M";
    }

    private DurationFormatter() {}
}
