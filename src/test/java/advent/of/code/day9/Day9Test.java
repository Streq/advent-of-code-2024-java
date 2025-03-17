package advent.of.code.day9;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import advent.of.code.dayN.DayN;
import org.junit.jupiter.api.Test;

class Day9Test {

  static final String EXAMPLE = exampleInputPath(Day9Test.class);
  static final String EXAMPLE1 = exampleInputPath(Day9Test.class,1);

  @Test
  void testPart1() {
    assertEquals(1928, Day9.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(2858, Day9.Part2.solveFromFile(EXAMPLE));
  }
  @Test
  void testPart2_upping() {
    assertEquals(97898222299196L, Day9.Part2.solveFromFile(EXAMPLE1));
  }
}
