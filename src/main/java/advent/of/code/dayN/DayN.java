package advent.of.code.dayN;

import static advent.of.code.util.Util.*;

public class DayN {

  private static final String INPUT_PATH = inputPath(DayN.class);

  static Object inputFromFile(String path) {
    return getFileAsString(path);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Object input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Object input) {
      return 0;
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Object input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Object input) {
      return 0;
    }
  }
}
