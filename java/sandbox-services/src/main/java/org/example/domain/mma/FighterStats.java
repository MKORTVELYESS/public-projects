package org.example.domain.mma;

import static org.example.domain.mma.MeasurementUtils.*;

import io.vavr.control.Try;
import java.time.temporal.ChronoUnit;
import org.example.entity.FighterDetails;
import org.jetbrains.annotations.Nullable;

public class FighterStats {
  @Nullable
  static Float getHeightDiff(FighterDetails fighterDetails, FighterDetails opponentDetails) {
    return diff(getHeightCm(fighterDetails), getHeightCm(opponentDetails));
  }

  static byte getHeightKnownDiff(FighterDetails fighterDetails, FighterDetails opponentDetails) {
    return knownDiff(getIsHeightKnown(fighterDetails), getIsHeightKnown(opponentDetails));
  }

  @Nullable
  static Float getAgeDiff(
      CleanBout cleanBout, FighterDetails fighterDetails, FighterDetails opponentDetails) {
    return diff(
        getAgeAtFightTime(fighterDetails, cleanBout),
        getAgeAtFightTime(opponentDetails, cleanBout));
  }

  static byte getAgeKnownDiff(FighterDetails fighterDetails, FighterDetails opponentDetails) {
    return knownDiff(getIsAgeKnown(fighterDetails), getIsAgeKnown(opponentDetails));
  }

  static Byte getIsAgeKnown(FighterDetails fighterDetails) {
    return yesNoIfNotNull(fighterDetails.getDateOfBirth());
  }

  static Byte getIsHeightKnown(FighterDetails fighterDetails) {
    return yesNoIfNotNull(getHeightCm(fighterDetails));
  }

  static Float getAgeAtFightTime(FighterDetails fighter, CleanBout bout) {
    return Try.of(
            () ->
                (float)
                    (ChronoUnit.DAYS.between(fighter.getDateOfBirth(), bout.getDate()) / 365.2425))
        .getOrNull();
  }
}
