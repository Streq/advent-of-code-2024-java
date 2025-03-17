package advent.of.code.day23;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day23Test {

  static final String EXAMPLE = exampleInputPath(Day23Test.class);

  @Test
  void testPart1() {
    assertEquals(7, Day23.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals("co,de,ka,ta", Day23.Part2.solveFromFile(EXAMPLE));
  }
}
