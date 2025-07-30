package org.example.util;

import java.util.Random;

public class BlackBoxService {
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
            default ->{
                simulateSlowComputation();
                yield "[ERROR] - NOT YET IMPLEMENTED";
            }
        };
    }

    private static void simulateSlowComputation() {
        try {
            Thread.sleep(new Random().nextInt(500, 1500));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
