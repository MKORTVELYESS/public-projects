package org.example.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter iso = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String formatted(ZonedDateTime time){
        return time.format(iso);
    }

}
