package org.example;

import java.time.ZonedDateTime;

public record EventLite(ZonedDateTime eventTime, EventType eventType, Integer userId) {

}
