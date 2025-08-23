package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class SerializeUtilTest {
  @Test
  void testSecretBytesToString() throws IOException, ClassNotFoundException {
    String[] secret = {
      "10101100",
      "11101101",
      "00000000",
      "00000101",
      "01110100",
      "00000000",
      "00000110",
      "01010011",
      "01100101",
      "01100011",
      "01110010",
      "01100101",
      "01110100"
    };
    String expected = "Secret";
    byte[] bytes = new byte[secret.length];
    for (int i = 0; i < secret.length; i++) {
      bytes[i] = (byte) Integer.parseInt(secret[i], 2);
    }
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = new ObjectInputStream(bais);
    String actual = (String) ois.readObject();
    assertEquals(expected, actual);
  }

  @Test
  void testSecretBytesToUTF() throws IOException {
    String[] secret = {
      "10101100",
      "11101101",
      "00000000",
      "00000101",
      "01110111",
      "00001000",
      "00000000",
      "00000110",
      "01010011",
      "01100101",
      "01100011",
      "01110010",
      "01100101",
      "01110100"
    };
    String expected = "Secret";
    byte[] bytes = new byte[secret.length];
    for (int i = 0; i < secret.length; i++) {
      bytes[i] = (byte) Integer.parseInt(secret[i], 2);
    }
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = new ObjectInputStream(bais);
    String actual = ois.readUTF();
    assertEquals(expected, actual);
  }

  @Test
  void testDecodeBinarySecrets() throws IOException {
    String strSecret1 = "42";
    List<String> byteSecret1 =
        SerializeUtil.byteArrayAsFormattedStringList(
            SerializeUtil.serializeObj(strSecret1), SerializeUtil.byteToBinaryStringFormatter);

    String strSecret2 = "";
    List<String> byteSecret2 =
        SerializeUtil.byteArrayAsFormattedStringList(
            SerializeUtil.serializeObj(strSecret2), SerializeUtil.byteToBinaryStringFormatter);

    String strSecret3 = "Dominik";
    List<String> byteSecret3 =
        SerializeUtil.byteArrayAsFormattedStringList(
            SerializeUtil.serializeObj(strSecret3), SerializeUtil.byteToBinaryStringFormatter);

    String strSecret4 = "Dani";
    List<String> byteSecret4 =
        SerializeUtil.byteArrayAsFormattedStringList(
            SerializeUtil.serializeObj(strSecret4), SerializeUtil.byteToBinaryStringFormatter);

    String strSecret5 = "Eli Lilly Company";
    List<String> byteSecret5 =
        SerializeUtil.byteArrayAsFormattedStringList(
            SerializeUtil.serializeObj(strSecret5), SerializeUtil.byteToBinaryStringFormatter);

    String strSecret6 = "Congrats! You decoded me";
    List<String> byteSecret6 =
        SerializeUtil.byteArrayAsFormattedStringList(
            SerializeUtil.serializeObj(strSecret6), SerializeUtil.byteToBinaryStringFormatter);

    System.out.printf("%s: %s %n", strSecret1, byteSecret1);
    System.out.printf("%s: %s %n", strSecret2, byteSecret2);
    System.out.printf("%s: %s %n", strSecret3, byteSecret3);
    System.out.printf("%s: %s %n", strSecret4, byteSecret4);
    System.out.printf("%s: %s %n", strSecret5, byteSecret5);
    System.out.printf("%s: %s %n", strSecret6, byteSecret6);
  }
}
