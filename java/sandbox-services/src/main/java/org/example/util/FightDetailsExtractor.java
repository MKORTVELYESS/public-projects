package org.example.util;

import java.util.*;
import java.util.stream.Collectors;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import org.example.entity.Bout;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FightDetailsExtractor {
    private static final Logger logger = LoggerFactory.getLogger(FightDetailsExtractor.class);

    public static List<Bout> extract(Document doc, String fighterId) {
        return Try.of(() -> doc.selectFirst("section.fighterFightResults"))
                .map(section ->
                        {
                            assert section != null;
                            return section.selectStream("div[data-fighter-bout-target=bout]")
                                    .map(b -> {
                                        Bout bout = new Bout();

                                        bout.setFighterId(fighterId);

                                        String boutId = b.id();

                                        bout.setId(fighterId + "-" + boutId);

                                        bout.setStatus(tryGet("status", boutId,
                                                () -> b.attr("data-status")));

                                        bout.setSport(tryGet("sport", boutId,
                                                () -> b.attr("data-sport")));

                                        bout.setDivision(tryGet("division", boutId,
                                                () -> b.attr("data-division")));

                                        bout.setBoutId(tryGet("boutId", boutId,
                                                () -> b.attr("data-bout-id")));

                                        bout.setMethod(tryGet("method", boutId,
                                                () -> b.selectFirst("div.\\-rotate-90").text()));

                                        bout.setOpponentId(tryGet("opponentId", boutId,
                                                () -> lastPathSegment(
                                                        b.selectFirst("a[title$=Fighter Page]")
                                                )));

                                        // Bout page
                                        Try.run(() -> {
                                                    Element el = b.selectFirst("a[title=Bout Page]");
                                                    bout.setBoutPageId(lastPathSegment(el));
                                                    bout.setFightShortDescription(el.text());
                                                })
                                                .onFailure(e ->
                                                        logger.error("Failed extracting bout page data for bout {}", boutId));

                                        // Event page
                                        Try.run(() -> {
                                                    Element el = b.selectFirst("a[title=Event Page]");
                                                    bout.setEventPageId(lastPathSegment(el));
                                                    bout.setEventName(el.text());
                                                })
                                                .onFailure(e ->
                                                        logger.error("Failed extracting event page data for bout {}", boutId));

                                        bout.setFighterRecordBeforeFight(
                                                tryGet("fighterRecordBeforeFight", boutId,
                                                        () -> b.selectFirst("span[title=Fighter Record Before Fight]").text())
                                        );

                                        bout.setOpponentRecordBeforeFight(
                                                tryGet("opponentRecordBeforeFight", boutId,
                                                        () -> b.selectFirst("span[title=Opponent Record Before Fight]").text())
                                        );

                                        // Fight date
                                        Try.run(() -> {
                                                    Element yearEl = b.select("span.font-bold").stream()
                                                            .filter(e -> e.text().matches("\\d{4}"))
                                                            .toList()
                                                            .getFirst();

                                                    bout.setFightYear(yearEl.text());
                                                    bout.setFightDay(yearEl.nextElementSibling().text());
                                                })
                                                .onFailure(e ->
                                                        logger.error("Failed extracting fight date for bout {}", boutId));

                                        // Details
                                        bout.setDetails(
                                                tryGet("details", boutId, () -> {
                                                    Element detailRows =
                                                            doc.selectFirst("div#detail-rows-" + bout.getBoutId());

                                                    return detailRows.select("> div").stream()
                                                            .map(row -> row.select("span"))
                                                            .filter(spans -> spans.size() >= 2)
                                                            .collect(Collectors.toMap(
                                                                    spans -> spans.getFirst().text().trim(),
                                                                    spans -> spans.get(1).text().trim(),
                                                                    (a, b1) -> a,
                                                                    LinkedHashMap::new
                                                            ));
                                                })
                                        );

                                        return bout;
                                    })
                                    .toList();
                        }
                )
                .onFailure(e -> logger.error("Failed extracting fighterFightResults section"))
                .getOrElse(List.of());
    }


    private static <T> T tryGet(String field, String boutId, CheckedFunction0<T> supplier) {
        return Try.of(supplier)
                .onFailure(e -> logger.error("Failed extracting {} for bout {}", field, boutId))
                .getOrNull();
    }

    private static String lastPathSegment(Element element) {
        return Arrays.stream(element.attr("href").split("/"))
                .toList()
                .getLast();
    }

}
