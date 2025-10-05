package org.example.service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import org.example.entity.Prime;
import org.example.repository.PrimeRepository;
import org.example.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class PrimeService {
  private static final int CONCURRENT_DB_OPERATION_PERMITS = 10;
  private static final int BATCH_SIZE = 5000;
  private static final int GENERATION_SEED = 2;
  private static final Logger logger = LoggerFactory.getLogger(PrimeService.class);
  private final Executor virtualExecutor =
      Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
  private final Semaphore semaphore =
      new Semaphore(CONCURRENT_DB_OPERATION_PERMITS); // limit to 10 concurrent inserts
  private final PrimeRepository primeRepository;

  public PrimeService(PrimeRepository primeRepository) {
    this.primeRepository = primeRepository;
  }

  public void generateAndSavePrimes(int firstN) {
    logger.info(
        "Got request to generate first {} prime numbers and persisting into the database", firstN);

    logger.info("Clearing table first...");
    primeRepository.truncateTable();

    int targetBatchesCount = firstN / BATCH_SIZE;
    logger.info(
        "Expecting {} full batches for count {} and batch size of {}",
        targetBatchesCount,
        firstN,
        BATCH_SIZE);

    logger.info(
        "Start generating numbers from {} until the persisted batches count matches the expected {}",
        GENERATION_SEED,
        targetBatchesCount);

    BlockingQueue<Prime> primes = createQueueOfPrimes(firstN, GENERATION_SEED);
    logger.info("Number generation done... Will assign position now");

    sortAndEnrichQueueMembers(primes);
    logger.info("Assigned positions, will insert results to database in batches now...");

    BlockingQueue<Prime> insertables = limitQueueSize(firstN, primes);
    List<CompletableFuture<Void>> dbCalls = batchInsertPrimes(insertables);

    logger.info("Done issuing database operations, will wait for all of them to complete");
    CompletableFuture.allOf(dbCalls.toArray(new CompletableFuture[0])).join();

    logger.info("Inserted first {} primes to table", firstN);
  }

  private BlockingQueue<Prime> limitQueueSize(int firstN, BlockingQueue<Prime> primes) {
    BlockingQueue<Prime> insertables = new LinkedBlockingQueue<>();
    primes.drainTo(insertables, firstN);
    return insertables;
  }

  private List<CompletableFuture<Void>> batchInsertPrimes(BlockingQueue<Prime> primes) {
    List<CompletableFuture<Void>> dbCalls = new ArrayList<>();
    while (!primes.isEmpty()) {
      HashSet<Prime> batch = new HashSet<>();
      primes.drainTo(batch, BATCH_SIZE);
      CompletableFuture<Void> dbOp = persistPrimes(batch);
      dbCalls.add(dbOp);
    }
    return dbCalls;
  }

  private void sortAndEnrichQueueMembers(BlockingQueue<Prime> primes) {
    TreeSet<Prime> allSorted = new TreeSet<>();
    primes.drainTo(allSorted);

    logger.info("Tree set created of {} items", allSorted.size());
    assignPosition(allSorted);

    logger.info("TreeSet will be converted to queue now");
    primes.addAll(allSorted);
    logger.info("Created queue");
  }

  private void assignPosition(TreeSet<Prime> allSorted) {
    logger.info("Start assigning positions in memory to primes");
    AtomicLong pos = new AtomicLong(1);
    for (Prime current : allSorted) {
      current.setPosition(pos.getAndIncrement());
    }
    logger.info("Done assigning positions");
  }

  private BlockingQueue<Prime> createQueueOfPrimes(int firstN, int seed) {
    final BlockingQueue<Prime> calculatedPrimes = new LinkedBlockingQueue<>();
    LongStream.iterate(seed, i -> calculatedPrimes.size() < firstN, i -> i + 1)
        .parallel()
        .filter(NumberUtil::isPrime)
        .mapToObj(Prime::new)
        .forEach(calculatedPrimes::add);
    return calculatedPrimes;
  }

  private CompletableFuture<Void> persistPrimes(Set<Prime> batch) {
    logger.info("Received batch to persist... Will schedule async task to persist this batch");
    return CompletableFuture.runAsync(
        () -> {
          StopWatch databaseTime = new StopWatch();
          try {
            StopWatch semaphoreWait = new StopWatch();
            semaphoreWait.start();
            semaphore.acquire();
            semaphoreWait.stop();
            databaseTime.start();
            logger.info(
                "Waited {} seconds in queue for db semaphore", semaphoreWait.getTotalTimeSeconds());
            primeRepository.saveAll(batch);
          } catch (InterruptedException e) {
            logger.error("Semaphore interrupted", e);
          } finally {
            semaphore.release();
            databaseTime.stop();
            logger.info(
                "Waited {} seconds for database to complete the batch insert operation",
                databaseTime.getTotalTimeSeconds());
          }
        },
        virtualExecutor);
  }
}
