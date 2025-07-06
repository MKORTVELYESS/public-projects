package org.example.util;

import java.util.Arrays;

public class ArrayUtil {
  public static <A> A[] dropLast(A[] parts) {
    return Arrays.copyOf(parts, parts.length - 1);
  }
}
