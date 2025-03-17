package advent.of.code.day3;

import advent.of.code.util.Util;
import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day3Test {

  static final String EXAMPLE = exampleInputPath(Day3Test.class);

  @Test
  void testPart1() {
    String text = Util.getFileAsString(EXAMPLE);
    var res = Day3.Part1.sumValidMuls(text);
    assertEquals(161, res);
  }
  @Test
  void testPart2() {
    String text = Util.getFileAsString(EXAMPLE);
    var res = Day3.Part2.sumValidMulsWithConditionals(text);
    assertEquals(48, res);
  }
}
