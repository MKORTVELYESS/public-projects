package org.example.component;

import org.example.util.LogEntityListener;
import org.example.util.SystemTimeSource;
import org.springframework.stereotype.Component;

@Component
public class EntityListenerInjector {

  public EntityListenerInjector(SystemTimeSource timeSource) {
    LogEntityListener.setTimeSource(timeSource);
  }
}
