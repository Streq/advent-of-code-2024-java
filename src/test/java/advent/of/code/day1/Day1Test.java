package advent.of.code.day1;

import org.junit.jupiter.api.Test;

import static advent.of.code.util.Util.exampleInputPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test {

  static final String EXAMPLE = exampleInputPath(Day1Test.class);

  @Test
  void testPart1() {
    var listPair = ListPairLoader.load(EXAMPLE);
    var totalDistance = Day1.getTotalDistance(listPair.left(), listPair.right());
    assertEquals(11, totalDistance);
  }

  @Test
  void testPart2() {
    var listPair = ListPairLoader.load(EXAMPLE);
    var similarityScore = Day1.getSimilarityScore(listPair.left(), listPair.right());
    assertEquals(31, similarityScore);
  }
}
