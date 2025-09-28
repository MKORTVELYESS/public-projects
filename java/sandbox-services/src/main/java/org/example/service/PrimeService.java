package org.example.service;

import org.example.entity.Prime;
import org.example.repository.PrimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import static org.example.util.NumberUtil.isPrime;

@Service
public class PrimeService {
    private final PrimeRepository primeRepository;
    private static final int BATCH_SIZE = 40000;
    private static final Logger logger = LoggerFactory.getLogger(PrimeService.class);
    private final Executor virtualExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    private final Semaphore semaphore = new Semaphore(20); // limit to 10 concurrent inserts


    public PrimeService(PrimeRepository primeRepository) {
        this.primeRepository = primeRepository;
    }

    public void generateAndSavePrimes(int firstN) {
        logger.info("Got request to generate first {} prime numbers and persisting into the database", firstN);
        List<Prime> batch = new ArrayList<>(BATCH_SIZE);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicInteger completedBatchesCount = new AtomicInteger();
        AtomicLong position = new AtomicLong(1);

        logger.info("Clearing table first...");
        primeRepository.truncateTable();

        int targetBatchesCount = firstN / BATCH_SIZE;
        logger.info("Expecting {} batches for count {} and batch size of {}", targetBatchesCount, firstN, BATCH_SIZE);

        int seed = 2;
        logger.info("Start generating numbers from {} until the persisted batches count matches the expected {}", seed, targetBatchesCount);
        LongStream.iterate(seed, i -> completedBatchesCount.get() < targetBatchesCount, i -> i + 1)
                .peek(i -> {
                    if (batch.size() >= BATCH_SIZE) {
                        logger.info("Batch size is {} which is greater or equal to the max batch size of {}", batch.size(), BATCH_SIZE);
                        futures.add(persistPrimes(new ArrayList<>(batch)));
                        batch.clear();
                        logger.info("Incrementing target batch count now");
                        completedBatchesCount.addAndGet(1);
                    }
                })
                .forEach(i -> {
                    if (isPrime(i)) batch.add(new Prime(position.getAndIncrement(), i));
                });

        if (!batch.isEmpty()) futures.add(persistPrimes(new ArrayList<>(batch)));

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        logger.info("Inserted first {} primes to table", firstN);

    }

    private CompletableFuture<Void> persistPrimes(List<Prime> batch) {
        logger.info("Received batch to persist... Will schedule async task to persist this batch");
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        semaphore.acquire();
                        primeRepository.saveAll(batch);
                    } catch (InterruptedException e) {
                        logger.error("Semaphore interrupted", e);
                    } finally {
                        semaphore.release();
                    }
                }, virtualExecutor
        );
    }
}
