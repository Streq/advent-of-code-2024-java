package advent.of.code.day13;

import advent.of.code.util.Util;
import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

  static final String EXAMPLE = exampleInputPath(Day13Test.class);

  @Test
  void testPart1() {
    var input = Day13.inputFromFile(EXAMPLE);
    var m0 = input.get(0);
    var m1 = input.get(1);
    var m2 = input.get(2);
    var m3 = input.get(3);
    assertEquals(new Day13.MachineData(94, 34, 22, 67, 8400, 5400), m0);
    assertEquals(new Day13.MachineData(26, 66, 67, 21, 12748, 12176), m1);
    assertEquals(new Day13.MachineData(17, 86, 84, 37, 7870, 6450), m2);
    assertEquals(new Day13.MachineData(69, 23, 27, 71, 18641, 10279), m3);

    assertEquals(new Util.CoordL(80, 40), m0.solve());
    assertNull(m1.solve());
    assertEquals(new Util.CoordL(38, 86), m2.solve());
    assertNull(m3.solve());

    assertEquals(480, Day13.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    var input = Day13.inputFromFile(EXAMPLE);
    var m0 = input.get(0);
    var m1 = input.get(1);
    var m2 = input.get(2);
    var m3 = input.get(3);

    assertNull(m0.solve2());
    assertNotNull(m1.solve2());
    assertNull(m2.solve2());
    assertNotNull(m3.solve2());
  }
}
