package org.example.domain.jobs;

public class JilAttribute {
  private final String key;
  private final String value;

  public JilAttribute(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
