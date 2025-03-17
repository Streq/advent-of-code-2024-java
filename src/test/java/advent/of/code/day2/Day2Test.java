package advent.of.code.day2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static advent.of.code.day2.ReportLoader.loadReports;
import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {

  static final String EXAMPLE = exampleInputPath(Day2Test.class);

  @Test
  void testPart1() {
    List<Report> reports = loadReports(EXAMPLE);

    long safeReports = Day2.Part1.getSafeReports(reports);

    assertEquals(2, safeReports);
  }


  @Test
  void testPart2() {
    List<Report> reports = loadReports(EXAMPLE);

    long safeReports = Day2.Part2.getDampenedSafeReports(reports);

    assertEquals(4, safeReports);
  }

  @Test
  void reportLoaderTest() {
    List<Report> reports = loadReports(EXAMPLE);
    assertEquals(List.of(7, 6, 4, 2, 1), reports.get(0).levels());
    assertEquals(List.of(1, 2, 7, 8, 9), reports.get(1).levels());
    assertEquals(List.of(9, 7, 6, 2, 1), reports.get(2).levels());
    assertEquals(List.of(1, 3, 2, 4, 5), reports.get(3).levels());
    assertEquals(List.of(8, 6, 4, 4, 1), reports.get(4).levels());
    assertEquals(List.of(1, 3, 6, 7, 9), reports.get(5).levels());
  }
}
