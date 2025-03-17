package advent.of.code.day21;

import org.junit.jupiter.api.Test;

import static advent.of.code.day21.Day21.getLength;
import static advent.of.code.util.Util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {

  static final String EXAMPLE = exampleInputPath(Day21Test.class);

  @Test
  void testIndirectionExpander() {

    assertEquals(68, getLength("029A", 2));
    assertEquals(60, getLength("980A", 2));
    assertEquals(68, getLength("179A", 2));
    assertEquals(64, getLength("456A", 2));
    assertEquals(64, getLength("379A", 2));

    assertEquals(74, getLength("593A", 2));
    assertEquals(68, getLength("283A", 2));
    assertEquals(68, getLength("670A", 2));
    assertEquals(74, getLength("459A", 2));
    assertEquals(72, getLength("279A", 2));
  }

  @Test
  void testPart1() {
    assertEquals(126384, Day21.Part1.solveFromFile(EXAMPLE));
  }
}
