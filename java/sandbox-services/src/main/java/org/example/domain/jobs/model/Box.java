package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public record Box(Map<JilAttributeKey, String> attributes) implements Job {

  public Box(Map<JilAttributeKey, String> attributes) {
    this.attributes = Map.copyOf(attributes);
  }

  @Override
  public Map<JilAttributeKey, String> attributes() {
    return Map.copyOf(attributes);
  }
}
