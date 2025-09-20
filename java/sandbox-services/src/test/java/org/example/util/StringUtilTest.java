package org.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void reverse() {
        assertEquals("olleh", StringUtil.reverse("hello"));
    }

    @Test
    void isPalindrome() {
        assertTrue(StringUtil.isPalindrome("abcdcba"));
        assertTrue(StringUtil.isPalindrome("abccba"));
        assertFalse(StringUtil.isPalindrome("abcdcda"));
        assertFalse(StringUtil.isPalindrome("abccda"));
        assertTrue(StringUtil.isPalindrome(""));
        assertTrue(StringUtil.isPalindrome("-"));
        assertTrue(StringUtil.isPalindrome("--"));
        assertTrue(StringUtil.isPalindrome("---"));
        assertTrue(StringUtil.isPalindrome("-+-"));
        assertTrue(StringUtil.isPalindrome("+-+"));
        assertFalse(StringUtil.isPalindrome("+-+-"));
    }
}