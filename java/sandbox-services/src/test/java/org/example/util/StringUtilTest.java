package org.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void reverse() {
        assertEquals("olleh", StringUtil.reverse("hello"));
    }
}