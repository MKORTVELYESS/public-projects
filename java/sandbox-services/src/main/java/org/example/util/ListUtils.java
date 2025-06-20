package org.example.util;

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
}
