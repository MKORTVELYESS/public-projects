package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

  @Autowired private HttpRequestLogRepository logRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    String headers =
        new ObjectMapper()
            .writeValueAsString(
                Collections.list(request.getHeaderNames()).stream()
                    .collect(Collectors.toMap(h -> h, request::getHeader)));

    HttpRequestLog log = new HttpRequestLog();
    log.setMethod(request.getMethod());
    log.setPath(request.getRequestURI());
    log.setQueryParams(request.getQueryString());
    log.setHeaders(headers);
    log.setBody(body);
    log.setRemoteIp(request.getRemoteAddr());

    logRepository.save(log);

    filterChain.doFilter(request, response);
  }
}
