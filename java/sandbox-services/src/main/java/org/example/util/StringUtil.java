package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

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
        logger.info("Reversing {}", input);
        var size = input.length();
        logger.debug("Length of {} is {}", input, size);
        var result = new StringBuilder();
        logger.trace("Initialized new StringBuilder to collect characters in reverse. " +
                "Will iterate and append to builder in reverse order");
        for (int i = size - 1; i >= 0; i--) {
            char current = input.charAt(i);
            logger.trace("Will append character at position {}: {}", i, current);
            result.append(current);
            logger.trace("Done append. Current value of the builder is: {}", result);
        }
        logger.info("Finished reverse iteration. Returning reversed result...");
        return result.toString();
    }

    public static boolean isPalindrome(String input) {
        logger.info("Checking palindrom {}", input);
        var size = input.length();
        logger.debug("Length of {} is {}", input, size);
        var isEven = size % 2 == 0;
        logger.debug("Length of input is even: {}", isEven);
        var arr = input.toCharArray();
        logger.trace("Input string broken down to array: {}", Arrays.toString(arr));
        var mid = size / 2;
        logger.debug("The mid of this array is at {}", mid);

        char[] first = Arrays.copyOfRange(arr, 0, mid);
        logger.trace("Created first sub-array from 0 up to but not including index {}", mid);

        int restSubArrayStart = mid + (isEven ? 0 : 1);
        char[] rest = Arrays.copyOfRange(arr, restSubArrayStart, size);
        logger.trace("Created rest sub-array from {} up to but not including index {}", restSubArrayStart, size);

        logger.info("Broken down array to first and rest... Skipping mid if not even\nFirst: {}\nRest: {}", Arrays.toString(first), Arrays.toString(rest));

        mid--; // 0 based indexing
        for (int i = 0; i <= mid; i++) {
            logger.trace("Trying to find inequality of first[{}] != rest[{}] ({} != {})?", i, mid - i, first[i], rest[mid - i]);
            if (first[i] != rest[mid - i]) {
                logger.info("Compared elements {} and {} are not equal. This is not a palindrom. Returning false...", first[i], rest[mid - i]);
                return false;
            }
        }
        logger.info("This is a palindrome as no character pair broke equality pattern. Returning true...");
        return true;
    }


}
