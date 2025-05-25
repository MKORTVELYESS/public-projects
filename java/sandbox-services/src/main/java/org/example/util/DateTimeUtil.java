package org.example.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
  private static final DateTimeFormatter iso = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  public static String formatted(OffsetDateTime time) {
    return time.format(iso);
  }
}
