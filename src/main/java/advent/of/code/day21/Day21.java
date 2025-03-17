package advent.of.code.day21;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static advent.of.code.day21.PathMatrix.*;
import static advent.of.code.util.Util.*;

public class Day21 {

  private static final String INPUT_PATH = inputPath(Day21.class);

  record Input(List<String> commands) {
    public static final Pattern REGEX = Pattern.compile(".+");

    public static Input from(String str) {
      return new Input(REGEX.matcher(str).results().map(r -> r.group(0)).toList());
    }
  }

  static Input inputFromFile(String path) {
    return Input.from(getFileAsString(path));
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
      return solveForRobotNumber(input, 2);
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
      return solveForRobotNumber(input, 25);
    }
  }

  static Stream<String> expandNum(String str) {
    return expandIntermediate("", 'A', str);
  }

  private static Stream<String> expandIntermediate(String done, char previous, String remaining) {
    if (remaining.isEmpty()) {
      return Stream.of(done);
    }

    char current = remaining.charAt(0);

    List<String> paths = NUM_MATRIX.pathsTo(previous, current);

    String newRemaining = remaining.substring(1);
    return paths.stream().flatMap(path -> expandIntermediate(done + path, current, newRemaining));
  }

  static long getLength(String str, int indirections) {
    return expandNum(str)
        .mapToLong(s -> IndirectionExpander.getLength(s, indirections))
        .min()
        .orElseThrow();
  }

  static long getNum(String str) {
    var match = Pattern.compile("(\\d+)").matcher(str).results().findFirst().orElseThrow().group(1);
    return Long.parseLong(match);
  }

  private static long solveForRobotNumber(Input input, int indirections) {
    return input.commands.stream().mapToLong(c -> getLength(c, indirections) * getNum(c)).sum();
  }
}
