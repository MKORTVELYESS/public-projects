package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.example.domain.jobs.JilAttributeKey;
import org.example.util.StringUtil;
import org.junit.jupiter.api.Test;

class JilServiceTest {
  JilService service = new JilService();

  @Test
  void splitBeforeFirstOccurrenceOf() {
    final var input = "insert_job: first insert_job: second insert_job: third";
    final var actual = StringUtil.breakDown(input, "insert_job");
    final var expected = List.of("insert_job: first ", "insert_job: second ", "insert_job: third");
    assertEquals(expected, actual);
  }

  @Test
  void splitJil() {
    final var input = "insert_job: first insert_job: second insert_job: third";
    final var listOfInputs = List.of(input, input, input);
    final var actual = service.splitJil(listOfInputs.iterator(), JilAttributeKey.insert_job);
    final var expected =
        List.of(
            "insert_job: first ",
            "insert_job: second ",
            "insert_job: third",
            "insert_job: first ",
            "insert_job: second ",
            "insert_job: third",
            "insert_job: first ",
            "insert_job: second ",
            "insert_job: third");
    assertEquals(expected, actual);
  }

  @Test
  void enlistJils() {
    final var input = "insert_job: first delete_job: second update_job: third insert_job: fourth";
    final var actual = service.enlistJils(input);
    final var expected =
        List.of(
            "insert_job: first ",
            "delete_job: second ",
            "update_job: third ",
            "insert_job: fourth");
    assertEquals(expected, actual);
  }
}
