package org.example.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static final String WORD_SPLITTER = " ";

    public static String lastWordOf(String line) {
        var words = line.strip().split(WORD_SPLITTER);
        var lastIndex = words.length - 1;
        return words[lastIndex];
    }

    public static List<String> breakDown(String splittable, String splitter) {
        List<String> result = new ArrayList<>();

        int crntIdx = 0;
        int nextIdx = splittable.indexOf(splitter, crntIdx + 1);
        while (nextIdx != -1) {
            result.add(splittable.substring(crntIdx, nextIdx));
            crntIdx = nextIdx;
            nextIdx = splittable.indexOf(splitter, crntIdx + 1);
        }
        result.add(splittable.substring(crntIdx));

        return result;
    }

    public static String reverse(String input) {
        var size = input.length();
        var result = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            result.append(input.charAt(i));
        }

        return result.toString();
    }
}
