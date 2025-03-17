package advent.of.code.day7;

import static advent.of.code.day7.Operator.MUL;
import static advent.of.code.day7.Operator.SUM;
import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

class Day7Test {

  static final String EXAMPLE = exampleInputPath(Day7Test.class);

  @Test
  void testCombinations() {
    assertEquals(
        Arrays.deepToString(
            new Operator[][] {
              {SUM, SUM, SUM},
              {MUL, SUM, SUM},
              {SUM, MUL, SUM},
              {MUL, MUL, SUM},
              {SUM, SUM, MUL},
              {MUL, SUM, MUL},
              {SUM, MUL, MUL},
              {MUL, MUL, MUL}
            }),
        Arrays.deepToString(Operator.combinations(3, SUM, MUL)));
  }

  @Test
  void testPart1() {
    assertEquals(3749, Day7.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    assertEquals(11387, Day7.Part2.solveFromFile(EXAMPLE));
  }
}
