package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListUtilsTest {

  @Test
  void newImmutableListFromHeadAndEmpty() {
    var head = "first";
    var rest = List.of();
    var actual = ListUtils.newImmutableListFrom(head, rest);
    var expected = List.of(head);
    assertEquals(expected, actual);
  }

  @Test
  void newImmutableListFromHeadAndNonEmpty() {
    var head = "first";
    var rest = List.of("second", "third");
    var actual = ListUtils.newImmutableListFrom(head, rest);
    var expected = List.of(head, "second", "third");
    assertEquals(expected, actual);
  }
}
