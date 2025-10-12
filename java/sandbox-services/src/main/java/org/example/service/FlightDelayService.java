package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlightDelayService {

  private static final Logger log = LoggerFactory.getLogger(FlightDelayService.class);
  private static final char SEPARATOR = ',';
  private static final DoubleSupplier DEFAULT_DELAY = () -> 0;
  private static final int DEPARTURE_DELAY_COLUMN_IDX = 15;
  private static final int FROM_AIRPORT_COLUMN_IDX = 7;
  private static final int TO_AIRPORT_COLUMN_IDX = 10;

  public FlightDelayService(@Value("${flight-delay.filepath}") String dataSourcePath) {
    this.dataSourcePath = dataSourcePath;
  }

  final String dataSourcePath;

  public Double getMaxDelayOnRoute(String from, String to) throws IOException {
    try (final Stream<String> lines = Files.lines(Path.of(dataSourcePath))) {
      log.info("Scanning datasource: {}", dataSourcePath);
      return lines
          .parallel()
          .filter(
              line -> {
                String[] parsedCols =
                    parseContentOfColumns(
                        line,
                        Set.of(
                            FROM_AIRPORT_COLUMN_IDX,
                            TO_AIRPORT_COLUMN_IDX,
                            DEPARTURE_DELAY_COLUMN_IDX));
                return parsedCols.length == 3
                    && from.equals(parsedCols[0])
                    && to.equals(parsedCols[1])
                    && !"".equals(parsedCols[2]);
              })
          .mapToDouble(
              l ->
                  Double.parseDouble(
                      parseContentOfColumns(l, Set.of(DEPARTURE_DELAY_COLUMN_IDX))[0]))
          .max()
          .orElseGet(DEFAULT_DELAY);
    } catch (Exception e) {
      log.error("Error getting max flight delay", e);
      throw e;
    }
  }

  String[] parseContentOfColumns(String line, Set<Integer> columnIndices) {
    StringBuilder result = new StringBuilder();
    StringBuilder currentColumn = new StringBuilder();
    int countQuote = 0;

    int maxColumnIndex =
        columnIndices.stream().mapToInt(Integer::intValue).max().orElseGet(() -> 0);
    int currentIndex = 0;

    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);

      if (columnIndices.contains(currentIndex)) {
        currentColumn.append(c);
      }

      if (c == FlightDelayService.SEPARATOR && (countQuote & 1) == 0) {
        if (!currentColumn.isEmpty()) {
          result.append(currentColumn);
          currentColumn.setLength(0);
        }
        currentIndex++;
        if (currentIndex > maxColumnIndex) break;
      }

      if (c == '"') countQuote++;
    }

    result.append(currentColumn);
    return result.toString().split(String.valueOf(FlightDelayService.SEPARATOR));
  }
}
