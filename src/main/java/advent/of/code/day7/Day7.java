package advent.of.code.day7;

import java.time.Instant;
import java.util.*;

import static advent.of.code.util.Util.getFileAsLines;
import static advent.of.code.util.Util.inputPath;

public class Day7 {

  private static final String INPUT_PATH = inputPath(Day7.class);

  public record Equation(Long result, List<Long> components) {}

  static List<Equation> inputFromFile(String path) {
    return getFileAsLines(path).stream()
        .map(
            l -> {
              String[] split = l.split(": ");
              var result = Long.parseUnsignedLong(split[0]);
              var components =
                  Arrays.stream(split[1].split(" ")).map(Long::parseUnsignedLong).toList();
              return new Equation(result, components);
            })
        .toList();
  }

  static class Part1 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(List<Equation> input) {
      AMOUNT = input.size();
      Operator[] operators = {Operator.SUM, Operator.MUL};
      return input.stream()
          .filter(equation -> hasSolution(equation, operators))
          .mapToLong(Equation::result)
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

    static long solve(List<Equation> input) {
      var start = Instant.now();

      AMOUNT = input.size();
      Operator[] operators = {Operator.SUM, Operator.MUL, Operator.CON};
      var ret =
          input.stream()
              .filter(equation -> hasSolution(equation, operators))
              .mapToLong(Equation::result)
              .sum();
      var end = Instant.now();
      System.out.printf("took %d millis%n", end.toEpochMilli() - start.toEpochMilli());
      return ret;
    }
  }

  static int COUNTER = 0;
  static int COMPUTATIONS = 0;
  static int AMOUNT = 0;

  private static boolean hasSolution(Equation equation, Operator... operators) {
    List<Long> components = equation.components();
    long result = equation.result();
    Operator[][] combinations = Operator.combinations(components.size() - 1, operators);

    for (var combination : combinations) {
      if (combinationSolves(components, result, combination)) {
        System.out.printf("%d/%d and performed %d computations %n", ++COUNTER, AMOUNT, COMPUTATIONS);
        return true;
      }
    }

    System.out.printf("%d/%d and performed %d computations %n", ++COUNTER, AMOUNT, COMPUTATIONS);
    return false;
  }

  private static boolean combinationSolves(List<Long> components, long result, Operator[] combination) {
    long accum = components.getFirst();

    for (int i = 0; i < combination.length; i++) {
      var operator = combination[i];
      accum = operator.apply(accum, components.get(i + 1));
      ++COMPUTATIONS;

      if (accum > result) { // performance culling
        return false;
      }
    }

    return accum == result;
  }
}
