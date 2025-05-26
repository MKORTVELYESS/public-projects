package org.example.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JobFactory {

    private static final Logger logger = LoggerFactory.getLogger(JobFactory.class);

    public static Job fromJil(String jil) {
        var parts = extractParts(jil);
        var keys = extractAttributeKeys(parts);
        var matchers = generateAttributeMatchersFromKeysAndJil(keys,jil);
        var values = matchForValuesInJil(jil,matchers);
        var attributes = buildJilAttributes(keys,values);
        return new Job(attributes);
    };


    private static String[] extractParts(String jil) {
        return jil.split(":");
    }

    private static List<String> extractAttributeKeys(String[] parts) {
        return Arrays.stream(Arrays.copyOf(parts, parts.length - 1))
                .map(
                        line -> {
                            String[] words = line.strip().split(" ");
                            return words[words.length - 1];
                        })
                .toList();
    }

    private static List<Matcher> generateAttributeMatchersFromKeysAndJil(List<String> keys, String jil) {
        List<Matcher> matchers = new ArrayList<>();

        int size = keys.size();
        for (int i = 0; i < size; i++) {
            int currentIndex = i;
            int nextIndex = currentIndex + 1;

            String current = keys.get(currentIndex);

            boolean isNextWithinBounds = nextIndex < size;
            String next = isNextWithinBounds ? keys.get(nextIndex) : "$";

            String regex = "(?s)" + current + "\\s*" + ":" + "(?<value>.*?)" + next;
            matchers.add(Pattern.compile(regex).matcher(jil));
        }

        return matchers;
    }

    private static List<String> matchForValuesInJil(String jil, List<Matcher> matchers) {
        return matchers.stream()
                .map(
                        matcher -> {
                            if (matcher.find()) {
                                return matcher.group("value").strip();
                            } else {
                                logger.error(jil);
                                throw new RuntimeException(
                                        "Please inspect jil as we are not able to match with regex");
                            }
                        })
                .toList();
    }

    private static Map<String, String> buildJilAttributes(List<String> keys, List<String> values) {

        if (keys.size() == values.size()) {
            logger.info(
                    "Jil keys and values are equal in size, can continue matching attributes with their values");
        } else {
            throw new RuntimeException(
                    "Please inspect jil as we found a different number of attribute keys compared to attribute values");
        }

        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

}
