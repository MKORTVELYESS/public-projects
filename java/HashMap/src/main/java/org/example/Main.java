package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Main {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    public static void main(String[] args) {

        AtomicInteger index = new AtomicInteger(0);
        Map<EventLite, Event> eventMap = new HashMap<>(50_000_000, 0.75f);
        Path path = Paths.get("data/2019-Nov.csv");

        Instant start = Instant.now();
        try (
                Stream<String> lines = Files.lines(path).skip(1).limit(30_000_000);
        ) {

            lines
                    .forEach((l) -> {
                        var i = index.getAndIncrement();
                        try {
                            var e = new Event(l);
                            eventMap.put(new EventLite(e.getEventTime(), e.getEventType(), e.getUserId()), e);

                        } catch (Throwable ignored) {
                        }
                        if (i % 10000000 == 0) {
                            Instant now = Instant.now();
                            System.out.println("Parser currently at line: " + i + " Elapsed since start: " + Duration.between(start, now).toMillis() + " ms");
                        }
                    });

            EventLite first = new EventLite(ZonedDateTime.parse("2019-11-01 00:00:24 UTC", formatter), EventType.VIEW, 521368162); //2019-11-01 00:00:24 UTC,view,10301494,2053013553115300101,,welly,5.77,521368162,375da15e-023b-47a5-88f9-828c28637359
            EventLite second = new EventLite(ZonedDateTime.parse("2019-11-01 12:44:10 UTC", formatter), EventType.CART, 518632383); //2019-11-01 12:44:10 UTC,cart,1005164,2053013555631882655,electronics.smartphone,xiaomi,281.32,518632383,92e8387e-13cf-4de8-9116-5911956a67f4
            EventLite third = new EventLite(ZonedDateTime.parse("2019-11-04 19:42:15 UTC", formatter), EventType.PURCHASE, 546716351); //2019-11-04 19:42:15 UTC,purchase,7100182,2053013555464110485,furniture.bedroom.bed,,136.40,546716351,8412869e-73c1-40de-82ec-d061577dc212
            var keys = List.of(first, second, third);

            //map of 30MM
            var duration1 = logLookupPerformance(eventMap, keys);

            //map of 3
            var duration2 = logLookupPerformance(Map.of(
                    first, new Event("2019-11-01 00:00:24 UTC,view,10301494,2053013553115300101,,welly,5.77,521368162,375da15e-023b-47a5-88f9-828c28637359"),
                    second, new Event("2019-11-01 12:44:10 UTC,cart,1005164,2053013555631882655,electronics.smartphone,xiaomi,281.32,518632383,92e8387e-13cf-4de8-9116-5911956a67f4"),
                    third, new Event("2019-11-04 19:42:15 UTC,purchase,7100182,2053013555464110485,furniture.bedroom.bed,,136.40,546716351,8412869e-73c1-40de-82ec-d061577dc212")), keys);

            System.out.printf("3 lookups from large map took %d ms, while 3 lookups from small map took %d ms. It took %d ms more time to look up 3 items from a map of 30MM than a map of 3", duration1, duration2, duration1 - duration2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long logLookupPerformance(Map<EventLite, Event> eventMap, List<EventLite> keys) {
        System.out.printf("Start test constant time lookup from map of %d%n", eventMap.size());
        Instant lookupStart = Instant.now();
        keys.forEach((key) ->
                System.out.println(eventMap.get(key)));
        var duration = Duration.between(lookupStart, Instant.now()).toMillis();
        System.out.println("Since start: " + duration + " ms");
        return duration;
    }
}