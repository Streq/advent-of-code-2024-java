package advent.of.code.day5;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;

class Day5Test {

  static final String EXAMPLE = exampleInputPath(Day5Test.class);

  @Test
  void testInputFromFile() {
    var ret = Day5.inputFromFile(EXAMPLE);

    assertEquals(21, ret.rules().size());
    assertEquals(6, ret.updates().size());
    assertTrue(ret.rules().contains(new Day5.Rule(47, 53))); // a rule
    assertTrue(ret.rules().contains(new Day5.Rule(53, 13))); // last rule
    assertTrue(ret.updates().contains(List.of(75, 47, 61, 53, 29))); // a update
    assertTrue(ret.updates().contains(List.of(97, 13, 75, 29, 47))); // last update
  }

  @Test
  void testPart1() {
    assertEquals(143, Day5.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(123, Day5.Part2.solveFromFile(EXAMPLE));
  }
}
