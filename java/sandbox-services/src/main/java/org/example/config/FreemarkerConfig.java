package org.example.config;

import static freemarker.template.Configuration.VERSION_2_3_34;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfig {

  @Value("${templates.dir}")
  private String templatesBasedir;

  @Bean
  public freemarker.template.Configuration freemarker() throws IOException {
    var cfg = new freemarker.template.Configuration(VERSION_2_3_34);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setDirectoryForTemplateLoading(new File(templatesBasedir));
    return cfg;
  }
}
