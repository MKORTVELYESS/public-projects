package org.example.domain.jobs;

import static org.example.domain.jobs.JilValidator.*;
import static org.example.util.ArrayUtil.dropLast;

import java.lang.constant.Constable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.example.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JilReader {

  private static final Logger logger = LoggerFactory.getLogger(JilReader.class);

  /** Match every ':' NOT preceded by '\' followed by even number of '"'-s */
  private static final String JIL_SPLITTER = "(?<!\\\\):(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

  private static final String END_OF_LINE_MATCHER = "$";
  private static final String DOTALL_ENABLE = "(?s)"; // dot should match newline char as well
  private static final String ZERO_OR_MORE_WHITESPACE_MATCHER = "\\s*";
  public static final String JIL_VALUE = "(?<value>.*?)";

  public static String[] getParts(String jil) {
    return jil.split(JIL_SPLITTER);
  }

  /**
   * Gets the last word of each 'part' Splitting of the jil is done at each ':' so the word before
   * the ':' which is the last word of the
   */
  public static Stream<String> extractAttributeKeys(String[] parts) {
    return Arrays.stream(dropLast(parts)).map(StringUtil::lastWordOf);
  }

  public static Stream<Matcher> generateValueMatchersForKeysInJil(
      Stream<JilAttributeKey> keys, String jil) {
    List<Matcher> matchers = new ArrayList<>();
    var list = keys.toList();
    var iterator = list.iterator();

    Constable thisJilKeyword;
    Constable nextJilKeyword = iterator.next();
    for (int i = 0; i < list.size(); i++) {
      thisJilKeyword = nextJilKeyword;
      nextJilKeyword = nextOrEnd(iterator);
      String regex =
          DOTALL_ENABLE
              + thisJilKeyword
              + ZERO_OR_MORE_WHITESPACE_MATCHER
              + ":"
              + JIL_VALUE
              + nextJilKeyword;
      matchers.add(Pattern.compile(regex).matcher(jil));
    }

    return matchers.stream();
  }

  private static Constable nextOrEnd(Iterator<JilAttributeKey> iterator) {
    return iterator.hasNext() ? iterator.next() : END_OF_LINE_MATCHER;
  }

  public static Stream<String> extractValues(String jil, Stream<Matcher> matchers) {
    return matchers.map(
        matcher -> {
          if (matcher.find()) {
            return matcher.group("value").strip();
          } else {
            logger.error(jil);
            throw new IllegalArgumentException(
                "Please inspect jil as we are not able to match with regex");
          }
        });
  }

  public static Map<JilAttributeKey, String> jilToKeyValueMap(String jil) {
    final var parts = getParts(jil);
    final var keys = getJilAttributeKeys(parts);
    final var values = getJilAttributeValues(jil, keys);
    runValidation(keys, values);

    logger.info("Start building jil attributes as key value map");

    return IntStream.range(0, keys.size())
        .boxed()
        .collect(
            Collectors.toMap(keys::get, values::get, (first, second) -> first + ", " + second));
  }

  private static List<String> getJilAttributeValues(String jil, List<JilAttributeKey> keyList) {
    final var valueCapturers = generateValueMatchersForKeysInJil(keyList.stream(), jil);
    final var values = extractValues(jil, valueCapturers);
    return values.toList();
  }

  private static List<JilAttributeKey> getJilAttributeKeys(String[] parts) {
    final var keys = extractAttributeKeys(parts);
    final var parsedKeys = keys.map(JilAttributeKey::valueOf);
    return parsedKeys.toList();
  }
}
