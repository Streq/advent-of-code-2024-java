package advent.of.code.day5;

import advent.of.code.util.Util;

import java.util.*;
import java.util.function.Predicate;

import static advent.of.code.util.Util.inputPath;
import static java.util.stream.Collectors.*;

public class Day5 {

  private static final String INPUT_PATH = inputPath(Day5.class);

  static Sheet inputFromFile(String path) {
    var lines = Util.getFileAsLines(path);

    var separator = 0;
    for (var line : lines) {
      if (line.isBlank()) {
        break;
      }
      ++separator;
    }

    Set<Rule> rules =
        lines.subList(0, separator).stream()
            .map(
                line -> {
                  var parts = line.split("\\|");
                  var a = Integer.parseInt(parts[0]);
                  var b = Integer.parseInt(parts[1]);

                  return new Rule(a, b);
                })
            .collect(toUnmodifiableSet());

    List<List<Integer>> updates =
        lines.subList(separator + 1, lines.size()).stream()
            .map(line -> Arrays.stream(line.split(",")).map(Integer::parseUnsignedInt).toList())
            .filter(Predicate.not(List::isEmpty))
            .toList();

    return new Sheet(rules, updates);
  }

  record Rule(int a, int b) {
    boolean matches(List<Integer> updates) {
      int fi = updates.lastIndexOf(a);
      int si = updates.indexOf(b);
      return si == -1 || fi <= si;
    }

    public String toString() {
      return "%d|%d".formatted(a, b);
    }
  }

  record Sheet(Set<Rule> rules, List<List<Integer>> updates) {}

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    /**
     * The notation X|Y means that if both page number X and page number Y are to be produced as
     * part of an update, page number X must be printed at some point before page number Y.
     */
    static long solve(Sheet input) {
      return input.updates.stream()
          .filter(update -> input.rules.stream().allMatch(rule -> rule.matches(update)))
          .mapToInt(update -> update.get(update.size() / 2))
          .sum();
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Sheet input) {
      var fixer = Fixer.from(input.rules);

      return input.updates.stream()
          .filter(update -> !input.rules.stream().allMatch(rule -> rule.matches(update)))
          .map(fixer::fix)
          .mapToInt(update -> update.get(update.size() / 2))
          .sum();
    }
  }
}
