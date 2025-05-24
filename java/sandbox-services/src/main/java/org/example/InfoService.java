package org.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class InfoService {

    public String getCurrentTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader != null && !xfHeader.isBlank())
                ? xfHeader.split(",")[0]
                : request.getRemoteAddr();
    }

}
