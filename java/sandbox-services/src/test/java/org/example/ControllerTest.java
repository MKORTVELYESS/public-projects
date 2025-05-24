package org.example;

import org.example.util.DateTimeUtil;
import org.example.util.SystemTimeSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class ControllerTest {
    private final ZonedDateTime testTime =
            ZonedDateTime.of(2025, 5, 24, 12, 47, 3, 0,
                    ZoneId.of("UTC"));
    private final SystemTimeSource mockSystemTimeSource;
    private final Controller controller;

    @Autowired
    public ControllerTest(SystemTimeSource systemTimeSource, Controller controller) {
        this.mockSystemTimeSource = systemTimeSource;
        this.controller = controller;
    }

    @Test
    void health() {
        final String ip = "123.45.67.89";
        final String time = DateTimeUtil.formatted(testTime);
        final String status = "up";

        when(mockSystemTimeSource.now())
                .thenReturn(testTime);

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET", "/health");
        mockHttpServletRequest.addHeader("X-Forwarded-For", ip);

        var expected = Map.of("whoami", ip, "time", time, "status", status);
        var actual = controller.health(mockHttpServletRequest);

        assertEquals(expected,actual);
    }
}