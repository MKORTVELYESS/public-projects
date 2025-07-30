package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public class Fw extends Job {

  public Fw(Map<JilAttributeKey, String> attributes) {
    this.attributes = Map.copyOf(attributes);
  }
}
