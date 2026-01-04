package org.example.entity;

public class FighterHeightWinRate {

  public FighterHeightWinRate(String id, Integer height, Double winRate) {
    this.id = id;
    this.height = height;
    this.winRate = winRate;
  }

  private final String id;
  private final Integer height;
  private final Double winRate;

  public String getId() {
    return id;
  }

  public Integer getHeight() {
    return height;
  }

  public Double getWinRate() {
    return winRate;
  }
}
