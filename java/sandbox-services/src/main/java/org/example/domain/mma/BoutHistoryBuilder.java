package org.example.domain.mma;

import io.vavr.control.Try;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.example.entity.Bout;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoutHistoryBuilder {

  private static final Logger log = LoggerFactory.getLogger(BoutHistoryBuilder.class);

  static Set<CleanBout> buildBoutHistory(List<Bout> allBouts, Set<String> titleBouts) {
    log.info("Start cleaning data. Size of all bouts: {}", allBouts.size());
    Set<CleanBout> cleanBouts =
        allBouts.stream()
            .filter(Bout::isWinOrLoss)
            .map(b -> CleanBout.fromBout(b, titleBouts))
            .collect(Collectors.toSet());
    log.info("Cleaned data. Size of clean bouts: {}", cleanBouts.size());
    buildBoutDependencies(cleanBouts);
    log.info("Done setting dependency graph and elo calc done.");
    return cleanBouts;
  }

  @NotNull
  private static Map<String, TreeSet<CleanBout>> cleanBoutsByFighterId(Set<CleanBout> cleanBouts) {
    return cleanBouts.stream()
        .collect(
            Collectors.groupingBy(CleanBout::getFighterId, Collectors.toCollection(TreeSet::new)));
  }

  private static void buildBoutDependencies(Set<CleanBout> cleanBouts) {
    Map<String, TreeSet<CleanBout>> fights = cleanBoutsByFighterId(cleanBouts);
    cleanBouts.forEach(
        (b) -> {
          b.setFighterPriorBout(tryFindPrevBoutOfFighter(b, fights));
          b.setOpponentPriorBout(tryFindPrevBoutOfOpponent(b, fights));
        });
    preCacheElo(cleanBouts);
  }

  private static CleanBout tryFindPrevBoutOfFighter(
      CleanBout currentBout, Map<String, TreeSet<CleanBout>> boutsByFighter) {
    return tryFindPrevBout(currentBout, boutsByFighter, currentBout.getFighterId());
  }

  private static CleanBout tryFindPrevBoutOfOpponent(
      CleanBout currentBout, Map<String, TreeSet<CleanBout>> boutsByFighter) {
    return tryFindPrevBout(currentBout, boutsByFighter, currentBout.getOpponentId());
  }

  private static CleanBout tryFindPrevBout(
      CleanBout currentBout, Map<String, TreeSet<CleanBout>> boutsByFighter, String id) {
    return Try.of(() -> boutsByFighter.get(id).lower(currentBout)).getOrNull();
  }

  private static void preCacheElo(Set<CleanBout> bouts) {
    bouts.forEach(CleanBout::getEloDiff);
  }
}
