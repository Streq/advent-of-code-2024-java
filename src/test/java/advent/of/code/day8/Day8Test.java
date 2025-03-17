package advent.of.code.day8;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import advent.of.code.dayN.DayN;
import org.junit.jupiter.api.Test;

class Day8Test {

  static final String EXAMPLE = exampleInputPath(Day8Test.class);

  @Test
  void testPart1() {
    assertEquals(14, Day8.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2() {
    Day8.Result result = Day8.Part2.solveFromFile(EXAMPLE);
    assertEquals(34, result.count());

    var g = result.antinodes().copy();

    Day8.overwrite(result.grid().elements(), g.elements(), new char[] {'.'});

    assertEquals(
        """
##....#....#
.#.#....0...
..#.#0....#.
..##...0....
....0....#..
.#...#A....#
...#..#.....
#....#.#....
..#.....A...
....#....A..
.#........#.
...#......##
""",
        g.toString());
  }
}
