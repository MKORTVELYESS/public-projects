package org.example.service;

import static org.jsoup.internal.Normalizer.normalize;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.Fighter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ResolveDocumentService {

  public Document resolveDocument(String html) {
    if (html == null || html.isBlank()) {
      throw new IllegalArgumentException("HTML body is null or empty");
    }

    return Jsoup.parse(html);
  }

  public Element resolveTbody(Document document) {
    return document.selectFirst("tbody");
  }

  public List<Fighter> resolveFightersTable(Element tbody) {
    List<Fighter> fighters = new ArrayList<>();

    for (Element row : tbody.select("tr")) {
      Elements cells = row.select("td");

      if (cells.size() < 9) {
        continue; // defensive: skip malformed rows
      }

      // --- Name + ID from anchor ---
      Element link = cells.get(0).selectFirst("a");
      if (link == null) {
        continue;
      }

      String name = link.text();

      // "/fightcenter/fighters/184367-michiyo-shew"
      String href = link.attr("href");
      String id = href.substring(href.lastIndexOf('/') + 1);

      // --- Height (empty cell allowed) ---
      String height = normalize(cells.get(2).text());

      // --- Weight class ---
      String weightClass = normalize(cells.get(4).text());

      // --- Record ---
      String record = normalize(cells.get(6).text());

      // --- Nation from flag image ---
      Element img = cells.get(8).selectFirst("img");
      String nation = null;
      if (img != null) {
        // "/assets/flags/CA-xxxx.gif" → "CA"
        String src = img.attr("src");
        nation = src.substring(src.lastIndexOf('/') + 1, src.indexOf('-'));
      }

      fighters.add(new Fighter(id, name, height, weightClass, record, nation));
    }

    return fighters;
  }
}
