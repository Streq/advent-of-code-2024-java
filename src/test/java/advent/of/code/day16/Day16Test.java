package advent.of.code.day16;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {

  static final String EXAMPLE = exampleInputPath(Day16Test.class);
  static final String EXAMPLE1 = exampleInputPath(Day16Test.class, 1);

  @Test
  void testPart1() {
    assertEquals(7036, Day16.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart1_1() {
    assertEquals(11048, Day16.Part1.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart2() {
    assertEquals(45, Day16.Part2.solveFromFile(EXAMPLE));
  }
  @Test
  void testPart2_1() {
    assertEquals(64, Day16.Part2.solveFromFile(EXAMPLE1));
  }
}
