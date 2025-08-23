package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Test;

class CollectionConvertersTest {
  @Test
  public void testArrayToList() {
    Integer[] arr = {1, 2, 3, 4};
    List<Integer> converted = CollectionConverters.arrayToList(arr);

    List<Integer> convertedUnmodifiable = CollectionConverters.arrayToUnmodifyableList(arr);
    Integer[] reconverted = CollectionConverters.listToArray(converted, Integer[]::new);
    List<Integer> unmodifiableList = CollectionConverters.asUnmodifiable(converted);
    LinkedList<Integer> linkedList =
        CollectionConverters.arrayListToLinkedList(new ArrayList<>(converted));
    ArrayList<Integer> arrayList = CollectionConverters.linkedListToArrayList(linkedList);

    List<Integer> expected = List.of(1, 2, 3, 4);

    assertArrayEquals(arr, reconverted);
    assertEquals(expected, converted);
    assertEquals(expected, convertedUnmodifiable);
    assertEquals(converted, convertedUnmodifiable);
    assertEquals(converted, unmodifiableList);
    assertEquals(converted, linkedList);
    assertEquals(arrayList, linkedList);

    converted.set(0, 2);
    assertNotEquals(converted, unmodifiableList);
    assertThrows(UnsupportedOperationException.class, () -> unmodifiableList.set(1, 2));
  }

  @Test
  public void testListSerializeForHex() throws IOException, ClassNotFoundException {
    List<Integer> expected = List.of(1, 2, 3, 4);

    List<String> serialized = CollectionConverters.listSerializeToHexBytes(expected);
    List<Integer> actual = CollectionConverters.listDeserializeFromHexBytes(serialized);

    assertEquals(expected, actual);
  }

  @Test
  public void testListSerializeForBinary() throws IOException, ClassNotFoundException {
    List<Integer> expected = List.of(1, 2, 3, 4);

    List<String> serialized = CollectionConverters.listSerializeToBinary(expected);
    List<Integer> actual = CollectionConverters.listDeserializeFromBinary(serialized);

    assertEquals(expected, actual);
  }

  @Test
  public void testListSerializeForBase64() throws IOException, ClassNotFoundException {
    List<Integer> expected = List.of(1, 2, 3, 4);

    String serialized = CollectionConverters.listSerializeToBase64(expected);
    List<Integer> actual = CollectionConverters.listDeserializeFromBase64(serialized);

    assertEquals(expected, actual);
  }
}
