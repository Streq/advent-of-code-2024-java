package advent.of.code.day1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static advent.of.code.util.Util.inputPath;

public class Day1 {

  private static final String INPUT_PATH = inputPath(Day1.class);

  public static class Part1 {

    public static void main(String[] args) {
      var listPair = ListPairLoader.load(INPUT_PATH);
      System.out.println(getTotalDistance(listPair.left(), listPair.right()));
    }
  }

  public static class Part2 {
    public static void main(String[] args) {
      var listPair = ListPairLoader.load(INPUT_PATH);
      System.out.println(getSimilarityScore(listPair.left(), listPair.right()));
    }
  }

  public static long getTotalDistance(List<Integer> left, List<Integer> right) {
    // Pair up the smallest number in the left list with the smallest number in the right list, then
    // the second-smallest left number with the second-smallest right number, and so on
    left.sort(Integer::compareTo);
    right.sort(Integer::compareTo);

    // Within each pair, figure out _how far apart_ the two numbers are; you'll need to _add up all
    // of those distances_.
    var totalDistance = 0;
    for (var i = 0; i < left.size(); ++i) {
      var distance = Math.abs(left.get(i) - right.get(i));
      totalDistance += distance;
    }
    return totalDistance;
  }

  public static long getSimilarityScore(List<Integer> left, List<Integer> right) {
    // Calculate a total similarity score by adding up each number in the left list after
    // multiplying it by the number of times that number appears in the right list.

    Map<Integer, Long> appearances = new HashMap<>();

    long similarityScore = 0;

    for (var num : left) {
      long appearancesInRight;
      if (appearances.containsKey(num)) {
        appearancesInRight = appearances.get(num);
      } else {
        appearancesInRight = right.stream().filter(num::equals).count();
        appearances.put(num, appearancesInRight);
      }

      similarityScore += num * appearancesInRight;
    }

    return similarityScore;
  }
}
