package org.example.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

class FlightDelayServiceTest {

  @Test
  void contentOfColumnsTest() {
    FlightDelayService svc = new FlightDelayService("");
    var res = svc.columnExtractor("egy,ketto,harom,negy,ot,hat", Set.of(0, 2, 5));
    assertEquals("egy", res[0]);
    assertEquals("harom", res[1]);
    assertEquals("hat", res[2]);
  }

  @Test
  void contentOfColumnsTestQuotedCommas() {
    FlightDelayService svc = new FlightDelayService("");
    var res = svc.columnExtractor("egy,\"ket,to\",harom,negy,ot,hat", Set.of(0, 2, 5));
    assertEquals("egy", res[0]);
    assertEquals("harom", res[1]);
    assertEquals("hat", res[2]);
  }
}
