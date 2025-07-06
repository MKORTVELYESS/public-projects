package org.example.domain.jobs;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.example.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JilValidator {
  private static final Logger logger = LoggerFactory.getLogger(JilValidator.class);

  private static final Set<JilAttributeKey> attributeDuplicationAllowList =
      Set.of(JilAttributeKey.envvars);

  public static String getJobName(Map<JilAttributeKey, String> keyValueMap) {
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

  public static JobType getJobType(Map<JilAttributeKey, String> keyValueMap) {
    return JobType.valueOf(keyValueMap.getOrDefault(JilAttributeKey.job_type, JobType.CMD.name()));
  }

  public static void ensureNoIllegalDuplicateAttributes(List<JilAttributeKey> keys) {
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

  public static void ensureEachKeyHasValue(List<JilAttributeKey> keys, List<String> values) {
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

  static void runValidation(List<JilAttributeKey> keyList, List<String> valueList) {
    ensureNoIllegalDuplicateAttributes(keyList);
    ensureEachKeyHasValue(keyList, valueList);
  }
}
