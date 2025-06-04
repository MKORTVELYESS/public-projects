package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.example.util.SystemTimeSource;
import org.junit.jupiter.api.Test;

class InfoServiceTest {

  @Test
  void getClientIp() {
    var service = new InfoService(new SystemTimeSource());
    var request = mock(HttpServletRequest.class);
    when(request.getHeader("X-Forwarded-For")).thenReturn(null);
    when(request.getRemoteAddr()).thenReturn("0.0.0.0.0");
    assertEquals("0.0.0.0.0", service.getClientIp(request));
    when(request.getHeader("X-Forwarded-For")).thenReturn("");
    assertEquals("0.0.0.0.0", service.getClientIp(request));
  }
}
