package org.example.util;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.entity.Bout;
import org.example.entity.FighterDetails;
import org.example.entity.FighterRawDetails;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FighterPageParser {
  private static final Logger logger = LoggerFactory.getLogger(FighterPageParser.class);

  // element parsing constants
  private static final String DIV_DATA_FIGHTER_BOUT_TARGET_BOUT =
      "div[data-fighter-bout-target=bout]";
  private static final String SECTION_FIGHTER_FIGHT_RESULTS = "section.fighterFightResults";
  private static final String DATA_STATUS = "data-status";
  private static final String DATA_SPORT = "data-sport";
  private static final String DATA_DIVISION = "data-division";
  private static final String DATA_BOUT_ID = "data-bout-id";
  private static final String METHOD_CODE_SELECTOR = "div.\\-rotate-90";
  private static final String OPPONENT_ID_SELECTOR = "a[title$=Fighter Page]";
  private static final String BOUT_PAGE = "a[title=Bout Page]";
  private static final String EVENT_PAGE = "a[title=Event Page]";
  private static final String FIGHTER_RECORD_BEFORE_FIGHT =
      "span[title=Fighter Record Before Fight]";
  private static final String OPPONENT_RECORD_BEFORE_FIGHT =
      "span[title=Opponent Record Before Fight]";
  public static final String STANDARD_DETAILS_SELECTOR = "#standardDetails";

  /**
   * Best effort basis extractor, extract
   *
   * @param doc
   * @return
   */
  public static List<Bout> parseBouts(Document doc) {
    return Try.of(() -> doc.selectFirst(SECTION_FIGHTER_FIGHT_RESULTS))
        .map(
            section -> {
              assert section != null;
              String fighterId = getFighterIdFromMetaTag(doc);
              return section
                  .selectStream(DIV_DATA_FIGHTER_BOUT_TARGET_BOUT)
                  .map(
                      b -> {
                        Function<String, String> textValueOfAttrib = extractAttributeTextFrom(b);
                        Function<String, String> textValueOfFistElementMatchingSelector =
                            tryToGetFirstElementTextFrom(b);

                        Bout bout = new Bout();

                        bout.setFighterId(fighterId);

                        bout.setStatus(textValueOfAttrib.apply(DATA_STATUS));

                        bout.setSport(textValueOfAttrib.apply(DATA_SPORT));

                        bout.setDivision(textValueOfAttrib.apply(DATA_DIVISION));

                        bout.setBoutId(textValueOfAttrib.apply(DATA_BOUT_ID));

                        bout.setMethod(tryGet(() -> b.selectFirst(METHOD_CODE_SELECTOR).text()));

                        bout.setOpponentId(
                            tryGet(() -> lastPathSegmentHref(b.selectFirst(OPPONENT_ID_SELECTOR))));

                        // Bout page - Event page
                        Try.run(
                            () -> {
                              Element boutPage = b.selectFirst(BOUT_PAGE);
                              Element eventPage = b.selectFirst(EVENT_PAGE);
                              bout.setBoutPageId(lastPathSegmentHref(boutPage));
                              bout.setFightShortDescription(boutPage.text());
                              bout.setEventPageId(lastPathSegmentHref(eventPage));
                              bout.setEventName(eventPage.text());
                            });

                        bout.setFighterRecordBeforeFight(
                            textValueOfFistElementMatchingSelector.apply(
                                FIGHTER_RECORD_BEFORE_FIGHT));

                        bout.setOpponentRecordBeforeFight(
                            textValueOfFistElementMatchingSelector.apply(
                                OPPONENT_RECORD_BEFORE_FIGHT));

                        // Fight date
                        Try.run(
                            () -> {
                              Element yearEl = getYearElementFrom(b);
                              bout.setFightYear(yearEl.text());
                              bout.setFightDay(yearEl.nextElementSibling().text());
                            });

                        // Details
                        bout.setDetails(
                            tryGet(
                                () ->
                                    convertDetailRowsElementToMap(
                                        doc.selectFirst(getDetailRowsCssQuery(bout)))));

                        return bout;
                      })
                  .toList();
            })
        .onFailure(e -> logger.error("Failed extracting fighterFightResults section"))
        .getOrElse(List.of());
  }

  @NotNull
  private static LinkedHashMap<String, String> convertDetailRowsElementToMap(Element detailRows) {
    return detailRows.select("> div").stream()
        .map(row -> row.select("span"))
        .filter(spans -> spans.size() >= 2)
        .collect(
            Collectors.toMap(
                spans -> spans.getFirst().text().trim(),
                spans -> spans.get(1).text().trim(),
                (a, b1) -> a,
                LinkedHashMap::new));
  }

  @NotNull
  private static String getDetailRowsCssQuery(Bout bout) {
    return "div#detail-rows-".concat(bout.getBoutId());
  }

  @NotNull
  private static Element getYearElementFrom(Element b) {
    return b.select("span.font-bold").stream()
        .filter(e -> e.text().matches("\\d{4}"))
        .toList()
        .getFirst();
  }

  private static String getFighterIdFromMetaTag(Document doc) {
    return lastPathSegmentContent(doc.selectFirst("meta[property=og:url]"));
  }

  @NotNull
  private static Function<String, String> extractAttributeTextFrom(Element element) {
    return (String attrName) -> extractAttributeText(element, attrName);
  }

  private static Function<String, String> tryToGetFirstElementTextFrom(Element element) {
    return (selector) -> extractFirstElementText(element, selector);
  }

  @NotNull
  private static String extractAttributeText(Element element, String attributeName) {
    return tryGet(() -> element.attr(attributeName));
  }

  @NotNull
  private static String extractFirstElementText(Element element, String elementSelector) {
    return tryGet(() -> element.selectFirst(elementSelector).text());
  }

  private static <T> T tryGet(CheckedFunction0<T> supplier) {
    return Try.of(supplier).getOrNull();
  }

  private static String lastPathSegmentHref(Element element) {
    return lastPathSegment(element, "href");
  }

  private static String lastPathSegmentContent(Element element) {
    return lastPathSegment(element, "content");
  }

  private static String lastPathSegment(Element element, String attributeKey) {
    return Arrays.stream(element.attr(attributeKey).split("/")).toList().getLast();
  }

  private static FighterRawDetails extract(Document doc) {
    FighterRawDetails details = new FighterRawDetails();

    Element standardDetails = doc.selectFirst(STANDARD_DETAILS_SELECTOR);
    if (standardDetails == null) return details;

    // -------- BASIC LABEL : VALUE EXTRACTION --------
    for (Element row : standardDetails.select("div > div")) {

      // find all strong tags in this row
      Elements labels = row.select("strong");
      for (Element labelEl : labels) {
        String label = normalizeLabel(labelEl.text());

        // value is usually the next span or text node
        Element valueEl = labelEl.nextElementSibling();
        if (valueEl == null) continue;

        String value = valueEl.text().trim();
        if (!value.isEmpty()) {
          details.attributes.put(label, value);
        }
      }
    }

    // -------- SPECIAL CASES --------

    // Age + DOB
    extractInline(details, standardDetails, "Age", "Date of Birth");

    // Height + Reach
    extractInline(details, standardDetails, "Height", "Reach");

    // Weight Class + Last Weigh-In
    extractInline(details, standardDetails, "Weight Class", "Last Weigh-In");

    // Last Fight (includes promotion link text)
    Element lastFight = standardDetails.selectFirst("strong:contains(Last Fight)");
    if (lastFight != null) {
      Element span = lastFight.nextElementSibling();
      if (span != null) {
        details.attributes.put("Last Fight", span.text().replaceAll("\\s+", " ").trim());
      }
    }

    // -------- LINKS --------

    // Affiliation
    Element affiliation = standardDetails.selectFirst("strong:contains(Affiliation)");
    if (affiliation != null) {
      Element link = affiliation.parent().selectFirst("a[href]");
      if (link != null) {
        details.attributes.put("Affiliation", link.text());
      }
    }

    // Resource links
    for (Element a : standardDetails.select("strong:contains(Resource Links) ~ div a[href]")) {
      details.resourceLinks.put(
          a.attr("href"), a.selectFirst("img") != null ? a.selectFirst("img").attr("src") : "");
    }

    // Personal links
    for (Element a : standardDetails.select("strong:contains(Personal Links) ~ div a[href]")) {
      details.personalLinks.put(
          a.attr("href"), a.selectFirst("img") != null ? a.selectFirst("img").attr("src") : "");
    }

    return details;
  }

  private static void extractInline(FighterRawDetails details, Element root, String... keys) {
    for (String key : keys) {
      Element el = root.selectFirst("strong:contains(" + key + ")");
      if (el != null) {
        Element value = el.nextElementSibling();
        if (value != null) {
          details.attributes.put(key, value.text().trim());
        }
      }
    }
  }

  private static String normalizeLabel(String label) {
    return label.replace(":", "").trim();
  }

  public static FighterDetails parseDetails(Document doc) {
    FighterRawDetails r = extract(doc);

    FighterDetails f = new FighterDetails();

    f.setFighterId(getFighterIdFromMetaTag(doc));
    f.setGivenName(r.attributes.get("Name"));
    f.setNickname(r.attributes.get("Nickname"));
    f.setProMmaRecord(r.attributes.get("Pro MMA Record"));
    f.setCurrentMmaStreak(r.attributes.get("Current Streak"));
    f.setHeight(r.attributes.get("Height"));
    f.setReach(r.attributes.get("Reach"));
    f.setWeightClass(r.attributes.get("Weight Class"));
    f.setAffiliation(r.attributes.get("Affiliation"));
    f.setBornIn(r.attributes.get("Born"));
    Try.of(() -> LocalDate.parse(r.attributes.get("Age"))).andThen(f::setDateOfBirth);
    f.setFightingOutOf(r.attributes.get("Fighting out of"));
    f.setLastFight(r.attributes.get("Last Fight"));
    f.setExtraAttributes(r.attributes); // full raw snapshot

    return f;
  }
}
