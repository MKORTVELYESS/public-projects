package org.example;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main implements HttpAware {
    private static final String url = "https://httpstat.us/";

    public static void main(String[] args) throws InterruptedException {

        var sender = new HttpSender();
        // Shared queues between stages
        BlockingQueue<CompletableFuture
                <java.net.http.HttpResponse<java.lang.String>>> stage1to2 = new LinkedBlockingQueue<>();
        BlockingQueue<
                CompletableFuture
                        <java.lang.Integer>> stage2to3 = new LinkedBlockingQueue<>();

        Map<Integer, Integer> original = Map.of(
                250, 200,
                550, 404,
                150, 201,
                25, 202,
                4500, 500,
                5000, 500,
                2500, 401,
                500, 200,
                100, 200);
        Map<Integer, Integer> input = new LinkedHashMap<>(original);

        AtomicInteger checksum = new AtomicInteger();
        Integer expectedChecksum = input.values().stream().mapToInt(Integer::intValue).sum();

        AtomicBoolean shouldContinue = new AtomicBoolean(true);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Instant startedAt = Instant.now();

        // Stage 1: Load input and simulate work
        executor.submit(() -> {
            try {
                for (Integer waitTime : input.keySet()) {
                    Integer expectedStatusCode = input.get(waitTime);
                    String url1 = getUrl(expectedStatusCode, waitTime);
                    System.out.println("[ " + LocalTime.now() + " ] [Stage 1] Processing " + waitTime + " we will send a request to " + url1);

                    var result = sender.sendGetRequestTo(url1); //simulate work that returns future

                    stage1to2.put(result);
                }
                CompletableFuture<HttpResponse<String>> POISON_PILL = new CompletableFuture<>();
                POISON_PILL.completeExceptionally(new RuntimeException("POISON_PILL"));
                stage1to2.put(POISON_PILL); // poison pill
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Stage 2: Consume from stage 1 and pass to stage 3
        executor.submit(() -> {
            try {
                while (shouldContinue.get()) {

                    CompletableFuture
                            <java.net.http.HttpResponse<java.lang.String>> item = stage1to2.take();

                    var result = item.thenCompose(r ->
                            CompletableFuture.supplyAsync(() -> {
                                try {
                                    System.out.println("[ " + LocalTime.now() + " ] [Stage 2] received CompletableFuture<HttpResponse<String>>, will output CompletableFuture<Integer>");
                                    Thread.sleep(r.statusCode()); // simulate delay
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                return r.statusCode();
                            })
                    );

                    stage2to3.put(result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Stage 3: Final stage
        executor.submit(() -> {
            try {
                while (shouldContinue.get()) {
                    CompletableFuture<Integer> item = stage2to3.take();


                    CompletableFuture
                            <java.lang.Integer> code = item.thenApply((cd) -> {
                        try {
                            System.out.println("[ " + LocalTime.now() + " ] [Stage 3] Processing " + cd);
                            System.out.println("[ " + LocalTime.now() + " ] [Stage 3] Done: " + cd + "-S3");
                            Thread.sleep(cd); // simulate delay
                            checksum.set(checksum.get() + cd);
                            System.out.println("Current checksum is " + checksum + " while the expected is " + expectedChecksum);

                            if (checksum.get() == expectedChecksum) {
                                System.out.println("Breaking the pipeline");
                                shouldContinue.set(false);
                                executor.shutdownNow();
                            }

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return cd;


                    });

                    System.out.println(code.get());

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        Instant finishedAt = Instant.now();

        System.out.println("Pipeline complete in " + Duration.between(startedAt, finishedAt).toMillis() + "ms");

    }

    private static String getUrl(int code, int delayMs) {
        return url + code + "?sleep=" + delayMs;
    }
}