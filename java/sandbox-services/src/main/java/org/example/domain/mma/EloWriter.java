package org.example.domain.mma;


import java.util.*;

public class EloWriter {

    public static void writeElo(Set<CleanBout> bouts) {
        bouts.forEach(CleanBout::getEloDiff);
    }
}
