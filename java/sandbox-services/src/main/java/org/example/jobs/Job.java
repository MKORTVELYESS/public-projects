package org.example.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job {

  private static final Logger logger = LoggerFactory.getLogger(Job.class);

  private final String jil;
  private String[] parts;
  private List<String> attributeKeys;
  private List<String> attributeValues;

  public String[] getParts() {
    return parts;
  }

  public void setParts(String[] parts) {
    this.parts = parts;
  }

  private String[] extractPartsFromJil() {
    return jil.split(":");
  }

  public List<String> getAttributeKeys() {
    return attributeKeys;
  }

  public void setAttributeKeys(List<String> attributeKeys) {
    this.attributeKeys = attributeKeys;
  }

  private List<String> extractAttributeKeysFromJil() {
    return Arrays.stream(Arrays.copyOf(getParts(), getParts().length - 1))
        .map(
            line -> {
              String[] words = line.strip().split(" ");
              return words[words.length - 1];
            })
        .toList();
  }

  public List<String> getAttributeValues() {
    return attributeValues;
  }

  public void setAttributeValues(List<String> attributeValues) {
    this.attributeValues = attributeValues;
  }

  private List<String> extractAttributeValuesFromJil() {
    return getAttributeMatchers().stream()
        .map(
            matcher -> {
              if (matcher.find()) {
                return matcher.group("value").strip();
              } else {
                logger.error(jil);
                throw new RuntimeException(
                    "Please inspect jil as we are not able to match with regex");
              }
            })
        .toList();
  }

  public List<Matcher> getAttributeMatchers() {
    return attributeMatchers;
  }

  public void setAttributeMatchers(List<Matcher> attributeMatchers) {
    this.attributeMatchers = attributeMatchers;
  }

  private List<Matcher> generateAttributeMatchersFromKeysAndJil() {
    List<Matcher> matchers = new ArrayList<>();

    int size = getAttributeKeys().size();
    for (int i = 0; i < size; i++) {
      int currentIndex = i;
      int nextIndex = currentIndex + 1;

      String current = getAttributeKeys().get(currentIndex);

      boolean isNextWithinBounds = nextIndex < size;
      String next = isNextWithinBounds ? getAttributeKeys().get(nextIndex) : "$";

      String regex = "(?s)" + current + "\\s*" + ":" + "(?<value>.*?)" + next;
      matchers.add(Pattern.compile(regex).matcher(jil));
    }

    return matchers;
  }

  private List<Matcher> attributeMatchers;

  public String getJil() {
    return jil;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  private Map<String, String> generateAttributesFromKeysAndValues() {

    if (getAttributeKeys().size() == getAttributeValues().size()) {
      logger.info(
          "Jil keys and values are equal in size, can continue matching attributes with their values");
    } else {
      throw new RuntimeException(
          "Please inspect jil as we found a different number of attribute keys compared to attribute values");
    }

    return IntStream.range(0, getAttributeKeys().size())
        .boxed()
        .collect(Collectors.toMap(getAttributeKeys()::get, getAttributeValues()::get));
  }

  private Map<String, String> attributes;

  public Job(String jil) {
    this.jil = jil;
    setParts(extractPartsFromJil());
    setAttributeKeys(extractAttributeKeysFromJil());
    setAttributeMatchers(generateAttributeMatchersFromKeysAndJil());
    setAttributeValues(extractAttributeValuesFromJil());
    setAttributes(generateAttributesFromKeysAndValues());
  }
}
