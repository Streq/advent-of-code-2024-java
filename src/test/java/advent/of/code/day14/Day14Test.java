package advent.of.code.day14;

import org.junit.jupiter.api.Test;

import java.util.List;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

  static final String EXAMPLE = exampleInputPath(Day14Test.class);

  @Test
  void testPart1() {
    assertEquals(12, Day14.Part1.solveFromFile(EXAMPLE, 100, 11, 7));
  }

  @Test
  void testPart2_1() {
    assertEquals(12, Day14.Part2.solve(List.of(new Day14.RobotData(2, 4, 2, -3)), 11, 7));
  }

  @Test
  void testPart2() {
    assertEquals(12, Day14.Part2.solveFromFile(EXAMPLE, 11, 7));
  }
}
