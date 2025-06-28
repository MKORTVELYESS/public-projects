package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.example.domain.shannon.Group;
import org.example.entity.HttpRequestLog;
import org.junit.jupiter.api.Test;

class HttpRequestLogTest {

  @Test
  void testEquals() {
    var log =
        new HttpRequestLog(
            1L,
            "GET",
            "/test",
            "?address=01",
            "Header",
            "Body",
            "0.0.0.0",
            OffsetDateTime.of(2025, 10, 1, 19, 54, 01, 0, ZoneOffset.UTC));
    var grp = new Group(0);
    assertFalse(log.equals(null));
    assertFalse(log.equals(grp));
  }
}
