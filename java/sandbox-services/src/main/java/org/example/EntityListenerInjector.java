package org.example;

import org.example.util.SystemTimeSource;
import org.springframework.stereotype.Component;

@Component
public class EntityListenerInjector {

    public EntityListenerInjector(SystemTimeSource timeSource) {
        LogEntityListener.setTimeSource(timeSource);
    }

}
