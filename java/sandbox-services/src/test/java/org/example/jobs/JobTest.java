package org.example.jobs;

import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.jobs.JilAttributeKey;
import org.example.domain.jobs.JobFactory;
import org.example.domain.jobs.model.Job;
import org.junit.jupiter.api.Test;

class JobTest {

  @Test
  void testJob() {
    Job j =
        JobFactory.fromInsertJobJil(
            "      insert_job:       appDev#cmd#ProductLoad\n"
                + "      job_type:         CMD\n"
                + "      description:      Run ProductLoad Script\n"
                + "      box_name:         appDev#box#ProductLoad\n"
                + "      owner:            @[DB_USER]\n"
                + "      machine:          @[AUTOSYS_SERVER]\n"
                + "      permission:       gx,wx\n"
                + "      command:          /appl/bin/appDev_ProductLoad.sh\n"
                + "      std_out_file:     /appl/log/appDev#box#ProductLoad.out\n"
                + "      std_err_file:     /appl/log/appDev#box#ProductLoad.err\n"
                + "      min_run_alarm:    0\n"
                + "      max_run_alarm:    30\n"
                + "      job_terminator:   yes\n"
                + "      box_terminator:   yes");

    System.out.println(j.attributes());

    assertEquals("appDev#cmd#ProductLoad", j.attributes().get(JilAttributeKey.insert_job));
    assertEquals("CMD", j.attributes().get(JilAttributeKey.job_type));
    assertEquals("Run ProductLoad Script", j.attributes().get(JilAttributeKey.description));
    assertEquals("appDev#box#ProductLoad", j.attributes().get(JilAttributeKey.box_name));
    assertEquals("@[DB_USER]", j.attributes().get(JilAttributeKey.owner));
    assertEquals("@[AUTOSYS_SERVER]", j.attributes().get(JilAttributeKey.machine));
    assertEquals("gx,wx", j.attributes().get(JilAttributeKey.permission));
    assertEquals("/appl/bin/appDev_ProductLoad.sh", j.attributes().get(JilAttributeKey.command));
    assertEquals(
        "/appl/log/appDev#box#ProductLoad.out", j.attributes().get(JilAttributeKey.std_out_file));
    assertEquals(
        "/appl/log/appDev#box#ProductLoad.err", j.attributes().get(JilAttributeKey.std_err_file));
    assertEquals("0", j.attributes().get(JilAttributeKey.min_run_alarm));
    assertEquals("30", j.attributes().get(JilAttributeKey.max_run_alarm));
    assertEquals("yes", j.attributes().get(JilAttributeKey.job_terminator));
    assertEquals("yes", j.attributes().get(JilAttributeKey.box_terminator));
  }

  @Test
  void testInvalidJob() {

    assertThrows(
        IllegalStateException.class,
        () -> {
          Job j =
              JobFactory.fromInsertJobJil(
                  "      insert_job:       appDev#cmd#ProductLoad\n"
                      + "      insert_job:         command\n"
                      + "      insert_job:      Run ProductLoad Script\n"
                      + "      insert_job:         appDev#box#ProductLoad\n"
                      + "      owner:            @[DB_USER]\n"
                      + "      machine:          @[AUTOSYS_SERVER]\n");
        });
  }
}
