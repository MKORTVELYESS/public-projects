package org.example.domain.mma;

import static org.example.domain.mma.BoutStats.*;
import static org.example.domain.mma.FighterStats.*;

import org.example.entity.FighterDetails;
import org.example.entity.mma.BoutFeatures;

public class FeatureAssembler {
  static BoutFeatures writeFeature(
      CleanBout cleanBout, FighterDetails fighterDetails, FighterDetails opponentDetails) {
    BoutFeatures feature = new BoutFeatures();

    feature.setId(cleanBout.getFeatureId());
    feature.setEloDiff(cleanBout.getEloDiff());
    feature.setTotalFightsDiff(getTotalFightsDiff(cleanBout));
    feature.setProWinsDiff(getProWinsDiff(cleanBout));
    feature.setAgeKnownDiff(getAgeKnownDiff(fighterDetails, opponentDetails));
    feature.setAgeDiff(getAgeDiff(cleanBout, fighterDetails, opponentDetails));
    feature.setHeightKnownDiff(getHeightKnownDiff(fighterDetails, opponentDetails));
    feature.setHeightDiff(getHeightDiff(fighterDetails, opponentDetails));
    feature.setPriorFightKnownDiff(getPriorFightKnownDiff(cleanBout));
    feature.setDaysSincePriorFightDiff(getDaysSincePriorFightDiff(cleanBout));
    feature.setWinStreakDiff(getWinStreakDiff(cleanBout));
    feature.setIsAmateurFight(getIsAmateurFight(cleanBout));
    feature.setFightYear(cleanBout.getDate().getYear());
    feature.setIsTitleBout(getIsTitleBout(cleanBout));
    feature.setHasTitleFightExperienceDiff(getTitleExperienceDiff(cleanBout));
    feature.setTargetWin(getTargetWin(cleanBout));

    return feature;
  }
}
