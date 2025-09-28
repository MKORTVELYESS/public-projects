package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

class BlackBoxServiceTest {

  @Test
  void sleep() {
    var sw = new StopWatch();
    sw.start();
    int sleepTime = 5000;
    BlackBoxService.sleep(sleepTime);
    assertTrue(sw.getTotalTimeMillis() < sleepTime);
  }
}
