package advent.of.code.day25;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static advent.of.code.util.Util.getFileAsString;
import static advent.of.code.util.Util.inputPath;

public class Day25 {

  private static long getFitting(Sheet sheet) {
    long count = 0;
    for (var key : sheet.keys()) {
      for (var lock : sheet.locks()) {
        if (key.fits(lock)) {
          System.out.printf("key %s fits lock %s%n", key, lock);
          count += 1;
        }
      }
    }
    return count;
  }

  record Schematic(int[] columns) {

    boolean fits(Schematic other) {
      for (int i = 0; i < this.columns.length; i++) {
        if (other.columns[i] + this.columns[i] > 5) {
          return false;
        }
      }
      return true;
    }

    public String toString() {
      return Arrays.toString(columns);
    }
  }

  record Sheet(List<Schematic> keys, List<Schematic> locks) {}

  private static final String INPUT_PATH = inputPath(Day25.class);

  static Sheet inputFromFile(String path) {
    List<Schematic> keys = new ArrayList<>();
    List<Schematic> locks = new ArrayList<>();

    Arrays.stream(getFileAsString(path).split("\\n\\n"))
        .forEach(
            s -> {
              var isLock = s.startsWith("#####");
              var columns = new int[5];
              var it = s.lines().skip(1).limit(5).iterator();
              while (it.hasNext()) {
                var line = it.next();
                for (int i = 0; i < 5; i++) {
                  if (line.charAt(i) == '#') {
                    columns[i]++;
                  }
                }
              }
              var sch = new Schematic(columns);
              if (isLock) {
                locks.add(sch);
              } else {
                keys.add(sch);
              }
            });

    var ret = new Sheet(Collections.unmodifiableList(keys), Collections.unmodifiableList(locks));

    System.out.printf(
        "%d keys, %d unique keys.%n", ret.keys().size(), ret.keys().stream().distinct().count());
    System.out.printf(
        "%d locks, %d unique locks.%n",
        ret.locks().size(), ret.locks().stream().distinct().count());

    return ret;
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Sheet sheet) {
      return getFitting(sheet);
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Object input) {
      return 0;
    }
  }
}
