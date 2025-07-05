package org.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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

  public static <A> Set<A> findDuplicates(List<A> input) {
    return input.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  public static <A> List<A> concatLists(List<A> first, List<A> second) {
    List<A> result = new ArrayList<>(first.size() + second.size());
    result.addAll(first);
    result.addAll(second);
    return result;
  }
}
