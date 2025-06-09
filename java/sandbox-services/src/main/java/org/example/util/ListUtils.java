package org.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ListUtils {
  public record SplitList<A>(List<A> first, List<A> rest) {

    public SplitList {
      first = List.copyOf(first);
      rest = List.copyOf(rest);
    }
  }

  public static <A> SplitList<A> splitAfter(int idx, List<A> list) {
    var frst = list.subList(0, idx);
    var rest = list.subList(idx, list.size());
    return new SplitList<A>(frst, rest);
  }

  public static <A> List<A> newImmutableListFrom(A head, List<A> list) {
    return Stream.concat(Stream.of(head), list.stream()).toList();
  }

  public static <T> List<List<T>> generateCombinations(List<T> input, int n) {
    List<List<T>> result = new ArrayList<>();
    generate(input, n, 0, new ArrayList<>(), result);
    return result;
  }

  private static <T> void generate(
      List<T> input, int n, int start, List<T> current, List<List<T>> result) {
    if (current.size() == n) {
      result.add(new ArrayList<>(current));
      return;
    }
    for (int i = start; i < input.size(); i++) {
      current.add(input.get(i));
      generate(input, n, i + 1, current, result);
      current.remove(current.size() - 1);
    }
  }
}
