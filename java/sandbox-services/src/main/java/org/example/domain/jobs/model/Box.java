package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public class Box extends Job {
  public Box(Map<JilAttributeKey, String> attributes) {
    this.attributes = Map.copyOf(attributes);
  }
}
