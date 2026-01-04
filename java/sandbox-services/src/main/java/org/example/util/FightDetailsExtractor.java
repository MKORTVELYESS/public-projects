package org.example.util;

import java.util.*;
import java.util.stream.Collectors;
import org.example.entity.Bout;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FightDetailsExtractor {
  public static List<Bout> extract(Document doc) {
    var fighterFightResults = Optional.ofNullable(doc.selectFirst("section.fighterFightResults"));
    return fighterFightResults
        .get()
        .selectStream("div[data-fighter-bout-target=bout]")
        .map(
            (b) -> {
              Bout bout = new Bout();
              bout.setId(b.id());
              String status = b.attr("data-status");
              bout.setStatus(status);
              String sport = b.attr("data-sport");
              bout.setSport(sport);
              String division = b.attr("data-division");
              bout.setDivision(division);
              String boutId = b.attr("data-bout-id");
              bout.setBoutId(boutId);
              String methodCode =
                  Optional.ofNullable(b.selectFirst("div.\\-rotate-90"))
                      .map(Element::text)
                      .orElse(null);
              bout.setMethod(methodCode);
              String opponentId =
                  Arrays.stream(
                          Objects.requireNonNull(b.selectFirst("a[title$=Fighter Page]"))
                              .attr("href")
                              .split("/"))
                      .toList()
                      .getLast();
              bout.setOpponentId(opponentId);

              Element boutPageElement = b.selectFirst("a[title=Bout Page]");
              String boutPageId =
                  Arrays.stream(Objects.requireNonNull(boutPageElement).attr("href").split("/"))
                      .toList()
                      .getLast();
              bout.setBoutPageId(boutPageId);
              String fightShortDescription = boutPageElement.text();
              bout.setFightShortDescription(fightShortDescription);

              Element eventPageElem = b.selectFirst("a[title=Event Page]");
              String eventPage =
                  Arrays.stream(Objects.requireNonNull(eventPageElem).attr("href").split("/"))
                      .toList()
                      .getLast();
              bout.setEventPageId(eventPage);
              String eventName = eventPageElem.text();
              bout.setEventName(eventName);

              String fighterRecordBeforeFight =
                  Optional.ofNullable(b.selectFirst("span[title=Fighter Record Before Fight]"))
                      .map(Element::text)
                      .orElse(null);
              bout.setFighterRecordBeforeFight(fighterRecordBeforeFight);
              String opponentRecordBeforeFight =
                  Optional.ofNullable(b.selectFirst("span[title=Opponent Record Before Fight]"))
                      .map(Element::text)
                      .orElse(null);
              bout.setOpponentRecordBeforeFight(opponentRecordBeforeFight);

              Element fightYearElement =
                  b.select("span.font-bold").stream()
                      .filter(e -> e.text().matches("\\d{4}"))
                      .toList()
                      .getFirst();

              String fightYear = fightYearElement.text();
              bout.setFightYear(fightYear);
              String fightDay =
                  Objects.requireNonNull(fightYearElement.nextElementSibling()).text();
              bout.setFightDay(fightDay);

              Element detailRows = doc.selectFirst("div#detail-rows-" + boutId);
              Map<String, String> details =
                  Optional.ofNullable(detailRows).map(el -> el.select("> div")).stream()
                      .flatMap(Collection::stream)
                      .map(row -> row.select("span"))
                      .filter(spans -> spans.size() >= 2)
                      .collect(
                          Collectors.toMap(
                              spans -> spans.getFirst().text().trim(),
                              spans -> spans.get(1).text().trim(),
                              (k, v) -> k,
                              LinkedHashMap::new));

              bout.setDetails(details);
              return bout;
            })
        .toList();
  }
}
