package org.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {
    public static <T> Stream<List<T>> combinationsAsStream(List<T> input, int k) {
        return combinationStream(input, k, 0, new ArrayList<>());
    }

    private static <T> Stream<List<T>> combinationStream(List<T> input, int k, int start, List<T> current) {
        if (current.size() == k) {
            return Stream.of(new ArrayList<>(current));
        }

        if (start >= input.size()) {
            return Stream.empty();
        }

        return IntStream.range(start, input.size()).boxed()
                .flatMap(i -> {
                    current.add(input.get(i));
                    Stream<List<T>> stream = combinationStream(input, k, i + 1, current);
                    current.removeLast();
                    return stream;
                });
    }
}
