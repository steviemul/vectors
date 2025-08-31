package io.steviemul.vectors.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sanitizer {

  private static final String FORMATTING_PATTERN = "\\{\\{[#a-zA-Z]*}}";
  private static final int MAX_WORDS = 100;

  public static String sanitize(String... strings) {

    List<String> output = new ArrayList<>();

    for (int i = 0; i < strings.length; i++) {
      if (strings[i] == null) continue;

      output.add(sanitize(strings[i]));
    }

    return String.join(".\n", output);
  }

  public static String sanitize(String input) {

    input = removeFormatting(input);

    String[] words = input.split(" ");

    int limit = Math.min(words.length, MAX_WORDS);

    words = Arrays.copyOfRange(words, 0 , limit);

    return String.join(" ", words).trim();
  }

  public static String removeFormatting(String input) {
    return input.replaceAll(FORMATTING_PATTERN, ". ");
  }
}
