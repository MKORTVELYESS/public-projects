package org.example.entity.mma;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BoutFeatures {
    @Id
    private String id;

    // FEATURES
    private Float eloDiff;
    private Byte totalFightsDiff; // always compute
    private Byte proWinsDiff; // always compute

    private Float ageDiff; // Years! diff = A(fight date - dob) - A(fight date - dob)
    private Byte ageKnownDiff;

    private Float heightDiff;
    private Byte heightKnownDiff;

    private Integer reachDiff;
    private Byte reachKnownDiff;

    private Float daysSincePriorFightDiff;
    private Byte priorFightKnownDiff;

    private Integer winStreakDiff; // if one fighter is a debut fighter we leave null
    private Byte winStreakKnownDiff;

    private Byte isAmateurFight;

    private Integer fightYear;

    private Byte isTitleBout;
    private Byte hasTitleFightExperienceDiff;

    // TARGET!
    private Byte targetWin;  // 1 = Fighter A wins, 0 = Fighter A loses

}
