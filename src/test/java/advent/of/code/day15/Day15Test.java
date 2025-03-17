package advent.of.code.day15;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {

  static final String EXAMPLE = exampleInputPath(Day15Test.class);
  static final String EXAMPLE1 = exampleInputPath(Day15Test.class, 1);
  static final String EXAMPLE2 = exampleInputPath(Day15Test.class, 2);

  @Test
  void testPart1() {
    assertEquals(2028, Day15.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart1_1() {
    assertEquals(10092, Day15.Part1.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart2() {
    assertEquals(9021, Day15.Part2.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart2_2() {
    assertEquals(0, Day15.Part2.solveFromFileNoConvert(EXAMPLE2));
  }
}
