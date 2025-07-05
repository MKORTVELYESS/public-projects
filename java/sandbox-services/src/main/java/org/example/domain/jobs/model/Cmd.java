package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public record Cmd(Map<JilAttributeKey, String> attributes) implements Job {

  public Cmd(Map<JilAttributeKey, String> attributes) {
    this.attributes = Map.copyOf(attributes);
  }

  @Override
  public Map<JilAttributeKey, String> attributes() {
    return Map.copyOf(attributes);
  }
}
