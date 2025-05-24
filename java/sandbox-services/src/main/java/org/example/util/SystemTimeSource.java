package org.example.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SystemTimeSource {

    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }

}
