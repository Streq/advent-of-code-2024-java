package advent.of.code.day17;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

  static final String EXAMPLE = exampleInputPath(Day17Test.class);
  static final String EXAMPLE1 = exampleInputPath(Day17Test.class, 1);
  static final String EXAMPLE2 = exampleInputPath(Day17Test.class, 2);

  @Test
  void testPart1() {
    assertEquals("4,6,3,5,6,3,5,2,1,0", Day17.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(117440, Day17.Part2.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart2_proof() {
    assertEquals("0,3,5,4,3,0", Day17.Part1.solveFromFile(EXAMPLE2));
  }
}
