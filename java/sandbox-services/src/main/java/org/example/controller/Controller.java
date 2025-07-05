package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.example.service.InfoService;
import org.example.service.JilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

  private static final Logger logger = LoggerFactory.getLogger(Controller.class);

  private final InfoService infoService;
  private final JilService jilService;

  public Controller(InfoService infoService, JilService jilService) {
    this.jilService = jilService;
    this.infoService = infoService;
  }

  @GetMapping("/health")
  public Map<String, String> health(HttpServletRequest request) {
    Map<String, String> response = new HashMap<>();
    response.put("whoami", infoService.getClientIp(request));
    response.put("time", infoService.getCurrentTime());
    response.put("status", "up");
    return response;
  }

  @PostMapping("/jil/print")
  public ResponseEntity<String> jilPrint(@RequestBody String body) {
    logger.info("Received body: {}", body);
    jilService.printJobs(body);
    return ResponseEntity.ok("Received: " + body);
  }
}
