package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public class Cmd extends Job {
  public Cmd(Map<JilAttributeKey, String> attributes) {
    this.attributes = Map.copyOf(attributes);
  }
}
