package org.example.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Bout {
  @Id private String id;

  private String status;
  private String sport;
  private String division;
  private String boutId;
  private String method;
  private String opponentId;
  private String boutPageId;
  private String fightShortDescription;
  private String eventPageId;
  private String eventName;
  private String fighterRecordBeforeFight;
  private String opponentRecordBeforeFight;
  private String fightYear;
  private String fightDay;

  @ElementCollection private Map<String, String> details = new LinkedHashMap<>();

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSport() {
    return sport;
  }

  public void setSport(String sport) {
    this.sport = sport;
  }

  public String getDivision() {
    return division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public String getBoutId() {
    return boutId;
  }

  public void setBoutId(String boutId) {
    this.boutId = boutId;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getOpponentId() {
    return opponentId;
  }

  public void setOpponentId(String opponentId) {
    this.opponentId = opponentId;
  }

  public String getBoutPageId() {
    return boutPageId;
  }

  public void setBoutPageId(String boutPageId) {
    this.boutPageId = boutPageId;
  }

  public String getFightShortDescription() {
    return fightShortDescription;
  }

  public void setFightShortDescription(String fightShortDescription) {
    this.fightShortDescription = fightShortDescription;
  }

  public String getEventPageId() {
    return eventPageId;
  }

  public void setEventPageId(String eventPageId) {
    this.eventPageId = eventPageId;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getFighterRecordBeforeFight() {
    return fighterRecordBeforeFight;
  }

  public void setFighterRecordBeforeFight(String fighterRecordBeforeFight) {
    this.fighterRecordBeforeFight = fighterRecordBeforeFight;
  }

  public String getOpponentRecordBeforeFight() {
    return opponentRecordBeforeFight;
  }

  public void setOpponentRecordBeforeFight(String opponentRecordBeforeFight) {
    this.opponentRecordBeforeFight = opponentRecordBeforeFight;
  }

  public String getFightYear() {
    return fightYear;
  }

  public void setFightYear(String fightYear) {
    this.fightYear = fightYear;
  }

  public String getFightDay() {
    return fightDay;
  }

  public void setFightDay(String fightDay) {
    this.fightDay = fightDay;
  }

  public Map<String, String> getDetails() {
    return new LinkedHashMap<>(details);
  }

  public void setDetails(Map<String, String> details) {
    this.details = new LinkedHashMap<>(details);
  }

  @Override
  public String toString() {
    return "Bout{"
        + "id='"
        + id
        + '\''
        + ", status='"
        + status
        + '\''
        + ", sport='"
        + sport
        + '\''
        + ", division='"
        + division
        + '\''
        + ", boutId='"
        + boutId
        + '\''
        + ", method='"
        + method
        + '\''
        + ", opponentId='"
        + opponentId
        + '\''
        + ", boutPageId='"
        + boutPageId
        + '\''
        + ", fightShortDescription='"
        + fightShortDescription
        + '\''
        + ", eventPageId='"
        + eventPageId
        + '\''
        + ", eventName='"
        + eventName
        + '\''
        + ", fighterRecordBeforeFight='"
        + fighterRecordBeforeFight
        + '\''
        + ", opponentRecordBeforeFight='"
        + opponentRecordBeforeFight
        + '\''
        + ", fightYear='"
        + fightYear
        + '\''
        + ", fightDay='"
        + fightDay
        + '\''
        + ", details="
        + details
        + '}';
  }
}
