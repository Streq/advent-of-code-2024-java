package advent.of.code.day13;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static advent.of.code.util.Util.*;

public class Day13 {

  private static final String INPUT_PATH = inputPath(Day13.class);

  record MachineData(long ax, long ay, long bx, long by, long px, long py) {

    public String toString() {
      return ("(%d,%d)=A(%d,%d) + B(%d,%d)".formatted(px, py, ax, ay, bx, by));
    }

    CoordL solve2() {
      return new MachineData(ax, ay, bx, by, px + 10000000000000L, py + 10000000000000L).solve();
    }

    CoordL solve() {
      // System of equations
      // (1): px == ax*a + bx*b
      // (2): py == ay*a + by*b
      // a >= 0 && a <= 100
      // b >= 0 && b <= 100

      // either:
      // - has no solution
      // - has one solution
      // - has infinite solutions (can't happen)

      // solution:
      // px == ax*a + bx*b
      // px-bx*b == ax*a
      // (px-bx*b)/ax == a

      // replace
      // py == ay*((px-bx*b)/ax) + by*b
      // py == ay*px/ax - ay*bx*b/ax + by*b
      // py - ay*px/ax == b*(-ay*bx/ax + by)
      // (py - ay*px/ax) / (-ay*bx/ax + by) == b
      // multiply each side of the division by ax to avoid rounding
      // (py*ax - ay*px) / (-ay*bx + by*ax) == b;
      long b = (py * ax - ay * px) / (-ay * bx + by * ax);
      long a = (px - bx * b) / ax;

      CoordL res = new CoordL(a, b);
      // check initial equations
      // (1): px == ax*a + bx*b
      if (px != ax * a + bx * b) {
        log("failed %s: %s, px != ax * a + bx * b", this, res);
        return null;
      }
      // (2): py == ay*a + by*b
      if (py != ay * a + by * b) {
        log("failed %s: %s, py != ay * a + by * b", this, res);
        return null;
      }
      // a >= 0 && a <= 100
      if (a < 0) {
        log("failed %s: %s, a < 0", this, res);
        return null;
      }
      // b >= 0 && b <= 100
      if (b < 0) {
        log("failed %s: %s, b < 0", this, res);
        return null;
      }
      log("SOLVED %s: %s", this, res);
      return res;
    }
  }

  static final Pattern REGEX =
      Pattern.compile(
          """
          Button A: X\\+([0-9]+), Y\\+([0-9]+)
          Button B: X\\+([0-9]+), Y\\+([0-9]+)
          Prize: X=([0-9]+), Y=([0-9]+)""");

  static List<MachineData> inputFromFile(String path) {
    var str = getFileAsString(path);
    return REGEX
        .matcher(str)
        .results()
        .map(
            r ->
                Stream.of(r.group(1), r.group(2), r.group(3), r.group(4), r.group(5), r.group(6))
                    .mapToLong(Long::parseUnsignedLong)
                    .toArray())
        .map(arr -> new MachineData(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]))
        .toList();
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(List<MachineData> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(List<MachineData> input) {
      return input.stream()
          .map(MachineData::solve)
          .filter(Objects::nonNull)
          .mapToLong(c -> c.x() * 3 + c.y())
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

    static long solve(List<MachineData> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(List<MachineData> input) {
      return input.stream()
          .map(MachineData::solve2)
          .filter(Objects::nonNull)
          .mapToLong(c -> c.x() * 3 + c.y())
          .sum();
    }
  }
}
