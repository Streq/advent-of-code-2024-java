package advent.of.code.day20;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test {

  static final String EXAMPLE = exampleInputPath(Day20Test.class);

  @Test
  void testPart1() {
    assertEquals(44, Day20.Part1.solveFromFile(EXAMPLE, 1, 2));
  }

  @Test
  void testPart2() {
    assertEquals(285, Day20.Part1.solveFromFile(EXAMPLE, 50, 20));
  }
}
