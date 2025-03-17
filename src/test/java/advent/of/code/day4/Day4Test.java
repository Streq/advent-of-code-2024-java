package advent.of.code.day4;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {

  static final String EXAMPLE = exampleInputPath(Day4Test.class);

  @Test
  void testPart1() {
    assertEquals(18, Day4.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(9, Day4.Part2.solveFromFile(EXAMPLE));
  }
}
