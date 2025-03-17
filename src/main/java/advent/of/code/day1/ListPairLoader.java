package advent.of.code.day1;

import advent.of.code.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ListPairLoader {

  /**
   * @return a pair of mutable integer lists
   */
  public static ListPair load(String path) {
    List<String> lines = Util.getFileAsLines(path);
    List<Integer> left = new ArrayList<>(lines.size());
    List<Integer> right = new ArrayList<>(lines.size());
    for (var line : lines) {
      var split = line.split(" +");
      addToList(left, split[0]);
      addToList(right, split[1]);
    }

    return new ListPair(left, right);
  }

  private static void addToList(List<Integer> left, String s) {
    left.add(Integer.parseInt(s));
  }

  private ListPairLoader() {}
}
