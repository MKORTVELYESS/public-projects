package org.example.domain.jobs;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.example.domain.jobs.model.Box;
import org.example.domain.jobs.model.Cmd;
import org.example.domain.jobs.model.Fw;
import org.example.domain.jobs.model.Job;
import org.example.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFactory {

  private static final Logger logger = LoggerFactory.getLogger(JobFactory.class);

  private static final Set<JilAttributeKey> attributeDuplicationAllowList =
      Set.of(JilAttributeKey.envvars);

  public static Job fromInsertJobJil(String jil) {
    final var parts = extractParts(jil);
    final var keys = extractAttributeKeys(parts);
    final var parsedKeys = keys.stream().map(JilAttributeKey::valueOf).collect(Collectors.toList());
    final var matchers = generateValueMatchersForKeysInJil(parsedKeys, jil);
    final var values = extractValues(jil, matchers);
    final var keyValueMap = mapKeysToValues(parsedKeys, values);
    final var jobName = findInsertJob(keyValueMap);
    final var jobType = getJobType(keyValueMap);
    logger.info("Start create {} job with name {}", jobType, jobName);
    return switch (jobType) {
      case JobType.CMD -> new Cmd(keyValueMap);
      case JobType.BOX -> new Box(keyValueMap);
      case JobType.FW -> new Fw(keyValueMap);
      default -> throw new IllegalArgumentException("Job type " + jobType + " not implemented yet");
    };
  }

  private static JobType getJobType(Map<JilAttributeKey, String> keyValueMap) {
    return JobType.valueOf(keyValueMap.getOrDefault(JilAttributeKey.job_type, JobType.CMD.name()));
  }

  private static String findInsertJob(Map<JilAttributeKey, String> keyValueMap) {
    Stream<JilAttributeKey> validSubcommands = Stream.of(JilAttributeKey.insert_job);
    Function<JilAttributeKey, Stream<? extends String>> extractJobNameFromSubcommand =
        key -> Optional.ofNullable(keyValueMap.get(key)).stream();
    List<String> candidateJobNames =
        validSubcommands.flatMap(extractJobNameFromSubcommand).toList();

    if (candidateJobNames.size() != 1)
      throw new IllegalArgumentException(
          "A job should have exactly one subcommand. Found: "
              + candidateJobNames.size()
              + "\nSupported subcommands: "
              + validSubcommands);

    return candidateJobNames.getFirst();
  }

  private static String[] extractParts(String jil) {
    return jil.split(":");
  }

  private static List<String> extractAttributeKeys(String[] parts) {
    return Arrays.stream(Arrays.copyOf(parts, parts.length - 1))
        .map(
            line -> {
              String[] words = line.strip().split(" ");
              return words[words.length - 1];
            })
        .toList();
  }

  private static List<Matcher> generateValueMatchersForKeysInJil(
      List<JilAttributeKey> keys, String jil) {
    List<Matcher> matchers = new ArrayList<>();

    int size = keys.size();
    for (int i = 0; i < size; i++) {
      int currentIndex = i;
      int nextIndex = currentIndex + 1;
      boolean isNextWithinBounds = nextIndex < size;

      JilAttributeKey current = keys.get(currentIndex);
      String next = isNextWithinBounds ? keys.get(nextIndex).name() : "$";
      String regex = "(?s)" + current + "\\s*" + ":" + "(?<value>.*?)" + next;

      matchers.add(Pattern.compile(regex).matcher(jil));
    }

    return matchers;
  }

  private static List<String> extractValues(String jil, List<Matcher> matchers) {
    return matchers.stream()
        .map(
            matcher -> {
              if (matcher.find()) {
                return matcher.group("value").strip();
              } else {
                logger.error(jil);
                throw new IllegalArgumentException(
                    "Please inspect jil as we are not able to match with regex");
              }
            })
        .toList();
  }

  private static Map<JilAttributeKey, String> mapKeysToValues(
      List<JilAttributeKey> keys, List<String> values) {
    ensureEachKeyHasValue(keys, values);
    ensureNoIllegalDuplicateAttributes(keys);

    logger.info("Start building jil attributes as key value map");

    return IntStream.range(0, keys.size())
        .boxed()
        .collect(
            Collectors.toMap(keys::get, values::get, (first, second) -> first + ", " + second));
  }

  private static void ensureNoIllegalDuplicateAttributes(List<JilAttributeKey> keys) {
    var duplicateAttributes = ListUtils.findDuplicates(keys);
    var allDuplicatesValuesAllowed = attributeDuplicationAllowList.containsAll(duplicateAttributes);
    if (!allDuplicatesValuesAllowed) {
      String message =
          "Illegal duplication in: "
              + duplicateAttributes
              + "\n Only "
              + attributeDuplicationAllowList
              + " can be duplicated in jil";
      logger.error(message);
      throw new IllegalStateException(message);
    }
    logger.info("No illegal duplications found");
  }

  private static void ensureEachKeyHasValue(List<JilAttributeKey> keys, List<String> values) {
    if (keys.size() != values.size()) {
      String parsedKeysAndValues = "Keys parsed: " + keys + "\n Values parsed: " + values;
      String message =
          "Attribute keys size: "
              + keys.size()
              + " != Attribute values size: "
              + values.size()
              + "\n"
              + parsedKeysAndValues;
      logger.error(message);
      throw new IllegalArgumentException(message);
    }
    logger.info("Each key has a corresponding value");
  }
}
