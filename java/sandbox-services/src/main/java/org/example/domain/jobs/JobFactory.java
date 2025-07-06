package org.example.domain.jobs;

import static org.example.domain.jobs.JilReader.*;
import static org.example.domain.jobs.JilValidator.getJobName;
import static org.example.domain.jobs.JilValidator.getJobType;

import org.example.domain.jobs.model.Box;
import org.example.domain.jobs.model.Cmd;
import org.example.domain.jobs.model.Fw;
import org.example.domain.jobs.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFactory {

  private static final Logger logger = LoggerFactory.getLogger(JobFactory.class);

  public static Job fromInsertJobJil(String jil) {
    final var keyValueMap = jilToKeyValueMap(jil);
    final var jobName = getJobName(keyValueMap);
    final var jobType = getJobType(keyValueMap);
    logger.info("Start create {} job with name {}", jobType, jobName);
    return switch (jobType) {
      case JobType.CMD -> new Cmd(keyValueMap);
      case JobType.BOX -> new Box(keyValueMap);
      case JobType.FW -> new Fw(keyValueMap);
      default -> throw new IllegalArgumentException("Job type " + jobType + " not implemented yet");
    };
  }
}
