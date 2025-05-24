package org.example;

import org.example.util.SystemTimeSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {
    @Bean
    public SystemTimeSource mockSystemTimeSource() {
        return mock(SystemTimeSource.class);
    }
}
