package org.example.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vavr.control.Try;
import java.io.StringWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResolvedTemplateService {
  private static final Logger log = LoggerFactory.getLogger(ResolvedTemplateService.class);

  @Autowired
  private Configuration
      cfg; // declaring this final without autowired causes spotbugs EI_EXPOSE_REP2

  public ResolvedTemplateService() {}

  public String resolve(String template, Map<String, Object> data) {
    return Try.of(() -> cfg.getTemplate(template))
        .flatMapTry(t -> eval(data, t))
        .onFailure(
            ex -> log.error("Error processing template {}: {}", template, ex.getMessage(), ex))
        .get();
  }

  private Try<String> eval(Map<String, Object> data, Template template) {
    return Try.of(
        () -> {
          StringWriter writer = new StringWriter();
          template.process(data, writer);
          return writer.toString();
        });
  }
}
