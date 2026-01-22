package org.example;

import static org.example.JsonCompareUtil.compareJsonAndList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;
import org.example.controller.Controller;
import org.example.entity.*;
import org.example.repository.*;
import org.example.service.HtmlFetchService;
import org.example.service.TapologyService;
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
import org.springframework.test.web.servlet.MvcResult;
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
      HttpRequestLogRepository logRepository,
      PrimeRepository primeRepository) {
    this.mockSystemTimeSource = systemTimeSource;
    this.controller = controller;
  }

  @Autowired private HttpRequestLogRepository logRepository;
  @Autowired private PrimeRepository primeRepository;
  @Autowired private MockMvc mockMvc;
  @Autowired private FighterRepository fighterRepository;
  @Autowired private FighterDetailsRepository fighterDetailsRepository;
  @Autowired private BoutRepository boutRepository;
  @Autowired private TapologyService tapologyService;
  @Autowired private HtmlFetchService htmlFetchService;

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
        "insert_job: sb-services-job-load job_type: CMD machine: sndb1 owner: sandp condition: s(sb-services-job-input-fw) max_run_alarm:1000 min_run_alarm:400 box_name:sb-services-job-box envvars:GREETING=\"hello world\"  envvars:GREETING2=\"hello world2\""
            + "insert_job: sb-services-job-input-fw job_type: FW machine: sndb2 owner: sandp date_conditions:1 start_times: 13\\:00  max_run_alarm:1000 min_run_alarm:400 box_name:sb-services-job-box envvars:GREETING=\"hello world\""
            + "insert_job: sb-services-job-box job_type: BOX machine: sndb2 owner: sandp date_conditions:1 start_times: \"12:00\" max_run_alarm:1000 min_run_alarm:400";

    final StopWatch sw = new StopWatch();
    sw.start();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/jil/print")
                .contentType(MediaType.TEXT_PLAIN)
                .content(body))
        .andExpect(MockMvcResultMatchers.status().isOk());
    sw.stop();
    System.out.println(sw.getTotalTimeSeconds());
  }

  @Test
  void shouldGenerateFirst10Primes() throws Exception {
    int n = 10;
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/generate-first-n-primes/{n}", n))
        .andExpect(MockMvcResultMatchers.status().isOk());

    List<Long> integers = List.of(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L, 31L);
    List<Prime> actual = primeRepository.findAll();
    List<Prime> expected =
        IntStream.range(0, n).mapToObj(i -> new Prime(i + 1L, i + 1L, integers.get(i))).toList();
    actual.sort(Comparator.comparing(Prime::getPosition));
    assertEquals(expected, actual);
  }

  @Test
  void shouldGiveMaxDelayOnRoute() throws Exception {
    String from = "LAX";
    String to = "LAS";

    var sw = new StopWatch();
    sw.start();
    MvcResult r =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/max-flight-delay/{from}/{to}", from, to))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    sw.stop();
    String content = r.getResponse().getContentAsString();
    Double actual = Double.parseDouble(content);
    assertEquals(35.0, actual);
    assertTrue(
        sw.getTotalTimeMillis() < 1000,
        "The parsing and response to this question should take less than 1000ms");
  }

  @Test
  void shouldGiveTotalLineCountInDataSourceFile() throws Exception {
    var sw = new StopWatch();
    sw.start();
    MvcResult r =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/flight-delay-data-line-count"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    sw.stop();
    String content = r.getResponse().getContentAsString();
    Long actual = Long.parseLong(content);
    assertEquals(10001L, actual);
    assertTrue(
        sw.getTotalTimeMillis() < 1000,
        "The parsing and response to this question should take less than 1000ms");
  }

  @Test
  void shouldResolveTemplate() throws Exception {
    var expectedFilePath = Path.of("src/test/resources/data/resolved-template-expected.txt");
    var actualRequestBodyPath = Path.of("src/test/resources/data/resolved-template-test.json");
    var testTemplateName = "resolved-template.ftl";
    var jsonBody = Files.readString(actualRequestBodyPath, StandardCharsets.UTF_8);
    MvcResult r =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/resolved-template/" + testTemplateName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    String actual = r.getResponse().getContentAsString();
    String expected = Files.readString(expectedFilePath, StandardCharsets.UTF_8);
    assertEquals(expected, actual);
  }

  @Test
  void shouldResolveTemplate2() throws Exception {
    var expectedFilePath = Path.of("src/test/resources/data/complex-template-expected.txt");
    var actualRequestBodyPath = Path.of("src/test/resources/data/complex-template-test.json");
    var testTemplateName = "complex-template.ftl";
    var jsonBody = Files.readString(actualRequestBodyPath, StandardCharsets.UTF_8);
    MvcResult r =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/resolved-template/" + testTemplateName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    String actual = r.getResponse().getContentAsString();
    String expected = Files.readString(expectedFilePath, StandardCharsets.UTF_8);
    assertEquals(expected, actual);
  }

  @Test
  void persistAllFighterDetails() throws Exception {
    // given
    final String FIGHTER_DETAILS_BASE_URL = "https://www.tapology.com/fightcenter/fighters/";

    var fighterListPageHtmlFileName = "fighter-list-grid-1.html";
    mockFetchHtml(fighterListPageHtmlFileName, fighterListPageHtmlFileName);
    mockFetchHtml(
        FIGHTER_DETAILS_BASE_URL.concat("314900-ryan-dobinson"), "314900-ryan-dobinson.html");
    mockFetchHtml(FIGHTER_DETAILS_BASE_URL.concat("377781-jack-bangs"), "377781-jack-bangs.html");
    mockFetchHtml(
        FIGHTER_DETAILS_BASE_URL.concat("444966-george-plevin"), "444966-george-plevin.html");
    mockFetchHtml(FIGHTER_DETAILS_BASE_URL.concat("467881-bilal-omari"), "467881-bilal-omari.html");
    List<Fighter> fighters =
        new ArrayList<>(tapologyService.listFighters(fighterListPageHtmlFileName));
    fighterRepository.saveAll(fighters);
    when(mockSystemTimeSource.currentTime()).thenReturn(testTime.toLocalDateTime());

    // when
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/persist-fighter-details"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    // then
    List<FighterDetails> actualFighterDetails =
        fighterDetailsRepository.findAll().stream()
            .sorted(Comparator.comparing(FighterDetails::getFighterId))
            .toList();
    List<Bout> actualBoutData =
        boutRepository.findAll().stream()
            .peek(Bout::unsetDetails)
            .sorted(Comparator.comparing(Bout::getBoutId).thenComparing(Bout::getFighterId))
            .toList();
    String expectedFighterDetails =
        TestUtils.getTestFileContent("data/mma/expected/fighter-details-1.txt");
    String expectedBoutData = TestUtils.getTestFileContent("data/mma/expected/bouts-1.txt");

    assertTrue(compareJsonAndList(expectedFighterDetails, actualFighterDetails));
    assertTrue(compareJsonAndList(expectedBoutData, actualBoutData));
  }

  void mockFetchHtml(String url, String fName) throws IOException {

    when(htmlFetchService.fetchHtml(url))
        .thenReturn(TestUtils.getTestFileContent("data/mma/pages/" + fName));
  }
}
