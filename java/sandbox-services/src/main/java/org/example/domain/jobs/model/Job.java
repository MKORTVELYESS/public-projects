package org.example.domain.jobs.model;

import java.util.Map;
import org.example.domain.jobs.JilAttributeKey;

public interface Job {

  Map<JilAttributeKey, String> attributes();
}
