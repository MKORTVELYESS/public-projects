package org.example.domain.jobs;

import java.util.Map;

public class Job {

  private final Map<JilAttributeKey, String> attributes;

  public Map<JilAttributeKey, String> getAttributes() {
    return Map.copyOf(attributes);
  }

  protected Job(Map<JilAttributeKey, String> attributes) {
    this.attributes = attributes;
  }
}
