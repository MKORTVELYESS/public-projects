package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.example.service.FlightDelayService;
import org.example.service.InfoService;
import org.example.service.JilService;
import org.example.service.PrimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

  private static final Logger logger = LoggerFactory.getLogger(Controller.class);

  private final InfoService infoService;
  private final JilService jilService;
  private final PrimeService primeService;
  private final FlightDelayService flightDelayService;

  public Controller(
      InfoService infoService,
      JilService jilService,
      PrimeService primeService,
      FlightDelayService flightDelayService) {
    this.jilService = jilService;
    this.infoService = infoService;
    this.primeService = primeService;
    this.flightDelayService = flightDelayService;
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

  @GetMapping("/generate-first-n-primes/{n}")
  public ResponseEntity<String> persistFirstNPrimes(@PathVariable int n) {
    logger.info("Received n: {}", n);
    StopWatch sw = new StopWatch();
    sw.start();
    primeService.generateAndSavePrimes(n);
    sw.stop();
    return ResponseEntity.ok("Time taken: " + sw.getTotalTimeSeconds() + " seconds");
  }

  @GetMapping("/max-flight-delay/{from-city}/{to-city}")
  public ResponseEntity<Double> getMaxFlightDelayOnRoute(
      @PathVariable("from-city") String fromCity, @PathVariable("to-city") String toCity)
      throws IOException {
    logger.info("Received from city: {} and to city: {}", fromCity, toCity);
    StopWatch sw = new StopWatch();
    sw.start();
    var r = flightDelayService.getMaxDelayOnRoute(fromCity, toCity);
    sw.stop();
    logger.info(
        "Found max delay in {} ms. The max departure delay on route {} -> {} was {} hours",
        sw.getTotalTimeMillis(),
        fromCity,
        toCity,
        r / 60);
    return ResponseEntity.ok(r);
  }
}
