package org.example.domain.mma;

import io.vavr.control.Try;
import org.example.entity.Bout;
import org.example.entity.FighterDetails;
import org.example.entity.mma.BoutFeatures;
import org.example.service.TapologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FeatureWriter {

    static DateTimeFormatter BOUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    private static final Byte YES = 1;
    private static final Byte NO = 0;
    private static final Pattern HEIGHT_CM_FORMAT = Pattern.compile("(\\d+)cm");
    private static final Logger log = LoggerFactory.getLogger(FeatureWriter.class);

    private static BoutFeatures writeFeature(CleanBout cleanBout, FighterDetails fighterDetails, FighterDetails opponentDetails) {
        BoutFeatures feature = new BoutFeatures();
        feature.setId(cleanBout.getBoutUrl().concat("-").concat(cleanBout.getFighterId()));
        feature.setEloDiff(cleanBout.getEloDiff());
        feature.setTotalFightsDiff(getTotalFightsBeforeAndIncluding(cleanBout.getFighterPriorBout()) - getTotalFightsBeforeAndIncluding(cleanBout.getOpponentPriorBout()));
        feature.setProWinsDiff((long) (getProWinsBeforeAndIncluding(cleanBout.getFighterPriorBout()) - getProWinsBeforeAndIncluding(cleanBout.getOpponentPriorBout())));

        feature.setAgeKnownDiff(knownDiff(getIsAgeKnown(fighterDetails), getIsAgeKnown(opponentDetails)));
        feature.setAgeDiff(diff(getAgeAtFightTime(fighterDetails, cleanBout), getAgeAtFightTime(opponentDetails, cleanBout)));
        feature.setHeightKnownDiff(knownDiff(getIsHeightKnown(fighterDetails), getIsHeightKnown(opponentDetails)));
        feature.setHeightDiff(diff(getHeightCm(fighterDetails), getHeightCm(opponentDetails)));
        feature.setPriorFightKnownDiff(knownDiff(yesNoIfNotNull(cleanBout.getFighterPriorBout()), yesNoIfNotNull(cleanBout.getOpponentPriorBout())));
        feature.setDaysSincePriorFightDiff(getDaysSincePriorFightDiff(cleanBout));
        feature.setWinStreakDiff(getWinStreakDiff(cleanBout));
        feature.setIsAmateurFight(getIsAmateurFight(cleanBout));
        feature.setFightYear(cleanBout.getDate().getYear());
        feature.setIsTitleBout(getIsTitleBout(cleanBout));
        feature.setHasTitleFightExperienceDiff(getTitleExperienceDiff(cleanBout));
        feature.setTargetWin(cleanBout.getWin() ? (byte) 1 : (byte) 0);
        return feature;
    }

    public static List<BoutFeatures> writeFeatures(List<Bout> allBouts, List<FighterDetails> allFighterDetails, Set<String> titleBouts) {
        log.info("Start building bout history");
        Set<CleanBout> data = buildBoutHistory(allBouts, titleBouts);
        log.info("Done building bout history");
        Map<String, List<FighterDetails>> fighterDetailsMap = allFighterDetails.stream().collect(Collectors.groupingBy(FighterDetails::getFighterId));
        log.info("Built fighter lookup map");
        return data.stream()
                .filter(b ->
                        fighterDetailsMap.containsKey(b.getFighterId()) &&
                                fighterDetailsMap.containsKey(b.getOpponentId()) &&
                                !fighterDetailsMap.get(b.getFighterId()).isEmpty() &&
                                !fighterDetailsMap.get(b.getOpponentId()).isEmpty()
                ).filter(b -> b.getBoutUrl() != null && b.getFighterId() != null)
                .map(b -> {
                    FighterDetails f = fighterDetailsMap.get(b.getFighterId()).getFirst();
                    FighterDetails o = fighterDetailsMap.get(b.getOpponentId()).getFirst();
                    return writeFeature(b, f, o);
                })
                .toList();
    }

    private static Set<CleanBout> buildBoutHistory(List<Bout> allBouts, Set<String> titleBouts) {
        Comparator<CleanBout> boutComparator =
                Comparator.comparing(CleanBout::getDate, Comparator.nullsFirst(Comparator.naturalOrder()));
        log.info("Start cleaning data. Size of all bouts: {}", allBouts.size());
        Set<CleanBout> cleanBouts = allBouts.stream().filter(b -> {
            return "win".equals(b.getStatus()) || "loss".equals(b.getStatus());
        }).map(b -> CleanBout.fromBout(b, titleBouts)).collect(Collectors.toSet());
        log.info("Cleaned data. Size of clean bouts: {}", cleanBouts.size());
        Map<String, TreeSet<CleanBout>> grouped =
                cleanBouts.stream()
                        .collect(Collectors.groupingBy(
                                CleanBout::getFighterId,
                                Collectors.toCollection(() -> new TreeSet<>(boutComparator))
                        ));
        log.info("Grouped clean data into {} groups by fighter Id", grouped.size());
        cleanBouts
                .forEach((b) -> {
                    b.setFighterPriorBout(Try.of(() -> grouped.get(b.getFighterId()).lower(b)).getOrNull());
                    b.setOpponentPriorBout(Try.of(() -> grouped.get(b.getOpponentId()).lower(b)).getOrNull());
                });
        log.info("Done setting dependency graph. Start elo calc now.");
        StopWatch sw = new StopWatch();
        sw.start();
        EloWriter.writeElo(cleanBouts);
        sw.stop();
        log.info("Calced elo in {} ms", sw.getTotalTimeMillis());
        return cleanBouts;
    }

    private static byte getIsAmateurFight(CleanBout cleanBout) {
        return Boolean.TRUE.equals(cleanBout.getPro()) ? NO : YES;
    }

    private static byte getIsTitleBout(CleanBout cleanBout) {
        return Boolean.TRUE.equals(cleanBout.getTitle()) ? YES : NO;
    }

    private static Long getProWinsCount(TreeSet<CleanBout> history) {
        return history.stream().filter(b -> b.getPro() && b.getWin()).count();
    }

    private static Byte yesNoIfNotNull(Object value) {
        return value != null ? YES : NO;
    }

    private static Byte getIsAgeKnown(FighterDetails fighterDetails) {
        return yesNoIfNotNull(fighterDetails.getDateOfBirth());
    }

    private static Byte getWinStreakKnown(FighterDetails fighterDetails) {
        return yesNoIfNotNull(fighterDetails.getDateOfBirth());
    }

    private static Byte getIsHeightKnown(FighterDetails fighterDetails) {
        return yesNoIfNotNull(getHeightCm(fighterDetails));
    }

    private static Float getDaysSincePriorFightDiff(CleanBout bout) {
        return Try.of(() -> {
            long fighterDaysSincePrior = ChronoUnit.DAYS.between(bout.getFighterPriorBout().getDate(), bout.getDate());
            long opponentDaysSincePrior = ChronoUnit.DAYS.between(bout.getOpponentPriorBout().getDate(), bout.getDate());
            return (Long.valueOf(fighterDaysSincePrior - opponentDaysSincePrior)).floatValue();

        }).getOrNull();
    }

    private static Float diff(Float x, Float y) {
        if (x != null & y != null) {
            return x - y;
        } else {
            return null;
        }
    }

    private static byte knownDiff(byte x, byte y) {
        return (byte) (x - y);
    }


    private static Float getAgeAtFightTime(FighterDetails fighter, CleanBout bout) {
        return Try.of(() -> (float) (ChronoUnit.DAYS.between(fighter.getDateOfBirth(), bout.getDate()) / 365.2425)).getOrNull();
    }

    private static Float getHeightCm(FighterDetails fighter) {
        if (fighter.getHeight() == null) return null;
        Matcher m = HEIGHT_CM_FORMAT.matcher(fighter.getHeight());
        if (m.find()) {
            var result = (Integer) Integer.parseInt(m.group(1));
            return result.floatValue();
        } else {
            return null;
        }
    }

    private static Integer getWinStreakDiff(CleanBout bout) {
        Integer fighterWinStreak = getWinStreakStartingFrom(bout.getFighterPriorBout());
        Integer opponentWinStreak = getWinStreakStartingFrom(bout.getOpponentPriorBout());

        return Try.of(() -> (fighterWinStreak - opponentWinStreak)).getOrNull();
    }

    private static byte getTitleExperienceDiff(CleanBout bout) {
        byte fighterHasTitleExp = getHasTitleExperience(bout.getFighterPriorBout());
        byte opponentHasTitleExp = getHasTitleExperience(bout.getOpponentPriorBout());

        return knownDiff(fighterHasTitleExp, opponentHasTitleExp);
    }

    private static Integer getWinStreakStartingFrom(CleanBout bout) {
        if (bout == null) return null;
        int streak = 0;
        CleanBout currentBout = bout;
        while (currentBout != null && currentBout.getWin()) {
            streak = streak + 1;
            currentBout = currentBout.getFighterPriorBout();
        }
        return streak;
    }

    private static byte getHasTitleExperience(CleanBout bout) {
        byte exp = NO;
        CleanBout currentBout = bout;
        while (currentBout != null) {
            if (currentBout.getTitle()) {
                exp = YES;
                break;
            }
            currentBout = currentBout.getFighterPriorBout();
        }
        return exp;
    }

    private static int getTotalFightsBeforeAndIncluding(CleanBout bout) {
        int count = 0;
        CleanBout currentBout = bout;
        while (currentBout != null) {
            count++;
            currentBout = currentBout.getFighterPriorBout();
        }
        return count;
    }

    private static int getProWinsBeforeAndIncluding(CleanBout bout) {
        int count = 0;
        CleanBout currentBout = bout;
        while (currentBout != null) {
            if (bout.getPro()) count++;
            currentBout = currentBout.getFighterPriorBout();
        }
        return count;
    }
}
