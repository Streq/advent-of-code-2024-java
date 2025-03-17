package advent.of.code.day6;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import advent.of.code.dayN.DayN;
import org.junit.jupiter.api.Test;

class Day6Test {

  static final String EXAMPLE = exampleInputPath(Day6Test.class);

  @Test
  void testPart1() {
    assertEquals(41, Day6.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(6, Day6.Part2.solveFromFile(EXAMPLE));
  }
}
