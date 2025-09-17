package org.example.util;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumbersTest {
    @Test
    void testIntegerCompare() {
        List<Integer> eodStockPrices = new ArrayList<>(List.of(100, 100, 123, 134, 134, 150,100));
        List<LocalDate> dates = LocalDate.of(2025, 1, 12).datesUntil(LocalDate.of(2025, 1, 19)).toList();

        eodStockPrices.sort(Comparator.naturalOrder());

        var consecutiveEqualDaysMax = 0;
        var consecutiveEqualDays = 1;
        Iterator<Integer> it = eodStockPrices.listIterator();
        Integer prev;
        Integer next = null;
        while (it.hasNext()) {
            prev = next;
            next = it.next();
            if (next.equals(prev)) { // This is a consecutive day
                consecutiveEqualDays += 1;
            } else { // This is a non-consecutive day
                consecutiveEqualDaysMax = Math.max(consecutiveEqualDays,consecutiveEqualDaysMax);
                consecutiveEqualDays = 1;
            }
        }
        consecutiveEqualDaysMax = Math.max(consecutiveEqualDays,consecutiveEqualDaysMax);
        assertEquals(3, consecutiveEqualDaysMax);
    }

    @Test
    void testIntegerCompare2() {

        List<Integer> eodStockPrices = new ArrayList<>(List.of(100, 100, 123, 134, 134, 150,100));
        List<LocalDate> dates =
        List.of(LocalDate.parse("2025-01-16"),
                LocalDate.parse("2025-01-15"),
                LocalDate.parse("2025-01-14"),
                LocalDate.parse("2025-01-13"),
                LocalDate.parse("2025-01-18"),
                LocalDate.parse("2025-01-12"),
                LocalDate.parse("2025-01-17"));

        record Pair<Integer, LocalDate>(Integer price, LocalDate date){}

        List<LocalDate> sortedDates = new ArrayList<>(dates.size()+1);
        sortedDates.addAll(dates);
        Collections.sort(sortedDates);

        List<Pair<Integer, LocalDate>> zipped = new ArrayList<>(sortedDates.size());
        for (LocalDate date:sortedDates) {
            zipped.add(new Pair<>(eodStockPrices.get(dates.indexOf(date)),date));
        }

        var consecutiveEqualDaysMax = 0;
        var consecutiveEqualDays = 1;
        Iterator<Pair<Integer, LocalDate>> it = zipped.listIterator();
        Integer prev;
        Integer next = null;
        while (it.hasNext()) {
            prev = next;
            next = it.next().price;
            if (next.equals(prev)) { // This is a consecutive day
                consecutiveEqualDays += 1;
            } else { // This is a non-consecutive day
                consecutiveEqualDaysMax = Math.max(consecutiveEqualDays,consecutiveEqualDaysMax);
                consecutiveEqualDays = 1;
            }
        }
        consecutiveEqualDaysMax = Math.max(consecutiveEqualDays,consecutiveEqualDaysMax);
        assertEquals(3, consecutiveEqualDaysMax);
    }
}
