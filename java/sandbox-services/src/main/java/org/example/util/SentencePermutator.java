package org.example.util;

import java.util.*;


public class SentencePermutator {

    public List<String> permuteWords(String sentence) {
        String[] words = sentence.split("\\s+");
        List<String> results = new ArrayList<>();
        permute(words, 0, results);
        return results;
    }

    private void permute(String[] words, int start, List<String> results) {
        if (start == words.length - 1) {
            // exit case
            results.add(String.join(" ", words));
            return;
        }
        for (int i = start; i < words.length; i++) {
            swap(words, start, i);
            permute(words, start + 1, results);
            swap(words, start, i); // backtrack
        }
    }

    private void swap(String[] arr, int i, int j) {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
