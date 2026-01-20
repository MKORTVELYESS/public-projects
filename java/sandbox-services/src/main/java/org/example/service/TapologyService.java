package org.example.service;

import java.sql.Types;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.example.domain.mma.FeatureWriter;
import org.example.entity.*;
import org.example.entity.mma.BoutFeatures;
import org.example.repository.BoutFeaturesRepository;
import org.example.repository.BoutRepository;
import org.example.repository.FighterDetailsRepository;
import org.example.repository.FighterRepository;
import org.example.util.FightDetailsExtractor;
import org.example.util.StandardDetailsExtractor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TapologyService {
    private static final Logger log = LoggerFactory.getLogger(TapologyService.class);
    private static final String FIGHTER_DETAILS_BASE_URL =
            "https://www.tapology.com/fightcenter/fighters/";
    private final HtmlFetchService htmlFetchService;
    private final ResolveDocumentService resolveDocumentService;
    private final FighterRepository fighterRepository;
    private final FighterDetailsRepository fighterDetailsRepository;
    private final BoutRepository boutRepository;
    private final BoutFeaturesRepository boutFeaturesRepository;
    private final JdbcTemplate jdbcTemplate;
    private final Pattern heightPattern = Pattern.compile("(\\d{3})\\s*cm");
    private final Pattern recordPattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
    private static final int BATCH_SIZE = 5_000;

    public TapologyService(
            HtmlFetchService htmlFetchService,
            ResolveDocumentService resolveDocumentService,
            FighterRepository fighterRepository,
            FighterDetailsRepository fighterDetailsRepository,
            BoutRepository boutRepository,
            BoutFeaturesRepository boutFeaturesRepository,
            JdbcTemplate jdbcTemplate) {
        this.htmlFetchService = htmlFetchService;
        this.resolveDocumentService = resolveDocumentService;
        this.fighterRepository = fighterRepository;
        this.fighterDetailsRepository = fighterDetailsRepository;
        this.boutRepository = boutRepository;
        this.boutFeaturesRepository = boutFeaturesRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Fighter> listFighters(String url) {
        String html = htmlFetchService.fetchHtml(url);
        Document document = resolveDocumentService.resolveDocument(html);
        Element tbody = resolveDocumentService.resolveTbody(document);
        return resolveDocumentService.resolveFightersTable(tbody);
    }

    public String printFighters(String url) {
        List<Fighter> fighters = listFighters(url);
        persistFighters(fighters);
        String result = fighters.stream().map(this::formatFighter).collect(Collectors.joining("\n"));
        // System.out.println(result);
        return result;
    }

    @Transactional
    public void persistFighters(List<Fighter> fighters) {
        fighterRepository.saveAll(fighters);
    }

    public List<Fighter> retrieveFighters() {
        return fighterRepository.findAll();
    }

    public void persistAllFighterDetails() {
        var alreadyHasDetails =
                new HashSet<>(
                        fighterDetailsRepository.findAll().stream().map(FighterDetails::getFighterId).toList());
        retrieveFighters().stream()
                .filter(f -> !alreadyHasDetails.contains(f.getId()))
                .parallel()
                .forEach(
                        this::persistFighterDetails);
    }

    public void persistFighterDetails(Fighter fighter) {
        String url = FIGHTER_DETAILS_BASE_URL.concat(fighter.getId());
        log.info("Built url: {}", url);
        String html = htmlFetchService.fetchHtml(url);
        log.info("Fetched: {}", url);
        Document document = resolveDocumentService.resolveDocument(html);
        log.info("Resolved: {}", url);
        FighterRawDetails raw = StandardDetailsExtractor.extract(document);
        log.info("Extracted raw fighter: {}", url);
        List<Bout> bouts = FightDetailsExtractor.extract(document, fighter.getId());
        log.info("Extracted bouts: {}", url);
        FighterDetails details = StandardDetailsExtractor.convert(raw, fighter.getId());
        log.info("Converted to persistable: {}", url);
        fighterDetailsRepository.save(details);
        log.info("Saved fighter details: {}", url);
        boutRepository.saveAll(bouts);
        log.info("Saved bouts: {}", url);
    }

    public void persistFeatures() {
        log.info("Will get title bouts");
        Set<String> titleBouts = getTitleBouts().keySet();
        log.info("Will delete bout features");
        boutFeaturesRepository.deleteAll();
        log.info("Done delete bout features");
        List<BoutFeatures> features = FeatureWriter.writeFeatures(boutRepository.findAll().stream().filter(f -> "mma".equals(f.getSport())).toList(), fighterDetailsRepository.findAll(), titleBouts);
        log.info("Features are now in memory, start persist");
        bulkInsert(features);
        saveInBatches(features);
    }

    public void getHeightWinRateCorrelationsByWeightGroup() {
        Map<String, List<Fighter>> grouped =
                retrieveFighters().stream().collect(Collectors.groupingBy(Fighter::getWeightClass));
        grouped.forEach(
                (label, grp) -> {
                    var rates = getCleanFighterHeightAndWinRate(grp);
                    var weightClass = new FighterWeightClass(label, rates);
                    var percentiles =
                            rates.stream()
                                    .map(
                                            r ->
                                                    new FighterHeightWinRatePercentile(
                                                            weightClass.getHeightPercentile(r),
                                                            weightClass.getWinRatePercentile(r)))
                                    .toList();
                    var heightPercentiles =
                            percentiles.stream()
                                    .mapToDouble(FighterHeightWinRatePercentile::heightPercentileWithinClass)
                                    .toArray();
                    var winRatePercentiles =
                            percentiles.stream()
                                    .mapToDouble(FighterHeightWinRatePercentile::winRatePercentileWithinClass)
                                    .toArray();

                    PearsonsCorrelation pc = new PearsonsCorrelation();
                    double correlation = pc.correlation(heightPercentiles, winRatePercentiles);
                    double p = pValue(correlation, percentiles.size());
                    System.out.printf(
                            "Correlation %.4f in group %s. The sample size is: %d P value: %.4f%n",
                            correlation, label, percentiles.size(), p);
                });
    }

    public static double pValue(double r, int n) {
        if (n < 3) return 1.0;

        double t = r * Math.sqrt((n - 2) / (1 - r * r));

        NormalDistribution normal = new NormalDistribution();
        return 2 * (1 - normal.cumulativeProbability(Math.abs(t)));
    }

    public List<FighterHeightWinRate> getCleanFighterHeightAndWinRate(List<Fighter> fighters) {
        return fighters.stream()
                .map((f) -> new FighterHeightWinRate(f.getId(), parseHeight(f), parseWinRate(f)))
                .filter(r -> r.getWinRate() != null)
                .filter(r -> r.getHeight() != null)
                .toList();
    }

    private Double parseWinRate(Fighter fighter) {
        if (fighter.getRecord() == null) {
            return null;
        }

        Matcher matcher = recordPattern.matcher(fighter.getRecord());

        if (!matcher.find()) {
            return null;
        }

        int wins = Integer.parseInt(matcher.group(1));
        int losses = Integer.parseInt(matcher.group(2));
        int draws = Integer.parseInt(matcher.group(3));

        int total = wins + losses + draws;
        if (total == 0) {
            return null;
        }

        return wins / (double) total;
    }

    private Integer parseHeight(Fighter fighter) {
        // log.info("Evaluation fighter: {}", fighter);
        if (fighter.getHeight() == null) {
            // log.info("Height null");
            return null;
        }

        Matcher matcher = heightPattern.matcher(fighter.getHeight());

        if (matcher.find()) {
            // log.info("found height: {}", matcher.group(1));
            return Integer.parseInt(matcher.group(1));
        }
        // log.info("Height regex not matched in {}", fighter.getHeight());
        return null;
    }

    private String formatFighter(Fighter fighter) {
        return String.format(
                "ID=%s | Name=%s | Height=%s | Class=%s | Record=%s | Nation=%s",
                fighter.getId(),
                fighter.getName(),
                fighter.getHeight(),
                fighter.getWeightClass(),
                fighter.getRecord(),
                fighter.getNation());
    }

    @Transactional
    public void saveInBatches(List<BoutFeatures> entities) {
        var withId = entities.stream().filter(e -> e.getId() != null).toList();
        log.info("Dropped: {} due to missing id", entities.size() - withId.size());
        for (int i = 0; i < withId.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, withId.size());
            List<BoutFeatures> batch = withId.subList(i, end);

            boutFeaturesRepository.saveAll(batch);
            boutFeaturesRepository.flush();
            log.info("Persisted {} to {} out of {}", i, end, withId.size());
        }
    }

    public Map<String, String> getTitleBouts() {
        List<Object[]> rows =
                boutRepository.findBoutDetailsByKey("Title Bout:");

        Map<String, String> result = new HashMap<>(rows.size() * 4 / 3);

        for (Object[] row : rows) {
            result.put((String) row[0], (String) row[1]);
        }

        return result;
    }

    public void bulkInsert(List<BoutFeatures> entities) {

        String sql = """
                INSERT INTO public.bout_features(
                    id, age_diff, age_known_diff, days_since_prior_fight_diff, elo_diff, fight_year, has_title_fight_experience_diff, height_diff, height_known_diff, is_amateur_fight, is_title_bout, prior_fight_known_diff, pro_wins_diff, target_win, total_fights_diff, win_streak_diff)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(
                sql,
                entities,
                BATCH_SIZE,
                (ps, e) -> {
                    ps.setString(1, e.getId());
                    ps.setObject(2, e.getAgeDiff(), Types.REAL);
                    ps.setObject(3, e.getAgeKnownDiff(), Types.TINYINT);
                    ps.setObject(4, e.getDaysSincePriorFightDiff(), Types.REAL);
                    ps.setObject(5, e.getEloDiff(), Types.REAL);
                    ps.setObject(6, e.getFightYear(), Types.INTEGER);
                    ps.setObject(7, e.getHasTitleFightExperienceDiff(), Types.TINYINT);
                    ps.setObject(8, e.getHeightDiff(), Types.REAL);
                    ps.setObject(9, e.getHeightKnownDiff(), Types.TINYINT);
                    ps.setObject(10, e.getIsAmateurFight(), Types.TINYINT);
                    ps.setObject(11, e.getIsTitleBout(), Types.TINYINT);
                    ps.setObject(12, e.getPriorFightKnownDiff(), Types.TINYINT);
                    ps.setObject(13, e.getProWinsDiff(), Types.BIGINT);
                    ps.setObject(14, e.getTargetWin(), Types.TINYINT);
                    ps.setObject(15, e.getTotalFightsDiff(), Types.INTEGER);
                    ps.setObject(16, e.getWinStreakDiff(), Types.INTEGER);
                }
        );
    }


}
