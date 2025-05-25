package org.example;

import jakarta.servlet.http.HttpServletRequest;
import org.example.util.DateTimeUtil;
import org.example.util.SystemTimeSource;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

  private final SystemTimeSource time;

  public InfoService(SystemTimeSource systemTimeSource) {
    this.time = systemTimeSource;
  }

  public String getCurrentTime() {
    return DateTimeUtil.formatted(time.now());
  }

  public String getClientIp(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    return (xfHeader != null && !xfHeader.isBlank())
        ? xfHeader.split(",")[0]
        : request.getRemoteAddr();
  }
}
