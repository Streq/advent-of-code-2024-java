package advent.of.code.day11;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

  static final String EXAMPLE = exampleInputPath(Day11Test.class);

  @Test
  void testPart1() {
    assertEquals(2, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 0));
    assertEquals(3, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 1));
    assertEquals(4, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 2));
    assertEquals(5, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 3));
    assertEquals(9, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 4));
    assertEquals(13, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 5));
    assertEquals(22, Day11.Part1.solveFromFileCustomBlinks(EXAMPLE, 6));
    assertEquals(55312, Day11.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(65601038650482L, Day11.Part2NonRecursive.solveFromFile(EXAMPLE));
  }
}
