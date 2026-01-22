package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class FighterDetails {

  @Id private String fighterId;

  private String givenName;

  private String nickname;

  private String proMmaRecord;

  private String currentMmaStreak;

  private String height;

  private String reach;

  private String weightClass;

  private LocalDate dateOfBirth;

  private String bornIn;

  private String fightingOutOf;

  private String affiliation;

  private Long careerEarningsUsd;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "extra_attributes", columnDefinition = "jsonb")
  private Map<String, String> extraAttributes = new HashMap<>();

  //    @OneToMany(mappedBy = "fighter", cascade = CascadeType.ALL, orphanRemoval = true)
  //    private Set<FighterLink> links;

  private LocalDateTime snapTime;

  private String sourceUrl;

  public String getFighterId() {
    return fighterId;
  }

  public String getGivenName() {
    return givenName;
  }

  public String getNickname() {
    return nickname;
  }

  public String getProMmaRecord() {
    return proMmaRecord;
  }

  public String getCurrentMmaStreak() {
    return currentMmaStreak;
  }

  public String getHeight() {
    return height;
  }

  public String getReach() {
    return reach;
  }

  public String getWeightClass() {
    return weightClass;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public String getBornIn() {
    return bornIn;
  }

  public String getFightingOutOf() {
    return fightingOutOf;
  }

  public String getAffiliation() {
    return affiliation;
  }

  public Long getCareerEarningsUsd() {
    return careerEarningsUsd;
  }

  public Map<String, String> getExtraAttributes() {
    return new HashMap<>(extraAttributes);
  }

  public LocalDateTime getSnapTime() {
    return snapTime;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public void setFighterId(String id) {
    this.fighterId = id;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setProMmaRecord(String proMmaRecord) {
    this.proMmaRecord = proMmaRecord;
  }

  public void setCurrentMmaStreak(String currentMmaStreak) {
    this.currentMmaStreak = currentMmaStreak;
  }

  public void setReach(String reach) {
    this.reach = reach;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public void setWeightClass(String weightClass) {
    this.weightClass = weightClass;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public void setBornIn(String bornIn) {
    this.bornIn = bornIn;
  }

  public void setFightingOutOf(String fightingOutOf) {
    this.fightingOutOf = fightingOutOf;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public void setCareerEarningsUsd(Long careerEarningsUsd) {
    this.careerEarningsUsd = careerEarningsUsd;
  }

  public void setExtraAttributes(Map<String, String> extraAttributes) {
    this.extraAttributes = new HashMap<>(extraAttributes);
  }

  public void setSnapTime(LocalDateTime snapTime) {
    this.snapTime = snapTime;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }
}
