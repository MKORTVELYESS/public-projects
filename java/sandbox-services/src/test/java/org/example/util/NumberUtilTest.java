package org.example.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest {

    @Test
    void isPrime() {
        var num1 = 983;
        var num2 = 797;
        var num3 = 389;
        var num4 = 388;
        var num5 = 2;
        var num6 = 1;
        var num7 = 0;
        var num8 = -1;

        assertEquals(true, NumberUtil.isPrime(num1));
        assertEquals(true, NumberUtil.isPrime(num2));
        assertEquals(true, NumberUtil.isPrime(num3));
        assertEquals(false, NumberUtil.isPrime(num4));
        assertEquals(true, NumberUtil.isPrime(num5));
        assertEquals(false, NumberUtil.isPrime(num6));
        assertEquals(false, NumberUtil.isPrime(num7));
        assertEquals(false, NumberUtil.isPrime(num8));

    }

    @Test
    void factorial() {
        var expected = 362880;
        var n = 9;

        assertEquals(expected, NumberUtil.factorial(n));

    }

    @Test
    void bubbleSort() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] exp = {1, 2, 3, 4, 5};

        assertArrayEquals(exp, NumberUtil.bubbleSort(arr));
    }

    @Test
    void calcuatorTest() {
        assertEquals(5.0, NumberUtil.calculator(2, 3, '+'));
        assertEquals(20.0, NumberUtil.calculator(5, 4, '*'));
        assertEquals(2.5, NumberUtil.calculator(5, 2, '/'));
        assertEquals(3.0, NumberUtil.calculator(5, 2, '-'));
        assertThrows(IllegalArgumentException.class, () -> NumberUtil.calculator(5, 2, 'x'));
    }

    @Test
    void sumOfTwoTest() {
        var result1 = NumberUtil.sumOfTwoNumsYieldN(10, new int[]{1, 2, 3, 4});
        var result2 = NumberUtil.sumOfTwoNumsYieldN(3, new int[]{1, 2, 3, 4});
        var result3 = NumberUtil.sumOfTwoNumsYieldN(5, new int[]{1, 2, 3, 4});
        var result4 = NumberUtil.sumOfTwoNumsYieldN(2, new int[]{1, 2, 3, 4});
        var result5 = NumberUtil.sumOfTwoNumsYieldN(8, new int[]{1, 2, 3, 4});
        var result6 = NumberUtil.sumOfTwoNumsYieldN(7, new int[]{1, 2, 3, 4});

        assertFalse(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertFalse(result4);
        assertFalse(result5);
        assertTrue(result6);
    }
}