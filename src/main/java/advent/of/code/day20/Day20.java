package advent.of.code.day20;

import advent.of.code.util.CharGrid;
import advent.of.code.util.IntGrid;

import java.util.*;

import static advent.of.code.util.Util.*;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class Day20 {

  private static final String INPUT_PATH = inputPath(Day20.class);

  static CharGrid inputFromFile(String path) {
    return CharGrid.from(getFileAsString(path), '#');
  }

  record ShortCut(int x, int y, int x1, int y1) {}

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH, 100, 2));
    }

    static long solveFromFile(String input, long minStepSave, int stepsAllowed) {
      return solve(inputFromFile(input), minStepSave, stepsAllowed);
    }

    static long solve(CharGrid input, long minStepSave, int stepsAllowed) {
      return timeSolution(() -> solveInternal(input, minStepSave, stepsAllowed));
    }

    static long solveInternal(CharGrid input, long minStepSave, int stepsAllowed) {

      var solveCounter = new HashMap<Long, Long>();

      var p0 = input.find('S');

      var paths = input.findPaths(p0[0], p0[1], new char[] {'#'});
      long maxWeight = 0;

      var p1 = new int[2];

      for (int y = 0; y < paths.height(); y++) {
        for (int x = 0; x < paths.width(); x++) {
          long at = paths.at(x, y);
          if (at > maxWeight) {
            maxWeight = at;
            p1[0] = x;
            p1[1] = y;
          }
        }
      }

      logPaths(paths, maxWeight);

      var path = findPath(paths, p1[0], p1[1]);
      for (int idx = path.size() - 1; idx >= 0; idx--) {
        Coord p = path.get(idx);

        int x = p.x();
        int y = p.y();
        var steps0 = paths.at(x, y);

        if (steps0 == -1) {
          continue;
        }

        for (int j = -stepsAllowed; j <= stepsAllowed; j++) {
          int absj = abs(j);
          int remaining = stepsAllowed - absj;
          for (int i = -remaining; i <= remaining; i++) {
            int x1 = x + i;
            int y1 = y + j;
            var steps1 = paths.atSafe(x1, y1);
            if (steps1 == -1) {
              continue;
            }
            int stepsTakenDuringCheat = abs(i) + absj;
            var savedSteps = steps1 - (steps0 + stepsTakenDuringCheat);
            if (savedSteps < minStepSave) {
              continue;
            }

            //logSolution(paths, maxWeight, new ShortCut(x, y, x1, y1), savedSteps);

            solveCounter.put(savedSteps, solveCounter.getOrDefault(savedSteps, 0L) + 1);
          }
        }
      }
      solveCounter.entrySet().stream()
          .sorted(Comparator.comparingLong(Map.Entry::getValue))
          .sorted(Comparator.comparingLong(Map.Entry::getKey))
          .forEachOrdered(
              e -> {
                long n = e.getValue();
                long s = e.getKey();
                if (n == 1L) {
                  log("There is one cheat that saves %d picoseconds.", e.getKey());
                } else {
                  log("There are %d cheats that save %d picoseconds.", e.getValue(), e.getKey());
                }
              });

      return solveCounter.values().stream().mapToLong(l -> l).sum();
    }
  }

  private static void logPaths(IntGrid paths, long maxWeight) {
    log(
        paths.toString(
            n -> {
              if (n < 0) {
                return "#";
              } else if (n == 0) {
                return ANSI_BRIGHT_GREEN_BACKGROUND + ANSI_BLACK + 'S' + ANSI_RESET;
              } else if (n == maxWeight) {
                return ANSI_BRIGHT_CYAN_BACKGROUND + ANSI_BLACK + 'E' + ANSI_RESET;
              } else {
                int color = (n.intValue() / INT_TO_CHAR.length()) % (RAINBOW_COLORS.size());

                return RAINBOW_COLORS.get(color) + ANSI_BLACK_BACKGROUND + toChar(n) + ANSI_RESET;
              }
            }));
  }

  private static void logSolution(IntGrid paths, long maxWeight, ShortCut node, long savedSteps) {
    var d = paths.copy();
    var sx = Integer.signum(node.x1 - node.x);
    var sy = Integer.signum(node.y1 - node.y);
    var val = 0;
    d.set(node.x, node.y, -2);
    //    for (int i = node.x; i != node.x1; i += sx) {
    //      if (val != 0) {
    //        d.set(i, node.y, val - 2);
    //      }
    //      --val;
    //    }
    //    for (int i = node.y; i != node.y1; i += sy) {
    //      if (val != 0) {
    //        d.set(node.x1, i, val - 2);
    //      }
    //      --val;
    //    }
    d.set(node.x1, node.y1, -3);
    log("saved us %d steps", savedSteps);
    log(
        d.toString(
            w -> {
              if (w <= -2) {
                return ANSI_BLACK + ANSI_BRIGHT_YELLOW_BACKGROUND + toChar(-w - 2) + ANSI_RESET;
              } else if (w == 0) {
                return ANSI_BRIGHT_GREEN_BACKGROUND + ANSI_BLACK + 'S' + ANSI_RESET;
              } else if (w == maxWeight) {
                return ANSI_BRIGHT_CYAN_BACKGROUND + ANSI_BLACK + 'E' + ANSI_RESET;
              } else if (w < 0) {
                return "#";
              } else {
                int color = (w.intValue() / INT_TO_CHAR.length()) % (RAINBOW_COLORS.size());

                return RAINBOW_COLORS.get(color) + ANSI_BLACK_BACKGROUND + toChar(w) + ANSI_RESET;
              }
            }));
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(Part1.solveFromFile(INPUT_PATH, 100, 20));
    }
  }
}
