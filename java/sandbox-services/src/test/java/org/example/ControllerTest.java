package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.example.controller.Controller;
import org.example.entity.HttpRequestLog;
import org.example.repository.HttpRequestLogRepository;
import org.example.util.DateTimeUtil;
import org.example.util.SystemTimeSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StopWatch;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@AutoConfigureMockMvc
class ControllerTest {
  private final OffsetDateTime testTime =
      OffsetDateTime.of(2025, 5, 24, 12, 47, 3, 0, ZoneOffset.UTC);
  private final SystemTimeSource mockSystemTimeSource;
  private final Controller controller;

  @Autowired
  public ControllerTest(
      SystemTimeSource systemTimeSource,
      Controller controller,
      HttpRequestLogRepository logRepository) {
    this.mockSystemTimeSource = systemTimeSource;
    this.controller = controller;
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
    String url = "/api/health";
    mockMvc
        .perform(MockMvcRequestBuilders.get(url).header("X-Forwarded-For", ip).with(remoteAddr(ip)))
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
                url,
                null,
                "{\"X-Forwarded-For\":\"123.45.67.89.health.test\"}",
                "",
                ip,
                testTime));
    List<HttpRequestLog> actual = logRepository.findByRemoteIp(ip);

    assertEquals(expected, actual);
  }

  @Test
  void shouldTriggerFilterWhenEndpointCalled2() throws Exception {

    when(mockSystemTimeSource.now()).thenReturn(testTime);
    final String ip = "123.45.67.89.health.test2";
    String url = "/api/health";
    mockMvc
        .perform(MockMvcRequestBuilders.get(url).header("X-Forwarded-For", ip).with(remoteAddr(ip)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("up"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.whoami").value(ip))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.time").value(DateTimeUtil.formatted(testTime)));

    List<HttpRequestLog> expected =
        List.of(
            new HttpRequestLog(
                1L, "GET", url, null, "{\"X-Forwarded-For\":\"" + ip + "\"}", "", ip, testTime));
    List<HttpRequestLog> actual = logRepository.findByRemoteIp(ip);

    assertEquals(expected, actual);
  }

  @Test
  void shouldNotThrowExceptionWhenCalledWithValidJobBody() throws Exception {

    when(mockSystemTimeSource.now()).thenReturn(testTime);
    final String body =
        "insert_job: sb-services-job-load job_type: CMD machine: sndb1 owner: sandp condition: s(sb-services-job-input-fw) max_run_alarm:1000 min_run_alarm:400"
            + "insert_job: sb-services-job-input-fw job_type: FW machine: sndb2 owner: sandp date_conditions:1 start_times: 13\\:00  max_run_alarm:1000 min_run_alarm:400"
            + "insert_job: sb-services-job-box job_type: BOX machine: sndb2 owner: sandp date_conditions:1 start_times: \"12:00\" max_run_alarm:1000 min_run_alarm:400";
    final String hugeBody =
        IntStream.range(0, 4000).mapToObj(num -> body).collect(Collectors.joining(" "));
    final StopWatch sw = new StopWatch();
    sw.start();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/jil/print")
                .contentType(MediaType.TEXT_PLAIN)
                .content(hugeBody))
        .andExpect(MockMvcResultMatchers.status().isOk());
    sw.stop();
    System.out.println(sw.getTotalTimeSeconds());
  }
}
