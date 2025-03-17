package advent.of.code.day10;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import advent.of.code.dayN.DayN;
import org.junit.jupiter.api.Test;

class Day10Test {

  static final String EXAMPLE = exampleInputPath(Day10Test.class);

  @Test
  void testPart1() {
    assertEquals(36, Day10.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(81, Day10.Part2.solveFromFile(EXAMPLE));
  }
}
