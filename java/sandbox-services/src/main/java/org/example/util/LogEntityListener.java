package org.example.util;

import jakarta.persistence.PrePersist;
import org.example.entity.HttpRequestLog;

public class LogEntityListener {
  private static SystemTimeSource timeSource;

  public static void setTimeSource(SystemTimeSource ts) {
    timeSource = ts;
  }

  @PrePersist
  public void onPrePersist(HttpRequestLog httpRequestLog) {
    httpRequestLog.setTimestamp(timeSource.now());
  }
}
