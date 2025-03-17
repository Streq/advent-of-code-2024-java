package advent.of.code.day12;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

  static final String EXAMPLE = exampleInputPath(Day12Test.class);
  static final String EXAMPLE1 = exampleInputPath(Day12Test.class, 1);
  static final String EXAMPLE2 = exampleInputPath(Day12Test.class, 2);

  @Test
  void testPart1() {
    assertEquals(140, Day12.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart1_1() {
    assertEquals(772, Day12.Part1.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart1_2() {
    assertEquals(1930, Day12.Part1.solveFromFile(EXAMPLE2));
  }

  @Test
  void testPart2() {
    assertEquals(80, Day12.Part2.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart2_1() {
    assertEquals(436, Day12.Part2.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart2_2() {
    assertEquals(1206, Day12.Part2.solveFromFile(EXAMPLE2));
  }

  @Test
  void testAliasMap() {
    AliasMap<Integer> map = new AliasMap<>();

    assertEquals(1, map.toOgId(1));
    assertEquals(2, map.toOgId(2));
    assertEquals(3, map.toOgId(3));
    assertEquals(4, map.toOgId(4));
    assertEquals(5, map.toOgId(5));
    assertEquals(6, map.toOgId(6));

    map.alias(1, 2);
    assertEquals(1, map.toOgId(1));
    assertEquals(1, map.toOgId(2));
    assertEquals(3, map.toOgId(3));
    assertEquals(4, map.toOgId(4));
    assertEquals(5, map.toOgId(5));
    assertEquals(6, map.toOgId(6));

    map.alias(2, 3);
    assertEquals(1, map.toOgId(1));
    assertEquals(1, map.toOgId(2));
    assertEquals(1, map.toOgId(3));
    assertEquals(4, map.toOgId(4));
    assertEquals(5, map.toOgId(5));
    assertEquals(6, map.toOgId(6));

    map.alias(4, 5);
    assertEquals(1, map.toOgId(1));
    assertEquals(1, map.toOgId(2));
    assertEquals(1, map.toOgId(3));
    assertEquals(4, map.toOgId(4));
    assertEquals(4, map.toOgId(5));
    assertEquals(6, map.toOgId(6));

    map.alias(4, 6);
    assertEquals(1, map.toOgId(1));
    assertEquals(1, map.toOgId(2));
    assertEquals(1, map.toOgId(3));
    assertEquals(4, map.toOgId(4));
    assertEquals(4, map.toOgId(5));
    assertEquals(4, map.toOgId(6));

    map.alias(1, 6);
    assertEquals(1, map.toOgId(1));
    assertEquals(1, map.toOgId(2));
    assertEquals(1, map.toOgId(3));
    assertEquals(1, map.toOgId(4));
    assertEquals(1, map.toOgId(5));
    assertEquals(1, map.toOgId(6));
  }
}
