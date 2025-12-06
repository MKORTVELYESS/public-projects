package org.example.util;

import java.util.*;
import java.util.stream.Stream;

public final class DataUtil {

  public static int countValues(Object data) {
    return count(data);
  }

  private static int count(Object node) {
    if (node == null) return 0;

    return switch (node) {
      case Map<?, ?> map -> sumElements(map.values().stream());
      case Collection<?> coll -> sumElements(coll.stream());
      case Object[] arr -> sumElements(Arrays.stream(arr));
      default -> 1;
    };
  }

  private static int sumElements(Stream<?> stream) {
    return stream.mapToInt(DataUtil::count).sum();
  }
}
