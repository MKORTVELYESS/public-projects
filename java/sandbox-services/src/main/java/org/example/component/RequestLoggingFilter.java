package org.example.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import org.example.entity.HttpRequestLog;
import org.example.repository.HttpRequestLogRepository;
import org.example.servlet.CachedBodyHttpServletRequest;
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
    CachedBodyHttpServletRequest cachedRequest =
        CachedBodyHttpServletRequest.fromHttpServletRequest(request);

    String body =
        cachedRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    String headers =
        new ObjectMapper()
            .writeValueAsString(
                Collections.list(cachedRequest.getHeaderNames()).stream()
                    .collect(Collectors.toMap(h -> h, cachedRequest::getHeader)));

    HttpRequestLog log = new HttpRequestLog();
    log.setMethod(cachedRequest.getMethod());
    log.setPath(cachedRequest.getRequestURI());
    log.setQueryParams(cachedRequest.getQueryString());
    log.setHeaders(headers);
    log.setBody(body);
    log.setRemoteIp(cachedRequest.getRemoteAddr());

    logRepository.save(log);

    filterChain.doFilter(cachedRequest, response);
  }
}
