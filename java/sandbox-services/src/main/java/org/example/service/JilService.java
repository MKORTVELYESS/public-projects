package org.example.service;

import static org.example.util.StringUtil.breakDown;

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
      result = splitJil(result.iterator(), splitters.next());
    }
    return result;
  }

  /**
   * Breaks down further a list that has been already broken down
   *
   * @param inputs = broken down jils to break down further
   * @param splitter = jil subcommand to break down by
   * @return --> accumulates and flattens the inputs iterator and the newly broken down list
   */
  public List<String> splitJil(Iterator<String> inputs, JilAttributeKey splitter) {
    List<String> accumulator = new ArrayList<>();

    while (inputs.hasNext()) {
      var brokenJils = breakDown(inputs.next(), splitter.toString());
      accumulator = ListUtils.concatLists(accumulator, brokenJils);
    }

    return accumulator;
  }
}
