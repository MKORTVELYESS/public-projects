package org.example.jobs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JobTest {

  @Test
  void testJob() {
    Job j =
        new Job(
            "      insert_job:       appDev#cmd#ProductLoad\n"
                + "      job_type:         command\n"
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

    System.out.println(j.getAttributes());

    assertEquals("appDev#cmd#ProductLoad", j.getAttributes().get("insert_job"));
    assertEquals("command", j.getAttributes().get("job_type"));
    assertEquals("Run ProductLoad Script", j.getAttributes().get("description"));
    assertEquals("appDev#box#ProductLoad", j.getAttributes().get("box_name"));
    assertEquals("@[DB_USER]", j.getAttributes().get("owner"));
    assertEquals("@[AUTOSYS_SERVER]", j.getAttributes().get("machine"));
    assertEquals("gx,wx", j.getAttributes().get("permission"));
    assertEquals("/appl/bin/appDev_ProductLoad.sh", j.getAttributes().get("command"));
    assertEquals("/appl/log/appDev#box#ProductLoad.out", j.getAttributes().get("std_out_file"));
    assertEquals("/appl/log/appDev#box#ProductLoad.err", j.getAttributes().get("std_err_file"));
    assertEquals("0", j.getAttributes().get("min_run_alarm"));
    assertEquals("30", j.getAttributes().get("max_run_alarm"));
    assertEquals("yes", j.getAttributes().get("job_terminator"));
    assertEquals("yes", j.getAttributes().get("box_terminator"));
  }
}
