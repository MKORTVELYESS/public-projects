package org.example.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class SystemTimeSource {

    public OffsetDateTime now() {
        return OffsetDateTime.now(ZoneId.of("UTC"));
    }

}
