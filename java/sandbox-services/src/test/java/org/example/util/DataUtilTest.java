package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DataUtilTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void countValues() throws IOException {
    var jsonPath = Path.of("src/test/resources/data/complex-template-test.json");
    var json = Files.readString(jsonPath, StandardCharsets.UTF_8);

    Map<String, Object> data = objectMapper.readValue(json, Map.class);

    var actual = DataUtil.countValues(data);
    var expected = 43;
    assertEquals(expected, actual);
  }
}
