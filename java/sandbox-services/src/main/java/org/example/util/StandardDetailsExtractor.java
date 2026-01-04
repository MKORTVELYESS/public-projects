package org.example.util;

import io.vavr.control.Try;
import java.time.LocalDate;
import org.example.entity.FighterDetails;
import org.example.entity.FighterRawDetails;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StandardDetailsExtractor {

  public static FighterRawDetails extract(Document doc) {
    FighterRawDetails details = new FighterRawDetails();

    Element root = doc.selectFirst("#standardDetails");
    if (root == null) return details;

    // -------- BASIC LABEL : VALUE EXTRACTION --------
    for (Element row : root.select("div > div")) {

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
    extractInline(details, root, "Age", "Date of Birth");

    // Height + Reach
    extractInline(details, root, "Height", "Reach");

    // Weight Class + Last Weigh-In
    extractInline(details, root, "Weight Class", "Last Weigh-In");

    // Last Fight (includes promotion link text)
    Element lastFight = root.selectFirst("strong:contains(Last Fight)");
    if (lastFight != null) {
      Element span = lastFight.nextElementSibling();
      if (span != null) {
        details.attributes.put("Last Fight", span.text().replaceAll("\\s+", " ").trim());
      }
    }

    // -------- LINKS --------

    // Affiliation
    Element affiliation = root.selectFirst("strong:contains(Affiliation)");
    if (affiliation != null) {
      Element link = affiliation.parent().selectFirst("a[href]");
      if (link != null) {
        details.attributes.put("Affiliation", link.text());
      }
    }

    // Resource links
    for (Element a : root.select("strong:contains(Resource Links) ~ div a[href]")) {
      details.resourceLinks.put(
          a.attr("href"), a.selectFirst("img") != null ? a.selectFirst("img").attr("src") : "");
    }

    // Personal links
    for (Element a : root.select("strong:contains(Personal Links) ~ div a[href]")) {
      details.personalLinks.put(
          a.attr("href"), a.selectFirst("img") != null ? a.selectFirst("img").attr("src") : "");
    }

    return details;
  }

  // -------- HELPERS --------

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

  public static FighterDetails convert(FighterRawDetails r, String id) {
    FighterDetails f = new FighterDetails();
    f.setFighterId(id);
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
