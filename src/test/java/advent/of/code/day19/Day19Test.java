package advent.of.code.day19;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test {

  static final String EXAMPLE = exampleInputPath(Day19Test.class);

  @Test
  void testPart1() {
    assertEquals(6, Day19.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(16, Day19.Part2.solveFromFile(EXAMPLE));
  }
}
