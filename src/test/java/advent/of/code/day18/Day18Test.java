package advent.of.code.day18;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {

  static final String EXAMPLE = exampleInputPath(Day18Test.class);

  @Test
  void testPart1() {
    assertEquals(22, Day18.Part1.solveFromFile(EXAMPLE, 6, 12));
  }

  @Test
  void testPart2() {
    assertEquals("6,1", Day18.Part2.solveFromFile(EXAMPLE, 6, 12));
  }
}
