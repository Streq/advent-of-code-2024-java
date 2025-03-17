package advent.of.code.day25;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import advent.of.code.dayN.DayN;
import org.junit.jupiter.api.Test;

class Day25Test {

  static final String EXAMPLE = exampleInputPath(Day25Test.class);

  @Test
  void testPart1() {
    assertEquals(3, Day25.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(0, Day25.Part2.solveFromFile(EXAMPLE));
  }
}
