package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import org.example.util.DateTimeUtil;
import org.example.util.SystemTimeSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@AutoConfigureMockMvc
class ControllerTest {
  private final OffsetDateTime testTime =
      OffsetDateTime.of(2025, 5, 24, 12, 47, 3, 0, ZoneOffset.UTC);
  private final SystemTimeSource mockSystemTimeSource;
  private final Controller controller;
  private final HttpRequestLogRepository httpRequestLogRepository;

  @Autowired
  public ControllerTest(
      SystemTimeSource systemTimeSource,
      Controller controller,
      HttpRequestLogRepository logRepository) {
    this.mockSystemTimeSource = systemTimeSource;
    this.controller = controller;
    this.httpRequestLogRepository = logRepository;
  }

  @Autowired private HttpRequestLogRepository logRepository;

  @Autowired private MockMvc mockMvc;

  private RequestPostProcessor remoteAddr(String ip) {
    return request -> {
      request.setRemoteAddr(ip);
      return request;
    };
  }

  @Test
  void health() {
    final String ip = "123.45.67.89.health.test";
    final String time = DateTimeUtil.formatted(testTime);
    final String status = "up";

    when(mockSystemTimeSource.now()).thenReturn(testTime);

    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET", "/health");
    mockHttpServletRequest.addHeader("X-Forwarded-For", ip);

    var expected = Map.of("whoami", ip, "time", time, "status", status);
    var actual = controller.health(mockHttpServletRequest);

    assertEquals(expected, actual);
  }

  @Test
  void shouldTriggerFilterWhenEndpointCalled() throws Exception {

    when(mockSystemTimeSource.now()).thenReturn(testTime);
    final String ip = "123.45.67.89.health.test";
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/health")
                .header("X-Forwarded-For", ip)
                .with(remoteAddr(ip)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("up"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.whoami").value(ip))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.time").value(DateTimeUtil.formatted(testTime)));

    List<HttpRequestLog> expected =
        List.of(
            new HttpRequestLog(
                1L,
                "GET",
                "/health",
                null,
                "{\"X-Forwarded-For\":\"123.45.67.89.health.test\"}",
                "",
                ip,
                testTime));
    List<HttpRequestLog> actual = logRepository.findByRemoteIp(ip);

    assertEquals(expected, actual);
  }
}
