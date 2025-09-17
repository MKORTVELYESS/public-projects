package org.example.util;

import org.example.domain.jobs.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

public class NumberUtil {
    private static final Logger logger = LoggerFactory.getLogger(NumberUtil.class);

    public static Boolean isPrime(int input) {
        var result = true;
        if (input < 2) return false;
        for (int div = 2; div < Math.sqrt(input); div++) {
            if (input % div == 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static Integer factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) break; // there was not a single swap during going through the unsorted part of the arr, break
        }
        return arr;
    }

    public static double calculator(int a, int b, Character operation) {
        return switch (operation) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> (b != 0) ? (double) a / b : Double.NaN;
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }

    public static boolean sumOfTwoNumsYieldN(int n, int[] nums) {
        var seen = new HashSet<Integer>();
        logger.debug("Will iterate {} and find if the sum of two elements in arr can yield {}", Arrays.toString(nums), n);
        for (int i : nums) {
            var rest = n - i;
            logger.trace("i: {} rest: {} seen: {}", i, rest, seen);
            if (seen.contains(rest)) {
                logger.debug("SUCCESS: {} + {} = {}", i, rest, n);
                return true;
            }
            logger.trace("Adding {} to the set of seen elements ({})", i, seen);
            seen.add(i);
        }
        logger.debug("No two nums from {} can make up {}", nums, n);
        return false;
    }

}
