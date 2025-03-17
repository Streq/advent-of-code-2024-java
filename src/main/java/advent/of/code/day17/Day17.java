package advent.of.code.day17;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static advent.of.code.util.Util.*;

public class Day17 {

  private static final Pattern REGEX =
      Pattern.compile(
          """
                Register A: ([0-9]+)
                Register B: ([0-9]+)
                Register C: ([0-9]+)

                Program: ([0-9](?:,[0-9])*)""");

  private static final String INPUT_PATH = inputPath(Day17.class);

  static Computer inputFromFile(String path) {
    Matcher matcher = REGEX.matcher(getFileAsString(path));
    var result = matcher.results().findFirst().orElseThrow();
    var A = Long.parseLong(result.group(1));
    var B = Long.parseLong(result.group(2));
    var C = Long.parseLong(result.group(3));
    var program = Arrays.stream(result.group(4).split(",")).mapToInt(Integer::valueOf).toArray();
    return new Computer(A, B, C, program);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static String solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static String solve(Computer input) {
      return timeSolution(() -> solveInternal(input));
    }

    static String solveInternal(Computer input) {
      return input.run();
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Computer input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Computer computer) {
      return computer.findSelfProducingA();
    }
  }
}
