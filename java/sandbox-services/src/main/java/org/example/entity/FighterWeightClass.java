package org.example.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FighterWeightClass {
  private final String name;
  private final List<FighterHeightWinRate> results;
  private final List<FighterHeightWinRate> sortedByWinRate;
  private final List<FighterHeightWinRate> sortedByHeight;

  public FighterWeightClass(String name, List<FighterHeightWinRate> results) {
    this.name = name;
    this.results = new ArrayList<>(results);
    this.sortedByWinRate =
        this.results.stream()
            .sorted(Comparator.comparing(FighterHeightWinRate::getWinRate))
            .toList();
    this.sortedByHeight =
        this.results.stream()
            .sorted(Comparator.comparing(FighterHeightWinRate::getHeight))
            .toList();
  }

  public Double getWinRatePercentile(FighterHeightWinRate f) {
    return (double) sortedByWinRate.indexOf(f) / (sortedByWinRate.size() - 1);
  }

  public Double getHeightPercentile(FighterHeightWinRate f) {
    return (double) sortedByHeight.indexOf(f) / (sortedByHeight.size() - 1);
  }

  public String getName() {
    return name;
  }
}
