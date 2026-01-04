package org.example.util;

import com.google.common.util.concurrent.UncheckedTimeoutException;
import java.util.concurrent.ThreadLocalRandom;

public class ThrottleUtil {

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Retry interrupted", e);
    }
  }

  public static void throttle(long msBase, int msExtra) {
    try {
      Thread.sleep(msBase + ThreadLocalRandom.current().nextInt(msExtra));
    } catch (InterruptedException e) {
      throw new UncheckedTimeoutException(e);
    }
  }
}
