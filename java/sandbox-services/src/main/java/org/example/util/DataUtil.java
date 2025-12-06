package org.example.util;

import java.util.*;
import java.util.stream.Stream;

public final class DataUtil {

  public static int countAllKeys(Object data) {
    return count(data, new IdentityHashMap<>());
  }

  private static int count(Object node, IdentityHashMap<Object, Boolean> visited) {

    if (node == null || visited.containsKey(node)) return 0;

    visited.put(node, true);

    return switch (node) {
      case Map<?, ?> map -> map.size() + sumElements(map.values().stream(), visited);
      case Collection<?> coll -> sumElements(coll.stream(), visited);
      case Object[] arr -> sumElements(Arrays.stream(arr), visited);
      default -> 0;
    };
  }

  private static int sumElements(Stream<?> stream, IdentityHashMap<Object, Boolean> visited) {
    return stream.mapToInt(e -> count(e, visited)).sum();
  }
}
