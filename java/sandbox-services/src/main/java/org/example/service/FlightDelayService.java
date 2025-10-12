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
                    columnExtractor(
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
              l -> Double.parseDouble(columnExtractor(l, Set.of(DEPARTURE_DELAY_COLUMN_IDX))[0]))
          .max()
          .orElseGet(DEFAULT_DELAY);
    } catch (Exception e) {
      log.error("Error getting max flight delay", e);
      throw e;
    }
  }

  String[] columnExtractor(String parsable, Set<Integer> extractables) {
    StringBuilder result = new StringBuilder();
    StringBuilder cell = new StringBuilder();
    int countQuote = 0;

    int lastExtractableColumn =
        extractables.stream().mapToInt(Integer::intValue).max().orElseGet(() -> 0);
    int currColIdx = 0;

    for (int i = 0; i < parsable.length(); i++) {
      char currentCharacter = parsable.charAt(i);

      boolean isExtractableColumn = extractables.contains(currColIdx);
      if (isExtractableColumn) {
        cell.append(currentCharacter);
      }

      boolean notQuoted = (countQuote & 1) == 0;
      boolean isSeparator = currentCharacter == FlightDelayService.SEPARATOR;
      boolean shouldStartNewColumn = isSeparator && notQuoted;
      if (shouldStartNewColumn) {
        result.append(cell); // save results of this column
        cell.setLength(0); // new column start --> reset cell to ""
        currColIdx++;
        boolean passedAllExtractableColumns = currColIdx > lastExtractableColumn;
        if (passedAllExtractableColumns) break;
      }

      boolean isQuote = currentCharacter == '"';
      if (isQuote) countQuote++;
    }

    result.append(
        cell); // case when loop is never broken because the last column is included in the
    // extractables
    return result.toString().split(String.valueOf(FlightDelayService.SEPARATOR));
  }
}
