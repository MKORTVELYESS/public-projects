package org.example;

import org.example.util.BlackBoxService;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    @Test
    public void testSendMessage() {
        var messages = List.of("Zsolt", "Jozsi", "Luca", "Patrik", "Aron", "Dorka", "Fanni", "Bence", "Dominik");
        var sw = new StopWatch();
        var service = new BlackBoxService();

        sw.start();

        messages.stream()
                .map(service::sendMessage)
                .forEach(System.out::println);

        sw.stop();

        System.out.printf("%.2f s elapsed\n", sw.getTotalTimeSeconds());
    }

    @Test
    public void testSendMessage2() {
        var messages = List.of("Zsolt", "Jozsi", "Luca", "Patrik", "Aron", "Dorka", "Fanni", "Bence", "Dominik");
        var sw = new StopWatch();
        var service = new BlackBoxService();

        sw.start();

        List<CompletableFuture<String>> runningCalcs =
                messages.stream().map(msg ->
                                CompletableFuture.supplyAsync(() -> service.sendMessage(msg)))
                        .toList();

        runningCalcs.forEach(
                runningCalc ->
                        runningCalc.thenAccept(System.out::println));

        var allCalcs = CompletableFuture.allOf(runningCalcs.toArray(CompletableFuture[]::new));

        allCalcs.thenRun(() -> {
            sw.stop();
            System.out.printf("%.2f s elapsed\n", sw.getTotalTimeSeconds());
        }).join();

    }
}
