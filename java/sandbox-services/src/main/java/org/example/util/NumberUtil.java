package org.example.util;

import java.util.Arrays;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberUtil {
    private static final Logger logger = LoggerFactory.getLogger(NumberUtil.class);

    public static Boolean isPrime(int input) {
        logger.info("Checking if {} is prime by trying to find a divisor", input);
        var result = true;
        logger.info("Consider {} to be prime until divisor found", input);
        if (input < 2) {
            logger.info("Input({}) is smaller than 2, and cannot be prime. Returning...", input);
            return false;
        }
        logger.info(
                "Will check if there is any integer div that divides input evenly until the upper bound of {}",
                Math.sqrt(input));
        for (int div = 2; div < Math.sqrt(input); div++) {
            logger.trace("{} % {} = {}", input, div, input % div);
            if (input % div == 0) {
                logger.debug("{} divides {} evenly --> {} is not a prime. Breaking...", div, input, input);
                result = false;
                logger.debug("Set result = false to not consider this number prime anymore");
                break;
            }
        }
        logger.info(
                "Upper bound reached or divisor found... Finished checking divisors. Returning current view of primeness: {}",
                result);
        return result;
    }

    public static Boolean isPrime(long input) {
        var result = true;
        if (input < 2) {
            return false;
        }

        for (int div = 2; div <= Math.sqrt(input); div++) {
            if (input % div == 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static Integer factorial(int n) {
        logger.debug("Calculating factorial of {}", n);
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        logger.info("Will bubble sort array of size {}: {}", n, Arrays.toString(arr));
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            logger.debug(
                    "\n***\nStart sorting sub-array. From index {} to {} Sub-array: {}\n***",
                    i,
                    n - i,
                    Arrays.toString(Arrays.copyOf(arr, n - i)));
            for (int j = 0; j < n - 1 - i; j++) {
                logger.trace("Current element arr[{}] = {}", j, arr[j]);
                logger.trace("Next    element arr[{}] = {}", j + 1, arr[j + 1]);
                if (arr[j] > arr[j + 1]) {
                    logger.trace(
                            "Current({}) > Next({}) --> This pair is NOT currently sorted to increasing order. Start swapping value with next value now.",
                            arr[j],
                            arr[j + 1]);
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                    logger.trace("Done swapping: {}", Arrays.toString(Arrays.copyOf(arr, n - i)));
                }
            }
            logger.debug("Array after sub-array sort: {}", Arrays.toString(arr));
            logger.debug("Swap needed during the sort of the sub-array: {}", swapped);
            if (!swapped) { // there was not a single swap during going through the unsorted part of the
                // arr, break
                logger.debug("Sub-array already sorted, breaking...");
                break;
            }
        }
        logger.info("Done sorting: {}", Arrays.toString(arr));
        return arr;
    }

    public static double calculator(int a, int b, Character operation) {
        var result =
                switch (operation) {
                    case '+' -> a + b;
                    case '-' -> a - b;
                    case '*' -> a * b;
                    case '/' -> (b != 0) ? (double) a / b : Double.NaN;
                    default -> throw new IllegalArgumentException("Invalid operation");
                };

        logger.info("{} {} {} = {}", a, operation, b, result);
        return result;
    }

    public static boolean sumOfTwoNumsYieldN(int n, int[] nums) {
        var seen = new HashSet<Integer>();
        logger.debug(
                "Will iterate {} and find if the sum of two elements in arr can yield {}",
                Arrays.toString(nums),
                n);
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

    public static int max(int[] nums) {
        logger.info("Start find max from {}", Arrays.toString(nums));
        int r = Integer.MIN_VALUE;
        logger.debug("Initialized r to {}", r);
        logger.info("Will iterate nums and reassign r if larger num found");
        for (int n : nums) {
            logger.trace("Curren n: {} Current max: {}", n, r);
            if (n > r) {
                logger.debug("{} > {}, found new max as {}", n, r, n);
                r = n;
            }
        }
        return r;
    }

    public static int max2(int[] nums) {
        logger.info("Start find max from {}", Arrays.toString(nums));
        int max = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        logger.debug("Initialized max to {} and max2 to {}", max, max2);
        logger.info("Will iterate nums and reassign max if larger num found");
        for (int n : nums) {
            logger.trace("Curren n: {} Current max: {}", n, max);
            if (n > max) {
                logger.debug("{} > {}, found new max as {}...", n, max, n);
                logger.debug("Will demote prior max to max2 {} = {}", max2, max);
                max2 = max;
                logger.debug("Will assign new max {} = {}", max, n);
                max = n;
            } else if (n > max2) {
                logger.debug("{} > {}, found new max2 as {}...", n, max2, n);
                max2 = n;
            }
        }
        return max2;
    }
}
