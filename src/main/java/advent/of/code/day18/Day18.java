package advent.of.code.day18;

import advent.of.code.util.CharGrid;
import advent.of.code.util.IntGrid;
import advent.of.code.util.Util;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static advent.of.code.util.Util.*;

public class Day18 {

  private static final String INPUT_PATH = inputPath(Day18.class);

  static CharGrid inputFromFile(String path, int size, int bytes) {
    var ret = new CharGrid(size + 1, size + 1, '#', '.');

    getEntries(path).limit(bytes).forEach(r -> ret.setSafe(r.x(), r.y(), '#'));
    log(ret);
    return ret;
  }

  private static Stream<Coord> getEntries(String path) {
    return Pattern.compile("([0-9]+),([0-9]+)")
        .matcher(getFileAsString(path))
        .results()
        .map(r -> new Coord(Integer.parseInt(r.group(1)), Integer.parseInt(r.group(2))));
  }

  private static List<Coord> findPath(IntGrid costs, int x, int y) {
    List<Coord> ret = new ArrayList<>();
    long cost = costs.at(x, y);
    while (cost != 0) {
      ret.add(new Coord(x, y));
      int nx = x;
      int ny = y;
      for (int[] dir : DIRS) {
        int x1 = x + dir[0];
        int y1 = y + dir[1];
        var cost1 = costs.atSafe(x1, y1);
        if (cost1 != -1 && cost1 < cost) {
          cost = cost1;
          nx = x1;
          ny = y1;
        }
      }
      x = nx;
      y = ny;
    }
    ret.add(new Coord(x, y));
    return ret;
  }

  private static IntGrid findPaths(CharGrid input, int x0, int y0, char[] disallowedTerrain) {

    var res = new IntGrid(input.width(), input.height(), Long.MAX_VALUE, -1);
    String disallowed = String.valueOf(disallowedTerrain);

    input.forEach(
        (x, y, c) -> {
          if (disallowed.indexOf(c) != -1) {
            res.set(x, y, -1);
          }
          return false;
        });

    Deque<Coord> nodes = new ArrayDeque<>();

    res.set(x0, y0, 0);

    nodes.add(new Coord(x0, y0));

    while (!nodes.isEmpty()) {
      var node = nodes.removeLast();
      int x = node.x();
      int y = node.y();
      long cost = res.at(x, y);
      for (int[] dir : DIRS) {
        int xn = x + dir[0];
        int yn = y + dir[1];
        long costNext = res.atSafe(xn, yn);
        long newCost = cost + 1;
        if (costNext > newCost) {
          res.set(xn, yn, newCost);
          nodes.add(new Coord(xn, yn));
        }
      }
    }

    return res;
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH, 70, 1024));
    }

    static long solveFromFile(String input, int size, int bytes) {
      return solve(inputFromFile(input, size, bytes), 0, 0, size, size);
    }

    static long solve(CharGrid input, int x0, int y0, int x1, int y1) {
      return timeSolution(() -> solveInternal(input, x0, y0, x1, y1));
    }

    static long solveInternal(CharGrid input, int x0, int y0, int x1, int y1) {
      var ret = findPaths(input, x0, y0, new char[] {'#'});

      var path = findPath(ret, x1, y1);

      var toPrint = input.copy();
      for (Coord coord : path) {
        toPrint.set(coord.x(), coord.y(), 'O');
      }
      log(toPrint);

      return ret.at(x1, y1);
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH, 70, 1000));
    }

    static String solveFromFile(String input, int size, int bytes) {
      return solve(
          inputFromFile(input, size, bytes),
          getEntries(input).skip(bytes).toList(),
          0,
          0,
          size,
          size);
    }

    static String solve(CharGrid input, List<Coord> entries, int x0, int y0, int x1, int y1) {
      return timeSolution(() -> solveInternal(input, entries, x0, y0, x1, y1));
    }

    static String solveInternal(
        CharGrid input, List<Coord> entries, int x0, int y0, int x1, int y1) {

      var it = entries.iterator();
      Coord next = null;
      var weightGrid = findPaths(input, x0, y0, new char[] {'#', '+'});
      while (weightGrid.at(x1, y1) != Long.MAX_VALUE && it.hasNext()) {
        next = it.next();
        input.set(next.x(), next.y(), '+');
        // weightGrid = findPaths(input, x0, y0, new char[] {'#', '+'});

        introduceWall(weightGrid, next.x(), next.y());
      }

      if (next != null) {
        input.set(next.x(), next.y(), 'O');
      }

      log(
          input
              .toString()
              .replaceAll("(\\++)", ANSI_BRIGHT_YELLOW + "$1" + ANSI_RESET)
              .replaceAll("O", ANSI_BLACK + ANSI_BRIGHT_GREEN_BACKGROUND + "#" + ANSI_RESET));
      return next != null ? next.x() + "," + next.y() : "";
    }

    private static void introduceWall(IntGrid weightGrid, int x, int y) {
      long oldWeight = weightGrid.atSafe(x, y);
      // log("introducing wall %d,%d where the weight was %d", x, y, oldWeight);
      if (oldWeight == -1) {
        return;
      }

      Deque<Coord> queue = new ArrayDeque<>();
      visitNextNodes(weightGrid, x, y, queue);
      weightGrid.set(x, y, -1);

      while (!queue.isEmpty()) {
        var pos = queue.removeFirst();
        var weight = weightGrid.at(pos.x(), pos.y());
        // log("processing %s", pos);
        if (!isOrigin(weightGrid, pos) && isLocalMin(weightGrid, pos)) {
          // log("it's now orphaned, making infinite and visiting neighbors");
          makeInfiniteWeight(weightGrid, pos);
          visitLowerNeighbors(weightGrid, pos, queue);
        } else {
          // log("it's not orphaned, fixing weight", weight);
          fixWeight(weightGrid, pos);
          visitHigherNeighbors(weightGrid, pos, queue);
        }
      }
    }

    private static void visitNextNodes(IntGrid weightGrid, int x, int y, Deque<Coord> queue) {
      var weight = weightGrid.at(x, y);
      for (int[] dir : DIRS) {
        int x1 = x + dir[0];
        int y1 = y + dir[1];
        if (weightGrid.atSafe(x1, y1) == weight + 1) {
          // log("adding next node %d,%d because its weight is %d", x1, y1, weight + 1);
          queue.add(new Coord(x1, y1));
        }
      }
    }

    private static void visitHigherNeighbors(IntGrid weightGrid, Coord pos, Deque<Coord> queue) {
      long weight = weightGrid.at(pos.x(), pos.y());
      long nextWeight = weight + 1;
      for (int[] dir : DIRS) {
        int x = pos.x() + dir[0];
        int y = pos.y() + dir[1];
        long weight1 = weightGrid.atSafe(x, y);
        if (weight1 > nextWeight) {
          weightGrid.set(x, y, nextWeight);
          queue.add(new Coord(x, y));
        }
      }
    }

    private static void fixWeight(IntGrid weightGrid, Coord pos) {
      var minWeight = Long.MAX_VALUE;
      for (int[] dir : DIRS) {
        long weight1 = weightGrid.atSafe(pos.x() + dir[0], pos.y() + dir[1]);
        if (weight1 < 0) {
          continue;
        }
        minWeight = Math.min(minWeight, weight1);
      }
      if (minWeight != Long.MAX_VALUE) {
        // log("weight is now %d", minWeight + 1);
        weightGrid.set(pos.x(), pos.y(), minWeight + 1);
      }
    }

    private static void visitLowerNeighbors(IntGrid weightGrid, Coord pos, Deque<Coord> queue) {
      for (int[] dir : DIRS) {
        int x = pos.x() + dir[0];
        int y = pos.y() + dir[1];
        long weight = weightGrid.atSafe(x, y);
        if (weight > 0 && weight < Long.MAX_VALUE) {
          // log("adding neighbor %d,%d that has weight %d", x, y, weight);
          queue.add(new Coord(x, y));
        }
      }
    }

    private static void makeInfiniteWeight(IntGrid weightGrid, Coord pos) {
      weightGrid.setSafe(pos.x(), pos.y(), Long.MAX_VALUE);
    }

    private static boolean isOrigin(IntGrid weightGrid, Coord pos) {
      return weightGrid.atSafe(pos.x(), pos.y()) == 0;
    }

    private static boolean isLocalMin(IntGrid weightGrid, Coord pos) {
      var weight = weightGrid.at(pos.x(), pos.y());
      var minWeight = weight;
      for (int[] dir : DIRS) {
        long weight1 = weightGrid.atSafe(pos.x() + dir[0], pos.y() + dir[1]);
        if (weight1 == -1) {
          continue;
        }
        minWeight = Math.min(minWeight, weight1);
      }
      return minWeight >= weight;
    }
  }
}
