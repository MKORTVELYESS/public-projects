package org.example.util;

import static org.example.util.SerializeUtil.*;

import java.io.*;
import java.util.*;
import java.util.function.IntFunction;

public class CollectionConverters {

  public static <T> List<T> arrayToList(T[] array) {
    return Arrays.asList(array);
  }

  public static <T> List<T> arrayToUnmodifyableList(T[] array) {
    return List.of(array);
  }

  public static <T> List<T> asUnmodifiable(List<T> list) {
    return List.copyOf(list);
  }

  public static <T> T[] listToArray(List<T> list, IntFunction<T[]> generator) {
    return list.toArray(generator);
  }

  public static <T> LinkedList<T> arrayListToLinkedList(ArrayList<T> arrayList) {
    return new LinkedList<>(arrayList);
  }

  public static <T> ArrayList<T> linkedListToArrayList(LinkedList<T> arrayList) {
    return new ArrayList<>(arrayList);
  }

  public static <T> List<String> listSerializeToHexBytes(List<T> list) throws IOException {
    return byteArrayAsFormattedStringList(serializeObj(list), byteToHexStringFormatter);
  }

  public static <T> List<String> listSerializeToBinary(List<T> list) throws IOException {
    return byteArrayAsFormattedStringList(serializeObj(list), byteToBinaryStringFormatter);
  }

  public static <T> String listSerializeToBase64(List<T> list) throws IOException {
    return getBase64(serializeObj(list));
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> listDeserializeFromHexBytes(List<String> list)
      throws IOException, ClassNotFoundException {
    return (List<T>) deserializeObj(formattedStringListAsByteArray(list, hexStringToByteParser));
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> listDeserializeFromBinary(List<String> list)
      throws IOException, ClassNotFoundException {
    return (List<T>) deserializeObj(formattedStringListAsByteArray(list, binaryStringToByteParser));
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> listDeserializeFromBase64(String base64)
      throws IOException, ClassNotFoundException {
    return (List<T>) deserializeObj(fromBase64(base64));
  }
}
