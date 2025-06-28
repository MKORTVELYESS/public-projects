package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import org.example.service.InfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private final InfoService infoService;

  public Controller(InfoService infoService) {
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
}
