package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HttpRequestLogRepositoryTest {
  OffsetDateTime testTime = OffsetDateTime.of(2025, 5, 25, 19, 14, 15, 0, ZoneOffset.UTC);

  @Autowired private HttpRequestLogRepository logRepository;

  @Test
  void saveAndLoad() {
    logRepository.save(
        new HttpRequestLog(
            "GET", "/health", null, "cookies=test", "{\"test\":\"test\"}", "0:0:0:0.0"));
    assertEquals(1, logRepository.count());
  }

  @Test
  void testEquals() {
    var a =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);
    var b =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);
    var c =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime.plusDays(1));

    var d =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.1",
            testTime);

    var e =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"not-the-same\"}",
            "0:0:0:0.0",
            testTime);

    var f =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=not-the-same",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);

    var g =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello",
            "?date=2024-12-17",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);

    var h =
        new HttpRequestLog(
            1L,
            "GET",
            "/api/hello-bello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);

    var j =
        new HttpRequestLog(
            1L,
            "POST",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);

    var k =
        new HttpRequestLog(
            2L,
            "GET",
            "/api/hello",
            "?date=2024-12-16",
            "cookies=test",
            "{\"test\":\"test\"}",
            "0:0:0:0.0",
            testTime);

    assertEquals(a, b);
    assertNotEquals(a, c);
    assertNotEquals(a, d);
    assertNotEquals(a, e);
    assertNotEquals(a, f);
    assertNotEquals(a, g);
    assertNotEquals(a, h);
    assertNotEquals(a, j);
    assertNotEquals(a, k);
  }

  @Test
  void testGetters() {
    var expectedMethod = "GET";
    var expectedPath = "/api/hello";
    var expectedQueryParams = "?date=2024-12-16";
    var expectedHeaders = "cookies=test";
    var expectedBody = "{\"test\":\"test\"}";
    var expectedRemoteIp = "";
    var expectedId = 1L;
    var expectedTimestamp = testTime;
    var actual =
        new HttpRequestLog(
            expectedId,
            expectedMethod,
            expectedPath,
            expectedQueryParams,
            expectedHeaders,
            expectedBody,
            expectedRemoteIp,
            expectedTimestamp);
    assertEquals(expectedId, actual.getId());
    assertEquals(expectedMethod, actual.getMethod());
    assertEquals(expectedPath, actual.getPath());
    assertEquals(expectedQueryParams, actual.getQueryParams());
    assertEquals(expectedHeaders, actual.getHeaders());
    assertEquals(expectedBody, actual.getBody());
    assertEquals(expectedRemoteIp, actual.getRemoteIp());
    assertEquals(expectedTimestamp, actual.getTimestamp());
  }

  @Test
  void testHashCode() {
    var expectedMethod = "GET";
    var expectedPath = "/api/hello";
    var expectedQueryParams = "?date=2024-12-16";
    var expectedHeaders = "cookies=test";
    var expectedBody = "{\"test\":\"test\"}";
    var expectedRemoteIp = "";
    var expectedId = 1L;
    var expectedTimestamp = testTime;
    var actual =
        new HttpRequestLog(
            expectedId,
            expectedMethod,
            expectedPath,
            expectedQueryParams,
            expectedHeaders,
            expectedBody,
            expectedRemoteIp,
            expectedTimestamp);

    var expected = -843173697;
    assertEquals(expected, actual.hashCode());
  }

  @Test
  void testToString() {
    var expectedMethod = "GET";
    var expectedPath = "/api/hello";
    var expectedQueryParams = "?date=2024-12-16";
    var expectedHeaders = "cookies=test";
    var expectedBody = "{\"test\":\"test\"}";
    var expectedRemoteIp = "";
    var expectedId = 1L;
    var expectedTimestamp = testTime;
    var actual =
        new HttpRequestLog(
            expectedId,
            expectedMethod,
            expectedPath,
            expectedQueryParams,
            expectedHeaders,
            expectedBody,
            expectedRemoteIp,
            expectedTimestamp);
    var expected =
        "HttpRequestLog{id=1, method='GET', path='/api/hello', queryParams='?date=2024-12-16', headers='cookies=test', body='{\"test\":\"test\"}', remoteIp='', timestamp=2025-05-25T19:14:15Z}";
    assertEquals(expected, actual.toString());
  }
}
