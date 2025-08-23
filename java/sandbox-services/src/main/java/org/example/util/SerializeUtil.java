package org.example.util;

import static org.example.util.CollectionConverters.arrayToList;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

public class SerializeUtil {

  public static final Function<Byte, String> byteToHexStringFormatter =
      (Byte b) -> String.format("%02X", b);
  public static final Function<Byte, String> byteToBinaryStringFormatter =
      (Byte b) -> String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
  public static final Function<String, Byte> hexStringToByteParser =
      (String s) -> (byte) Integer.parseInt(s, 16);
  public static final Function<String, Byte> binaryStringToByteParser =
      (String s) -> (byte) Integer.parseInt(s, 2);

  public static byte[] serializeObj(Object o) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(o);
    oos.flush();
    return baos.toByteArray();
  }

  public static Object deserializeObj(byte[] bytes) throws IOException, ClassNotFoundException {

    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
      return ois.readObject();
    }
  }

  public static List<String> byteArrayAsFormattedStringList(
      byte[] bytes, Function<Byte, String> byteToStringFormatter) {
    String[] formattedBytes = new String[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      formattedBytes[i] = byteToStringFormatter.apply(bytes[i]);
    }
    return arrayToList(formattedBytes);
  }

  public static <T> byte[] formattedStringListAsByteArray(
      List<String> list, Function<String, Byte> stringToByteFormatter) {
    byte[] bytes = new byte[list.size()];
    for (int i = 0; i < list.size(); i++) {
      bytes[i] = stringToByteFormatter.apply(list.get(i));
    }
    return bytes;
  }

  public static String getBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static byte[] fromBase64(String base64) {
    return Base64.getDecoder().decode(base64);
  }
}
