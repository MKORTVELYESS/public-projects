package org.example.util;

import java.util.ArrayList;

public class SimplePermute {

  public static void main(String[] args) {
    var result = permuteHelper("ABCD", "", new ArrayList<>());
    System.out.println(result);
  }

  public static ArrayList<String> permuteHelper(
      String permutable, String remaining, ArrayList<String> results) {
    if (permutable.isEmpty()) return results;
    if (permutable.length() == 1) {
      results.add(permute("", permutable + remaining));
      return results;
    }
    results.add(permute(permutable.charAt(0) + remaining, permutable.substring(1)));
    return permuteHelper(permutable.substring(1), remaining + permutable.charAt(0), results);
  }

  public static String permute(String prefix, String remaining) {
    if (remaining.isEmpty()) return prefix;
    if (remaining.length() == 1) return prefix + remaining;
    return permute(prefix + remaining.charAt(0), remaining.substring(1));
  }
}
