package advent.of.code.day22;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test {
  static final String EXAMPLE = exampleInputPath(Day22Test.class);

  static final String EXAMPLE2 = exampleInputPath(Day22Test.class, 2);

  @Test
  void testPart1() {
    assertEquals(37327623, Day22.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(23, Day22.Part2.solveFromFile(EXAMPLE2));
  }
}
