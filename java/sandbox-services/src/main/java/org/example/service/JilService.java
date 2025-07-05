package org.example.service;

import java.util.Iterator;
import java.util.List;
import org.example.domain.jobs.JilAttributeKey;
import org.example.domain.jobs.JobFactory;
import org.example.util.ListUtils;
import org.springframework.stereotype.Service;

@Service
public class JilService {

  public void printJobs(String monolithicJil) {
    var jils = enlistJils(monolithicJil);
    var jobs = jils.stream().map(JobFactory::fromInsertJobJil);
    jobs.forEach(System.out::println);
  }

  public List<String> enlistJils(String monolithicJil) {
    var splitters = JilAttributeKey.streamSubcommands().toList().iterator();
    List<String> result = List.of(monolithicJil);
    while (splitters.hasNext()) {
      result = splitJil(result.iterator(), splitters.next(), List.of());
    }
    return result;
  }

  public List<String> splitJil(
      Iterator<String> inputs, JilAttributeKey splitter, List<String> results) {
    if (inputs.hasNext()) {
      var brokenJils = breakBefore(inputs.next(), splitter.toString(), List.of());
      return splitJil(inputs, splitter, ListUtils.concatLists(brokenJils, results));
    } else {
      return results;
    }
  }

  public List<String> breakBefore(String stringToSplit, String splitter, List<String> results) {
    final var indexToBreakTheInputAt = stringToSplit.indexOf(splitter, 1);
    boolean splitterNotFound = indexToBreakTheInputAt == -1;

    if (splitterNotFound) {
      return ListUtils.newImmutableListFrom(stringToSplit, results).reversed();
    } else {
      final var frst = stringToSplit.substring(0, indexToBreakTheInputAt);
      final var rest = stringToSplit.substring(indexToBreakTheInputAt);
      return breakBefore(rest, splitter, ListUtils.newImmutableListFrom(frst, results));
    }
  }
}
