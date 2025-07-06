package org.example.service;

import java.util.ArrayList;
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
      var brokenJils = breakDown(inputs.next(), splitter.toString());
      return splitJil(inputs, splitter, ListUtils.concatLists(brokenJils, results));
    } else {
      return results;
    }
  }

  public List<String> breakDown(String splittable, String splitter) {
    List<String> result = new ArrayList<>();

    int crntIdx = 0;
    int nextIdx = splittable.indexOf(splitter, crntIdx + 1);
    while (nextIdx != -1) {
      result.add(splittable.substring(crntIdx, nextIdx));
      crntIdx = nextIdx;
      nextIdx = splittable.indexOf(splitter, crntIdx + 1);
    }
    result.add(splittable.substring(crntIdx));

    return result;
  }
}
