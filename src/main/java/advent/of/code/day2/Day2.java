package advent.of.code.day2;

import java.util.ArrayList;
import java.util.List;

import static advent.of.code.util.Util.inputPath;

public class Day2 {
  private static final String INPUT_PATH = inputPath(Day2.class);

  public static class Part1 {

    public static void main(String[] args) {
      List<Report> reports = ReportLoader.loadReports(INPUT_PATH);
      System.out.println(getSafeReports(reports));
    }

    public static long getSafeReports(List<Report> reports) {
      return reports.stream().filter(Part1::isSafe).count();
    }

    private static boolean isSafe(Report report) {
      return areLevelsSafe(report.levels());
    }
  }

  public static class Part2 {

    public static void main(String[] args) {
      List<Report> reports = ReportLoader.loadReports(INPUT_PATH);
      System.out.println(getDampenedSafeReports(reports));
    }

    public static long getDampenedSafeReports(List<Report> reports) {
      return reports.stream().filter(Part2::isDampenedSafe).count();
    }

    private static boolean isDampenedSafe(Report report) {
      return areLevelsSafeDampened(report.levels());
    }
  }

  /// A report only counts as safe if both of the following are true:
  /// - The levels are either all increasing or all decreasing.
  /// - Any two adjacent levels differ by at least one and at most three.
  private static boolean areLevelsSafe(List<Integer> levels) {
    if (levels.size() < 2) {
      return true;
    }

    var previousDiff = 0;

    var a = levels.getFirst();
    for (var i = 1; i < levels.size(); ++i) {
      var b = levels.get(i);
      var diff = a - b;
      var abs = Math.abs(diff);

      if (abs < 1 || abs > 3) {
        return false;
      }

      if (diff * previousDiff < 0) {
        return false;
      }

      previousDiff = diff;

      a = b;
    }

    return true;
  }

  /// Now, the same rules apply as before, except if removing a single level from an unsafe report
  /// would make it safe, the report instead counts as safe.
  private static boolean areLevelsSafeDampened(List<Integer> levels) {
    if (areLevelsSafe(levels)) {
      return true;
    }

    List<Integer> copy = new ArrayList<>(levels.size());
    for (int i = 0; i < levels.size(); ++i) {
      copy.addAll(levels);
      copy.remove(i);
      if (areLevelsSafe(copy)) {
        return true;
      }
      copy.clear();
    }

    return false;
  }
}
