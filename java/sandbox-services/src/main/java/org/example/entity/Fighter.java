package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Fighter {

  @Id private String id;

  private String name;
  private String height;
  private String weightClass;
  private String record;
  private String nation;

  protected Fighter() {
    // JPA
  }

  public Fighter(
      String id, String name, String height, String weightClass, String record, String nation) {
    this.id = id;
    this.name = name;
    this.height = height;
    this.weightClass = weightClass;
    this.record = record;
    this.nation = nation;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getHeight() {
    return height;
  }

  public String getWeightClass() {
    return weightClass;
  }

  public String getRecord() {
    return record;
  }

  public String getNation() {
    return nation;
  }

  @Override
  public String toString() {
    return "Fighter{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", height='"
        + height
        + '\''
        + ", weightClass='"
        + weightClass
        + '\''
        + ", record='"
        + record
        + '\''
        + ", nation='"
        + nation
        + '\''
        + '}';
  }
}
