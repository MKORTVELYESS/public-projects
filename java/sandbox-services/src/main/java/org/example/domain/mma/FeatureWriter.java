package org.example.domain.mma;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.example.entity.Bout;
import org.example.entity.FighterDetails;
import org.example.entity.mma.BoutFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureWriter {

  static DateTimeFormatter BOUT_DATE_FORMAT =
      DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
  private static final Logger log = LoggerFactory.getLogger(FeatureWriter.class);

  public static List<BoutFeatures>  compose(
      List<Bout> allBouts, List<FighterDetails> allFighterDetails, Set<String> titleBouts) {
    log.info("Start building bout history");
    Set<CleanBout> data = BoutHistoryBuilder.buildBoutHistory(allBouts, titleBouts);
    log.info("Done building bout history");
    Map<String, List<FighterDetails>> fighterDetailsMap =
        allFighterDetails.stream().collect(Collectors.groupingBy(FighterDetails::getFighterId));
    log.info("Built fighter lookup map");
    return data.stream()
        .filter(FeatureFilters.boutsParticipantsHaveMappedDetails(fighterDetailsMap))
        .filter(CleanBout::isIdentifiable)
        .map(
            b -> {
              FighterDetails f = fighterDetailsMap.get(b.getFighterId()).getFirst();
              FighterDetails o = fighterDetailsMap.get(b.getOpponentId()).getFirst();
              return FeatureAssembler.writeFeature(b, f, o);
            })
        .toList();
  }
}
