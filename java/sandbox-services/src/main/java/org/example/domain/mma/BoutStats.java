package org.example.domain.mma;

import static org.example.domain.mma.MeasurementUtils.*;

import io.vavr.control.Try;
import java.time.temporal.ChronoUnit;

public class BoutStats {

  static byte getTargetWin(CleanBout cleanBout) {
    return cleanBout.getWin() ? (byte) 1 : (byte) 0;
  }

  static byte getPriorFightKnownDiff(CleanBout cleanBout) {
    return knownDiff(
        yesNoIfNotNull(cleanBout.getFighterPriorBout()),
        yesNoIfNotNull(cleanBout.getOpponentPriorBout()));
  }

  static long getProWinsDiff(CleanBout cleanBout) {
    return (getProWinsBeforeAndIncluding(cleanBout.getFighterPriorBout())
        - getProWinsBeforeAndIncluding(cleanBout.getOpponentPriorBout()));
  }

  static int getTotalFightsDiff(CleanBout cleanBout) {
    return getTotalFightsBeforeAndIncluding(cleanBout.getFighterPriorBout())
        - getTotalFightsBeforeAndIncluding(cleanBout.getOpponentPriorBout());
  }

  static byte getIsAmateurFight(CleanBout cleanBout) {
    return Boolean.TRUE.equals(cleanBout.getPro()) ? NO : YES;
  }

  static byte getIsTitleBout(CleanBout cleanBout) {
    return Boolean.TRUE.equals(cleanBout.getTitle()) ? YES : NO;
  }

  static Float getDaysSincePriorFightDiff(CleanBout bout) {
    return Try.of(
            () -> {
              long fighterDaysSincePrior =
                  ChronoUnit.DAYS.between(bout.getFighterPriorBout().getDate(), bout.getDate());
              long opponentDaysSincePrior =
                  ChronoUnit.DAYS.between(bout.getOpponentPriorBout().getDate(), bout.getDate());
              return (Long.valueOf(fighterDaysSincePrior - opponentDaysSincePrior)).floatValue();
            })
        .getOrNull();
  }

  static Integer getWinStreakDiff(CleanBout bout) {
    Integer fighterWinStreak = getWinStreakStartingFrom(bout.getFighterPriorBout());
    Integer opponentWinStreak = getWinStreakStartingFrom(bout.getOpponentPriorBout());

    return Try.of(() -> (fighterWinStreak - opponentWinStreak)).getOrNull();
  }

  static byte getTitleExperienceDiff(CleanBout bout) {
    byte fighterHasTitleExp = getHasTitleExperience(bout.getFighterPriorBout());
    byte opponentHasTitleExp = getHasTitleExperience(bout.getOpponentPriorBout());

    return knownDiff(fighterHasTitleExp, opponentHasTitleExp);
  }

  private static Integer getWinStreakStartingFrom(CleanBout bout) {
    if (bout == null) return null;
    int streak = 0;
    CleanBout currentBout = bout;
    while (currentBout != null && currentBout.getWin()) {
      streak = streak + 1;
      currentBout = currentBout.getFighterPriorBout();
    }
    return streak;
  }

  private static byte getHasTitleExperience(CleanBout bout) {
    byte exp = NO;
    CleanBout currentBout = bout;
    while (currentBout != null) {
      if (currentBout.getTitle()) {
        exp = YES;
        break;
      }
      currentBout = currentBout.getFighterPriorBout();
    }
    return exp;
  }

  private static int getTotalFightsBeforeAndIncluding(CleanBout bout) {
    int count = 0;
    CleanBout currentBout = bout;
    while (currentBout != null) {
      count++;
      currentBout = currentBout.getFighterPriorBout();
    }
    return count;
  }

  private static int getProWinsBeforeAndIncluding(CleanBout bout) {
    int count = 0;
    CleanBout currentBout = bout;
    while (currentBout != null) {
      if (currentBout.getPro()) count++;
      currentBout = currentBout.getFighterPriorBout();
    }
    return count;
  }
}
