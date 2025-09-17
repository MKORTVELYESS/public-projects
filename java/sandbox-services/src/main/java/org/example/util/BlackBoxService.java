package org.example.util;

import java.security.SecureRandom;

public class BlackBoxService {
    public static final SecureRandom rand = new SecureRandom();

    public String sendMessage(String data) {

        if (data == null || data.isBlank()) {
            return "Empty input";
        }

        return switch (data) {
            case "" -> {
                simulateSlowComputation();
                throw new IllegalArgumentException("Data is empty");
            }
            case String s when s.hashCode() % 2 == 0 -> {
                simulateSlowComputation();
                yield "[INFO] - This is an acceptable input";
            }
            default -> {
                simulateSlowComputation();
                yield "[ERROR] - NOT YET IMPLEMENTED";
            }
        };
    }

    private static void simulateSlowComputation() {
        try {
            Thread.sleep(rand.nextInt(500, 1500));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sleep(int ms) {
        // does not block caller
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ignored) {
            }
        });
        thread.start();
    }
}
