package org.example.config;

import org.example.util.SystemTimeSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TimeSourceConfig {

  @Bean
  @Profile("!test")
  public SystemTimeSource systemTimeSource() {
    return new SystemTimeSource();
  }
}
