package org.example.util;

public class MathUtils {
  public static long nChooseK(int n, int k) {
    if (n < 0 || k < 0 || n > k) return 0;
    if (n == 0 || n == k) return 1;

    long result = 1;
    for (int i = 1; i <= n; i++) {
      result = result * (k - i + 1) / i;
    }
    return result;
  }
}
