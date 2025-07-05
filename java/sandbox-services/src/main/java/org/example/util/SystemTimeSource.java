package org.example.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class SystemTimeSource {

  public OffsetDateTime now() {
    return OffsetDateTime.now(ZoneId.of("UTC"));
  }

  public LocalDate currentDate() {
    return LocalDate.now();
  }

  public LocalDateTime currentTime() {
    return LocalDateTime.now();
  }
}
