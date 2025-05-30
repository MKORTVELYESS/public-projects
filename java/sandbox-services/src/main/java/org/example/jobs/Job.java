package org.example.jobs;

import java.util.Map;

public class Job {

  private final Map<String, String> attributes;

  public Map<String, String> getAttributes() {
    return Map.copyOf(attributes);
  }

  protected Job(Map<String, String> attributes) {
    this.attributes = attributes;
  }
}
