package org.example.util;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchUtil {
  // Searches through our inputs and returns the subset of the inputs which contain at least one of
  // our keywords
  public static List<String> search(List<String> keywords, List<String> inputs) {
    return inputs.parallelStream()
        .filter(input -> keywords.stream().anyMatch(input::contains))
        .collect(Collectors.toList());
  }

  // Returns true if we have at least one keyword which does not match any of our inputs, false
  // otherwise
  public static Boolean unmatchedStudentExists(List<String> students, List<String> files) {
    return students.stream()
        .anyMatch(student -> files.stream().noneMatch(file -> file.contains(student)));
  }

  // Takes a set of car names, and matches them to their brand using a mapping. The mapping is an
  // ordered map from car name matcher to brand name
  public static Map<String, String> matchCarsToBrands(
      HashSet<String> carNames, LinkedHashMap<String, String> nameMatcherToBrandMap) {
    return nameMatcherToBrandMap.entrySet().stream()
        .flatMap(
            e ->
                carNames.stream()
                    .filter(c -> c.contains(e.getKey()))
                    .map(c -> Map.entry(c, e.getValue())))
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (existing, newValue) -> existing));
  }

  // Takes a set of car names, and matches them to their brand using a mapping. The mapping is an
  // ordered map from car name matcher to brand name
  public static Map<String, String> matchCarsToBrandsRecursively(
      HashSet<String> carNames, String matcher, String brand, Map<String, String> accumulator) {

    return carNames.stream()
        .filter(c -> c.contains(matcher))
        .collect(Collectors.toMap(c -> c, c -> brand));
  }
}
