package org.example;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCompareUtil {

  private static final Logger log = LoggerFactory.getLogger(JsonCompareUtil.class);

  public static final ObjectMapper mapper =
      JsonMapper.builder()
          .addModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .build();

  public static <T> boolean compareJsonAndList(String json, List<T> list) {
    try {
      JsonNode jsonNodeFromString = normalizeNumbers(mapper.readTree(json));
      JsonNode jsonNodeFromList = normalizeNumbers(mapper.valueToTree(list));

      if (jsonNodeFromString.equals(jsonNodeFromList)) {
        return true;
      }

      logDiff("", jsonNodeFromList, jsonNodeFromString);
      return false;

    } catch (Exception e) {
      log.error("JSON comparison failed", e);
      return false;
    }
  }

  private static void logDiff(String path, JsonNode expected, JsonNode actual) {
    if (expected == null && actual != null) {
      log.warn("Extra node at {} : {}", path, actual);
      return;
    }
    if (expected != null && actual == null) {
      log.warn("Missing node at {} : {}", path, expected);
      return;
    }

    if (!expected.getNodeType().equals(actual.getNodeType())) {
      log.warn(
          "Type mismatch at {} : expected={}, actual={}",
          path,
          expected.getNodeType(),
          actual.getNodeType());
      return;
    }

    switch (expected.getNodeType()) {
      case OBJECT -> {
        ObjectNode expObj = (ObjectNode) expected;
        ObjectNode actObj = (ObjectNode) actual;

        for (Map.Entry<String, JsonNode> entry : expObj.properties()) {
          String field = entry.getKey();
          logDiff(path + "/" + field, entry.getValue(), actObj.get(field));
        }

        Iterator<String> actFields = actObj.fieldNames();
        while (actFields.hasNext()) {
          String field = actFields.next();
          if (!expObj.has(field)) {
            log.warn("Extra field at {}{} : {}", path, "/" + field, actObj.get(field));
          }
        }
      }
      case ARRAY -> {
        int max = Math.max(expected.size(), actual.size());
        for (int i = 0; i < max; i++) {
          logDiff(
              path + "[" + i + "]",
              expected.path(i).isMissingNode() ? null : expected.get(i),
              actual.path(i).isMissingNode() ? null : actual.get(i));
        }
      }
      default -> {
        if (expected.isNumber() && actual.isNumber()) {
          if (expected.decimalValue().compareTo(actual.decimalValue()) != 0) {
            log.warn("Numeric mismatch at {} : expected={}, actual={}", path, expected, actual);
          }
        } else if (!expected.equals(actual)) {
          log.warn("Value mismatch at {} : expected={}, actual={}", path, expected, actual);
        }
      }
    }
  }

  private static JsonNode normalizeNumbers(JsonNode node) {
    if (node == null) return null;

    if (node.isNumber()) {
      return LongNode.valueOf(node.longValue());
    }

    if (node.isObject()) {
      ObjectNode obj = JsonNodeFactory.instance.objectNode();
      node.properties()
          .iterator()
          .forEachRemaining(e -> obj.set(e.getKey(), normalizeNumbers(e.getValue())));
      return obj;
    }

    if (node.isArray()) {
      ArrayNode arr = JsonNodeFactory.instance.arrayNode();
      node.forEach(n -> arr.add(normalizeNumbers(n)));
      return arr;
    }

    return node;
  }
}
