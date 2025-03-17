package advent.of.code.dayN;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DayNTest {

  static final String EXAMPLE = exampleInputPath(DayNTest.class);

  @Test
  void testPart1() {
    assertEquals(0, DayN.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(0, DayN.Part2.solveFromFile(EXAMPLE));
  }
}
