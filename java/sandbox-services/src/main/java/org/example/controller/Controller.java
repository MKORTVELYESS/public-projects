package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.service.*;
import org.example.util.SystemTimeSource;
import org.example.util.ThrottleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

  @Value("${network.throttle.base-millis}")
  private long baseMillis;

  @Value("${network.throttle.extra-millis}")
  private int extraMillis;

  private static final Logger logger = LoggerFactory.getLogger(Controller.class);

  private final InfoService infoService;
  private final JilService jilService;
  private final PrimeService primeService;
  private final FlightDelayService flightDelayService;
  private final ResolvedTemplateService resolvedTemplateService;
  private final SystemTimeSource systemTimeSource;
  private final HtmlFetchService htmlFetchService;
  private final TapologyService tapologyService;

  public Controller(
      InfoService infoService,
      JilService jilService,
      PrimeService primeService,
      FlightDelayService flightDelayService,
      ResolvedTemplateService resolvedTemplateService,
      SystemTimeSource systemTimeSource,
      HtmlFetchService htmlFetchService,
      TapologyService tapologyService) {
    this.jilService = jilService;
    this.infoService = infoService;
    this.primeService = primeService;
    this.flightDelayService = flightDelayService;
    this.resolvedTemplateService = resolvedTemplateService;
    this.systemTimeSource = systemTimeSource;
    this.htmlFetchService = htmlFetchService;
    this.tapologyService = tapologyService;
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

  @GetMapping("/flight-delay-data-line-count")
  public ResponseEntity<Long> getMaxFlightDelayOnRoute() throws IOException {
    logger.info("Calculating line count of flight delay full data set");
    StopWatch sw = new StopWatch();
    sw.start();
    var r = flightDelayService.getFlightCount();
    sw.stop();
    logger.info("Found line count in {} ms. The line count is: {}", sw.getTotalTimeMillis(), r);
    return ResponseEntity.ok(r);
  }

  @PostMapping("/resolved-template/{template-name}")
  public ResponseEntity<String> getResolvedTemplate(
      @PathVariable("template-name") String templateName,
      @RequestBody Map<String, Object> payload) {
    logger.info("Resolving template: {}", templateName);
    StopWatch sw = new StopWatch();
    sw.start();
    var result = resolvedTemplateService.resolve(templateName, payload);
    sw.stop();
    logger.info("Resolution took {} ms", sw.getTotalTimeMillis());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/fetch-html")
  public String fetchHtml(@RequestParam String url) {
    return htmlFetchService.fetchHtml(url);
  }

  @GetMapping("/resolve-fighters")
  public String resolveFighters(@RequestBody String url) {
    List<String> urls = Arrays.stream(url.split(",")).toList();
    urls.forEach(
        (u) -> {
          ThrottleUtil.throttle(baseMillis, extraMillis);
          logger.info("Persisting {}", u);
          tapologyService.printFighters(u);
        });
    return "Done";
  }

  @GetMapping("/height-win-rate-correl-by-weight-group")
  public String resolveFighters() {
    tapologyService.getHeightWinRateCorrelationsByWeightGroup();
    return "Done";
  }

  @GetMapping("/persist-fighter-details")
  public String persistFighterDetails() {
    tapologyService.persistAllFighterDetails();
    return "Done";
  }

    @GetMapping("/persist-features")
    public String persistFeatures() {
        tapologyService.persistFeatures();
        return "Done";
    }
}
