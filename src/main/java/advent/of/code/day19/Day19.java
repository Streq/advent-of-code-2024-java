package advent.of.code.day19;

import java.util.*;

import static advent.of.code.util.Util.*;

public class Day19 {

  record Input(List<String> parts, List<String> designs) {}

  private static final String INPUT_PATH = inputPath(Day19.class);

  static Input inputFromFile(String path) {
    List<String> fileAsLines = getFileAsLines(path);

    var parts =
        Arrays.stream(fileAsLines.getFirst().split(", "))
            .sorted(Comparator.reverseOrder())
            .toList();
    var designs = fileAsLines.subList(2, fileAsLines.size());

    return new Input(parts, designs);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Input input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Input input) {
      log(input.designs);
      log(input.parts);
      return input.designs.stream()
          .filter(design -> canBeMadeBy(design, input.parts, new HashMap<>()))
          .count();
    }
  }

  static class Part2 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Input input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Input input) {
      log(input.designs);
      log(input.parts);
      HashMap<String, Long> cache = new HashMap<>();
      long ret =
          input.designs.stream()
              .mapToLong(design -> countBeMadeBy(design, input.parts, cache))
              .sum();
      // log(cache);
      return ret;
    }
  }

  private static boolean canBeMadeBy(
      String design, List<String> partsToTry, Map<String, Boolean> cache) {
    if (cache.containsKey(design)) {
      return cache.get(design);
    }
    partsToTry = partsToTry.stream().filter(design::contains).toList();

    for (var part : partsToTry) {
      if (design.length() == part.length()
          || design.startsWith(part)
              && canBeMadeBy(design.substring(part.length()), partsToTry, cache)) {
        cache.put(design, true);
        return true;
      }
    }
    cache.put(design, false);
    return false;
  }

  private static long countBeMadeBy(
      String design, List<String> partsToTry, Map<String, Long> cache) {
    if (cache.containsKey(design)) {
      return cache.get(design);
    }
    partsToTry = partsToTry.stream().filter(design::contains).toList();

    long totalWays = 0;

    for (var part : partsToTry) {
      if (design.length() == part.length()) {
        totalWays += 1;
      } else if (design.startsWith(part)) {
        totalWays += countBeMadeBy(design.substring(part.length()), partsToTry, cache);
      }
    }
    cache.put(design, totalWays);
    return totalWays;
  }
}
