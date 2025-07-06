package org.example.util;

public class StringUtil {
  public static final String WORD_SPLITTER = " ";

  public static String lastWordOf(String line) {
    var words = line.strip().split(WORD_SPLITTER);
    var lastIndex = words.length - 1;
    return words[lastIndex];
  }
}
