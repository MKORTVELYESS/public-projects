package org.example.util;

import java.util.List;
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
}
