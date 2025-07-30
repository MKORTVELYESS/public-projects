package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public abstract class Job {

  Map<JilAttributeKey, String> attributes;

  public Map<JilAttributeKey, String> attributes() {
    return Map.copyOf(attributes);
  }

  public String toString() {
    var header = "/*--------------------------*/";

    return "";
  }
}
