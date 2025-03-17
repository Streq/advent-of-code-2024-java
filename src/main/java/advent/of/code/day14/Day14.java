package advent.of.code.day14;

import advent.of.code.util.CharGrid;
import advent.of.code.util.Util;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static advent.of.code.util.Util.*;

public class Day14 {

  private static final String INPUT_PATH = inputPath(Day14.class);

  private static final Pattern REGEX =
      Pattern.compile("p=(-?[0-9]+),(-?[0-9]+) v=(-?[0-9]+),(-?[0-9]+)");

  record RobotData(long x, long y, long vx, long vy) {}

  static List<RobotData> inputFromFile(String path) {
    return REGEX
        .matcher(getFileAsString(path))
        .results()
        .map(
            r ->
                Stream.of(r.group(1), r.group(2), r.group(3), r.group(4))
                    .mapToLong(Long::parseLong)
                    .toArray())
        .map(arr -> new RobotData(arr[0], arr[1], arr[2], arr[3]))
        .toList();
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH, 100, 101, 103));
    }

    static long solveFromFile(String input, long seconds, int width, int height) {
      return solve(inputFromFile(input), seconds, width, height);
    }

    static long solve(List<RobotData> input, long seconds, int width, int height) {
      return timeSolution(() -> solveInternal(input, seconds, width, height));
    }

    static long solveInternal(List<RobotData> input, long seconds, int width, int height) {
      return input.stream()
          .map(
              r ->
                  new CoordL(
                      Math.floorMod(r.x() + r.vx() * seconds, width),
                      Math.floorMod(r.y() + r.vy() * seconds, height)))
          .filter(c -> height % 2 == 0 || c.y() * 2 != height - 1)
          .filter(c -> width % 2 == 0 || c.x() * 2 != width - 1)
          .peek(Util::log)
          .collect(
              Collectors.groupingBy(
                  c -> new CoordL(c.x() * 2 / width, c.y() * 2 / height), Collectors.counting()))
          .values()
          .stream()
          .mapToLong(s -> s)
          .peek(Util::log)
          .reduce((a, b) -> a * b)
          .orElse(0);
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      solveFromFile(INPUT_PATH, 101, 103);
    }

    static long solveFromFile(String input, int width, int height) {
      return solve(inputFromFile(input), width, height);
    }

    static long solve(List<RobotData> input, int width, int height) {
      return timeSolution(() -> solveInternal(input, width, height));
    }

    static long solveInternal(List<RobotData> input, int width, int height) {

      var charGrid = new CharGrid(new char[width * height], width, height, '\0', '.');
      var startCharGrid = new CharGrid(new char[width * height], width, height, '\0', '.');

      Map<Long, Long> herdings = new HashMap<>();

      Arrays.fill(startCharGrid.elements(), '.');

      input.stream()
          .collect(Collectors.groupingBy(c -> new CoordL(c.x(), c.y()), Collectors.counting()))
          .forEach((c, count) -> startCharGrid.set((int) c.x(), (int) c.y(), toChar(count)));

      long i = 0;
      do {
        i++;
        input = advance(input, 1, width, height);
        Arrays.fill(charGrid.elements(), '.');
        input.stream()
            .collect(Collectors.groupingBy(c -> new CoordL(c.x(), c.y()), Collectors.counting()))
            .forEach((c, count) -> charGrid.set((int) c.x(), (int) c.y(), toChar(count)));

        long herding = 0;

        for (RobotData robotData : input) {
          int x = (int) robotData.x();
          int y = (int) robotData.y();
          for (int[] ints : DIRS8) {
            if (charGrid.atSafe(x + ints[0], y + ints[1]) != '.') {
              herding += 1;
            }
          }
        }

        herdings.put(i, herding);

        // run until we reach the starting position
      } while (Arrays.compare(startCharGrid.elements(), charGrid.elements()) != 0);
      long seconds =
          herdings.entrySet().stream()
              .sorted((a, b) -> -Long.compare(a.getValue(), b.getValue()))
              .map(Map.Entry::getKey)
              .findFirst()
              .orElseThrow();
      input = advance(input, seconds, width, height);
      Arrays.fill(charGrid.elements(), '.');
      input.stream()
          .collect(Collectors.groupingBy(c -> new CoordL(c.x(), c.y()), Collectors.counting()))
          .forEach((c, count) -> charGrid.set((int) c.x(), (int) c.y(), toChar(count)));

      log(charGrid);

      return seconds;
    }
  }

  private static List<RobotData> advance(
      List<RobotData> input, long seconds, int width, int height) {
    return input.stream()
        .map(
            r ->
                new RobotData(
                    Math.floorMod(r.x() + r.vx() * seconds, width),
                    Math.floorMod(r.y() + r.vy() * seconds, height),
                    r.vx(),
                    r.vy()))
        .toList();
  }
}
