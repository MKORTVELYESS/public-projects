package org.example.entity.mma;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
public class BoutFeatures {
    @Id
    private String id;

    // FEATURES
    private Float eloDiff;
    private Integer totalFightsDiff; // always compute
    private Long proWinsDiff; // always compute

    private Float ageDiff; // Years! diff = A(fight date - dob) - A(fight date - dob)
    private Byte ageKnownDiff;

    private Float heightDiff;
    private Byte heightKnownDiff;

//    private Float reachDiff;
//    private Byte reachKnownDiff;

    private Float daysSincePriorFightDiff;
    private Byte priorFightKnownDiff;

    private Integer winStreakDiff; // if one fighter is a debut fighter we leave null

    private Byte isAmateurFight;

    private Integer fightYear;

    private Byte isTitleBout;
    private Byte hasTitleFightExperienceDiff;

    // TARGET!
    private Byte targetWin;  // 1 = Fighter A wins, 0 = Fighter A loses


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getEloDiff() {
        return eloDiff;
    }

    public void setEloDiff(Float eloDiff) {
        this.eloDiff = eloDiff;
    }

    public Integer getTotalFightsDiff() {
        return totalFightsDiff;
    }

    public void setTotalFightsDiff(Integer totalFightsDiff) {
        this.totalFightsDiff = totalFightsDiff;
    }

    public Long getProWinsDiff() {
        return proWinsDiff;
    }

    public void setProWinsDiff(Long proWinsDiff) {
        this.proWinsDiff = proWinsDiff;
    }

    public Float getAgeDiff() {
        return ageDiff;
    }

    public void setAgeDiff(Float ageDiff) {
        this.ageDiff = ageDiff;
    }

    public Byte getAgeKnownDiff() {
        return ageKnownDiff;
    }

    public void setAgeKnownDiff(Byte ageKnownDiff) {
        this.ageKnownDiff = ageKnownDiff;
    }

    public Float getHeightDiff() {
        return heightDiff;
    }

    public void setHeightDiff(Float heightDiff) {
        this.heightDiff = heightDiff;
    }

    public Byte getHeightKnownDiff() {
        return heightKnownDiff;
    }

    public void setHeightKnownDiff(Byte heightKnownDiff) {
        this.heightKnownDiff = heightKnownDiff;
    }

    public Float getDaysSincePriorFightDiff() {
        return daysSincePriorFightDiff;
    }

    public void setDaysSincePriorFightDiff(Float daysSincePriorFightDiff) {
        this.daysSincePriorFightDiff = daysSincePriorFightDiff;
    }

    public Byte getPriorFightKnownDiff() {
        return priorFightKnownDiff;
    }

    public void setPriorFightKnownDiff(Byte priorFightKnownDiff) {
        this.priorFightKnownDiff = priorFightKnownDiff;
    }

    public Integer getWinStreakDiff() {
        return winStreakDiff;
    }

    public void setWinStreakDiff(Integer winStreakDiff) {
        this.winStreakDiff = winStreakDiff;
    }

    public Byte getIsAmateurFight() {
        return isAmateurFight;
    }

    public void setIsAmateurFight(Byte isAmateurFight) {
        this.isAmateurFight = isAmateurFight;
    }

    public Integer getFightYear() {
        return fightYear;
    }

    public void setFightYear(Integer fightYear) {
        this.fightYear = fightYear;
    }

    public Byte getIsTitleBout() {
        return isTitleBout;
    }

    public void setIsTitleBout(Byte isTitleBout) {
        this.isTitleBout = isTitleBout;
    }

    public Byte getHasTitleFightExperienceDiff() {
        return hasTitleFightExperienceDiff;
    }

    public void setHasTitleFightExperienceDiff(Byte hasTitleFightExperienceDiff) {
        this.hasTitleFightExperienceDiff = hasTitleFightExperienceDiff;
    }

    public Byte getTargetWin() {
        return targetWin;
    }

    public void setTargetWin(Byte targetWin) {
        this.targetWin = targetWin;
    }
}
