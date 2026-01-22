package org.example;

import static org.mockito.Mockito.mock;

import org.example.service.HtmlFetchService;
import org.example.util.SystemTimeSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {
  @Bean
  public SystemTimeSource mockSystemTimeSource() {
    return mock(SystemTimeSource.class);
  }

  @Bean
  public HtmlFetchService mockHtmlFetchService() {
    return mock(HtmlFetchService.class);
  }
}
