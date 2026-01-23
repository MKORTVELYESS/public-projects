package org.example.domain.mma;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.example.entity.Bout;
import org.jetbrains.annotations.NotNull;

public class CleanBout implements Comparable<CleanBout> {

  public static final String WIN = "win";
  public static final char C = ' ';
  private static final float K = 32f;
  public static final float DEFAULT_ELO = 1500.0f;
  public static final String PRO = "pro";

  public static CleanBout fromBout(Bout bout, Set<String> titleBouts) {
    CleanBout cleanBout = new CleanBout();

    cleanBout.setDate(parseDate(bout));
    cleanBout.setBoutUrl(bout.getBoutPageId());
    cleanBout.setWin(WIN.equals(bout.getStatus()));
    cleanBout.setFighterId(bout.getFighterId());
    cleanBout.setOpponentId(bout.getOpponentId());
    cleanBout.setPro(PRO.equals(bout.getDivision()));
    cleanBout.setTitle(titleBouts.contains(bout.getId()));
    return cleanBout;
  }

  private String fighterId;
  private String opponentId;
  private String boutUrl;
  private LocalDate date;
  private CleanBout fighterPriorBout;
  private CleanBout opponentPriorBout;
  private Float postElo;
  private Boolean isWin;
  private Boolean isPro;
  private Boolean isTitle;

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getBoutUrl() {
    return boutUrl;
  }

  public void setBoutUrl(String boutUrl) {
    this.boutUrl = boutUrl;
  }

  public Float getPostElo() {
    if (postElo == null) {
      computePostFightElo();
    }
    return postElo;
  }

  public CleanBout getFighterPriorBout() {
    return fighterPriorBout;
  }

  public CleanBout getOpponentPriorBout() {
    return opponentPriorBout;
  }

  public Boolean getWin() {
    return isWin;
  }

  public void setWin(Boolean win) {
    isWin = win;
  }

  public Boolean getPro() {
    return isPro;
  }

  public void setPro(Boolean pro) {
    isPro = pro;
  }

  public String getFighterId() {
    return fighterId;
  }

  public void setFighterId(String fighterId) {
    this.fighterId = fighterId;
  }

  public String getOpponentId() {
    return opponentId;
  }

  public void setOpponentId(String opponentId) {
    this.opponentId = opponentId;
  }

  public void setFighterPriorBout(CleanBout fighterPriorBout) {
    this.fighterPriorBout = fighterPriorBout;
  }

  public void setOpponentPriorBout(CleanBout opponentPriorBout) {
    this.opponentPriorBout = opponentPriorBout;
  }

  public Boolean getTitle() {
    return isTitle;
  }

  public void setTitle(Boolean title) {
    isTitle = title;
  }

  public static LocalDate parseDate(Bout bout) {
    String day = bout.getFightDay(); // e.g. "Jan 5"
    String year = bout.getFightYear(); // e.g. "2024"

    if (day == null || year == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder(day.length() + 1 + year.length());

    sb.append(day).append(C).append(year);

    try {
      return LocalDate.parse(sb, FeatureWriter.BOUT_DATE_FORMAT);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  private void computePostFightElo() {
    if (getFighterPriorBout() == null) {
      this.postElo = DEFAULT_ELO;
      return;
    }
    float oppElo = opponentPriorBout != null ? opponentPriorBout.getPostElo() : DEFAULT_ELO;
    float myElo = fighterPriorBout != null ? fighterPriorBout.getPostElo() : DEFAULT_ELO;

    float s = Boolean.TRUE.equals(getWin()) ? 1.0f : 0.0f;
    float p = expectedWinProbability(oppElo, myElo);
    this.postElo = myElo + K * (s - p);
  }

  private static float expectedWinProbability(Float oppElo, Float myElo) {
    double exponent = (oppElo - myElo) / 400.0;
    return (float) (1.0 / (1.0 + Math.pow(10.0, exponent)));
  }

  public Float getEloDiff() {
    float fighterPostElo = fighterPriorBout != null ? fighterPriorBout.getPostElo() : DEFAULT_ELO;
    float opponentPostElo =
        opponentPriorBout != null ? opponentPriorBout.getPostElo() : DEFAULT_ELO;

    return fighterPostElo - opponentPostElo;
  }

  public boolean isIdentifiable() {
    return this.getBoutUrl() != null && this.getFighterId() != null;
  }

  public String getFeatureId() {
    return this.getBoutUrl().concat("-").concat(this.getFighterId());
  }

  @Override
  public int compareTo(@NotNull CleanBout o) {
    return Comparator.comparing(
            CleanBout::getDate, Comparator.nullsFirst(Comparator.naturalOrder()))
        .thenComparing(CleanBout::getFighterId)
        .thenComparing(CleanBout::getOpponentId)
        .thenComparing(CleanBout::getBoutUrl)
        .compare(this, o);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CleanBout cleanBout)) return false;
    return Objects.equals(getFighterId(), cleanBout.getFighterId())
        && Objects.equals(getOpponentId(), cleanBout.getOpponentId())
        && Objects.equals(getBoutUrl(), cleanBout.getBoutUrl())
        && Objects.equals(getDate(), cleanBout.getDate());
    // && Objects.equals(getPostElo(), cleanBout.getPostElo()) - lazily initiated field can only
    // be called after prior bouts are se, this will call it accidentally

  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getFighterId(), getOpponentId(), getBoutUrl(), getDate()
        // getPostElo(), - lazily initiated field can only be called after prior bouts are set, this
        // would call it accidentally
        );
  }
}
