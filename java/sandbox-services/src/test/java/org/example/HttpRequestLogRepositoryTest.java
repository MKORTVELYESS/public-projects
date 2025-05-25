package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HttpRequestLogRepositoryTest {
  @Autowired private HttpRequestLogRepository logRepository;

  @Test
  void saveAndLoad() {
    logRepository.save(
        new HttpRequestLog(
            "GET", "/health", null, "cookies=test", "{\"test\":\"test\"}", "0:0:0:0.0"));
    assertEquals(1, logRepository.count());
  }
}
