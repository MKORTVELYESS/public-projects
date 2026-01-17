package org.example.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.example.entity.*;
import org.example.repository.BoutRepository;
import org.example.repository.FighterDetailsRepository;
import org.example.repository.FighterRepository;
import org.example.util.FightDetailsExtractor;
import org.example.util.StandardDetailsExtractor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TapologyService {
  private static final Logger log = LoggerFactory.getLogger(TapologyService.class);
  private static final String FIGHTER_DETAILS_BASE_URL =
      "https://www.tapology.com/fightcenter/fighters/";
  private final HtmlFetchService htmlFetchService;
  private final ResolveDocumentService resolveDocumentService;
  private final FighterRepository fighterRepository;
  private final FighterDetailsRepository fighterDetailsRepository;
  private final BoutRepository boutRepository;
  private final Pattern heightPattern = Pattern.compile("(\\d{3})\\s*cm");
  private final Pattern recordPattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");

  public TapologyService(
      HtmlFetchService htmlFetchService,
      ResolveDocumentService resolveDocumentService,
      FighterRepository fighterRepository,
      FighterDetailsRepository fighterDetailsRepository,
      BoutRepository boutRepository) {
    this.htmlFetchService = htmlFetchService;
    this.resolveDocumentService = resolveDocumentService;
    this.fighterRepository = fighterRepository;
    this.fighterDetailsRepository = fighterDetailsRepository;
    this.boutRepository = boutRepository;
  }

  public List<Fighter> listFighters(String url) {
    String html = htmlFetchService.fetchHtml(url);
    Document document = resolveDocumentService.resolveDocument(html);
    Element tbody = resolveDocumentService.resolveTbody(document);
    return resolveDocumentService.resolveFightersTable(tbody);
  }

  public String printFighters(String url) {
    List<Fighter> fighters = listFighters(url);
    persistFighters(fighters);
    String result = fighters.stream().map(this::formatFighter).collect(Collectors.joining("\n"));
    // System.out.println(result);
    return result;
  }

  @Transactional
  public void persistFighters(List<Fighter> fighters) {
    fighterRepository.saveAll(fighters);
  }

  public List<Fighter> retrieveFighters() {
    return fighterRepository.findAll();
  }

  public void persistAllFighterDetails() {
    var alreadyHasDetails =
        new HashSet<>(
            fighterDetailsRepository.findAll().stream().map(FighterDetails::getFighterId).toList());
    retrieveFighters().parallelStream()
        .filter(f -> !alreadyHasDetails.contains(f.getId()))
        .forEach(
            f -> {
              persistFighterDetails(f);
            });
  }

  public void persistFighterDetails(Fighter fighter) {
    String url = FIGHTER_DETAILS_BASE_URL.concat(fighter.getId());
    log.info("Fetching: {}", url);
    String html = htmlFetchService.fetchHtml(url);
    Document document = resolveDocumentService.resolveDocument(html);
    FighterRawDetails raw = StandardDetailsExtractor.extract(document);
    List<Bout> bouts = FightDetailsExtractor.extract(document, fighter.getId());
    FighterDetails details = StandardDetailsExtractor.convert(raw, fighter.getId());
    fighterDetailsRepository.save(details);
    boutRepository.saveAll(bouts);
  }

  public void getHeightWinRateCorrelationsByWeightGroup() {
    Map<String, List<Fighter>> grouped =
        retrieveFighters().stream().collect(Collectors.groupingBy(Fighter::getWeightClass));
    grouped.forEach(
        (label, grp) -> {
          var rates = getCleanFighterHeightAndWinRate(grp);
          var weightClass = new FighterWeightClass(label, rates);
          var percentiles =
              rates.stream()
                  .map(
                      r ->
                          new FighterHeightWinRatePercentile(
                              weightClass.getHeightPercentile(r),
                              weightClass.getWinRatePercentile(r)))
                  .toList();
          var heightPercentiles =
              percentiles.stream()
                  .mapToDouble(FighterHeightWinRatePercentile::heightPercentileWithinClass)
                  .toArray();
          var winRatePercentiles =
              percentiles.stream()
                  .mapToDouble(FighterHeightWinRatePercentile::winRatePercentileWithinClass)
                  .toArray();

          PearsonsCorrelation pc = new PearsonsCorrelation();
          double correlation = pc.correlation(heightPercentiles, winRatePercentiles);
          double p = pValue(correlation, percentiles.size());
          System.out.printf(
              "Correlation %.4f in group %s. The sample size is: %d P value: %.4f%n",
              correlation, label, percentiles.size(), p);
        });
  }

  public static double pValue(double r, int n) {
    if (n < 3) return 1.0;

    double t = r * Math.sqrt((n - 2) / (1 - r * r));

    NormalDistribution normal = new NormalDistribution();
    return 2 * (1 - normal.cumulativeProbability(Math.abs(t)));
  }

  public List<FighterHeightWinRate> getCleanFighterHeightAndWinRate(List<Fighter> fighters) {
    return fighters.stream()
        .map((f) -> new FighterHeightWinRate(f.getId(), parseHeight(f), parseWinRate(f)))
        .filter(r -> r.getWinRate() != null)
        .filter(r -> r.getHeight() != null)
        .toList();
  }

  private Double parseWinRate(Fighter fighter) {
    if (fighter.getRecord() == null) {
      return null;
    }

    Matcher matcher = recordPattern.matcher(fighter.getRecord());

    if (!matcher.find()) {
      return null;
    }

    int wins = Integer.parseInt(matcher.group(1));
    int losses = Integer.parseInt(matcher.group(2));
    int draws = Integer.parseInt(matcher.group(3));

    int total = wins + losses + draws;
    if (total == 0) {
      return null;
    }

    return wins / (double) total;
  }

  private Integer parseHeight(Fighter fighter) {
    // log.info("Evaluation fighter: {}", fighter);
    if (fighter.getHeight() == null) {
      // log.info("Height null");
      return null;
    }

    Matcher matcher = heightPattern.matcher(fighter.getHeight());

    if (matcher.find()) {
      // log.info("found height: {}", matcher.group(1));
      return Integer.parseInt(matcher.group(1));
    }
    // log.info("Height regex not matched in {}", fighter.getHeight());
    return null;
  }

  private String formatFighter(Fighter fighter) {
    return String.format(
        "ID=%s | Name=%s | Height=%s | Class=%s | Record=%s | Nation=%s",
        fighter.getId(),
        fighter.getName(),
        fighter.getHeight(),
        fighter.getWeightClass(),
        fighter.getRecord(),
        fighter.getNation());
  }
}
