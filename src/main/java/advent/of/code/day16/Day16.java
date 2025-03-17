package advent.of.code.day16;

import advent.of.code.util.CharGrid;
import advent.of.code.util.IntGrid;

import java.util.*;
import java.util.stream.LongStream;

import static advent.of.code.util.Util.*;

public class Day16 {
  private static final boolean debugging = false;

  private static final String INPUT_PATH = inputPath(Day16.class);
  public static final char WALL = '#';
  public static final int RIGHT = 0;
  public static final int DOWN = 1;
  public static final int LEFT = 2;
  public static final int UP = 3;

  static CharGrid inputFromFile(String path) {
    String fileAsString = getFileAsString(path);

    return CharGrid.from(fileAsString);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(CharGrid input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(CharGrid grid) {
      log(grid);
      var startPos = grid.find('S');
      if (startPos == null) {
        throw new AssertionError("no start position found");
      }

      var endPos = grid.find('E');
      if (endPos == null) {
        throw new AssertionError("no end position found");
      }

      int x0 = startPos[0];
      int y0 = startPos[1];
      int x1 = endPos[0];
      int y1 = endPos[1];

      IntGrid scoreGrid = getScoreGrid(grid, x0, y0);

      return getLowestScoreInTile(scoreGrid, x1, y1);
    }
  }

  private static long getLowestScoreInTile(IntGrid scoreGrid, int x, int y) {
    return LongStream.of(RIGHT, DOWN, LEFT, UP)
        .map(s -> scoreGrid.at(x * 4 + (int) s, y))
        .min()
        .orElseThrow();
  }

  private static IntGrid getScoreGrid(CharGrid grid, int x0, int y0) {
    // We represent nodes using a 4*width by height int grid
    // Every cell represents a position and a direction, ex (0, 0, RIGHT)
    // cells for the same position are together:
    // (0,0) is (0, 0, RIGHT),
    // (1,0) is (0, 0, DOWN),
    // (2,0) is (0, 0, LEFT),
    // (3,0) is (0, 0, UP),
    // (4,0) is (1, 0, RIGHT) etc
    int scoreGridWidth = grid.width() * 4;
    IntGrid scoreGrid =
        new IntGrid(new long[scoreGridWidth * grid.height()], scoreGridWidth, grid.height(), -1);
    Arrays.fill(scoreGrid.elements(), Long.MAX_VALUE);

    // For debugging purposes
    IntGrid scoreRecencyGrid;
    IntGrid recencyGrid;
    if (debugging) {
      scoreRecencyGrid =
          new IntGrid(new long[scoreGridWidth * grid.height()], scoreGridWidth, grid.height(), 15);
      Arrays.fill(scoreRecencyGrid.elements(), 16);
      recencyGrid =
          new IntGrid(new long[grid.width() * grid.height()], grid.width(), grid.height(), 15);
      Arrays.fill(recencyGrid.elements(), 16);
    }
    long time = 0;

    Arrays.fill(scoreGrid.elements(), Long.MAX_VALUE);
    int dir = 0;

    scoreGrid.set(x0 * 4 + dir, y0, 0);
    Deque<Coord> stack = new ArrayDeque<>();
    stack.push(new Coord(x0 * 4 + dir, y0));

    while (!stack.isEmpty()) {
      var coord = stack.pop();
      var score = scoreGrid.at(coord.x(), coord.y());

      visitTurn(scoreGrid, coord, 1, score, stack);
      visitTurn(scoreGrid, coord, -1, score, stack);

      int x = coord.x() / 4;
      int y = coord.y();
      dir = coord.x() % 4;

      visitNext(grid, scoreGrid, x, y, dir, score, stack);
      ++time;
      if (debugging) {
        log(time);
        for (int j = 0; j < scoreRecencyGrid.height(); j++) {
          for (int i = 0; i < scoreRecencyGrid.width(); i++) {
            scoreRecencyGrid.compute(i, j, n -> Math.max(n, Math.min(n + 1, 15)));
          }
        }
        for (int j = 0; j < recencyGrid.height(); j++) {
          for (int i = 0; i < recencyGrid.width(); i++) {
            recencyGrid.compute(i, j, n -> Math.max(n, Math.min(n + 1, 15)));
          }
        }
        scoreRecencyGrid.set(coord.x(), coord.y(), 0);
        recencyGrid.set(coord.x() / 4, coord.y(), 0);
        log(scoreRecencyGrid.toStringSingleChar());
        log(recencyGrid.toStringSingleChar());
      }
    }
    return scoreGrid;
  }

  private static void visitNext(
      CharGrid map, IntGrid scoreGrid, int x, int y, int dir, long score, Deque<Coord> stack) {
    score = score + 1;

    var dirVec = DIRS[dir];

    x += dirVec[0];
    y += dirVec[1];

    // coordinate in score grid
    int x_score = x * 4 + dir;

    // if node unreachable or already visited by a shorter path ignore it
    if (scoreGrid.atSafe(x_score, y) <= score) {
      return;
    }

    // if node is a wall make it unreachable
    if (map.atSafe(x, y) == WALL) {
      scoreGrid.set(x_score, y, -1);
      return;
    }

    // set the score for the new path
    scoreGrid.set(x_score, y, score);

    Coord next = new Coord(x_score, y);
    stack.push(next);
  }

  private static void visitTurn(
      IntGrid scoreGrid, Coord coord, int turn, long currentScore, Deque<Coord> stack) {
    int x = coord.x() / 4;
    int currentDir = coord.x() % 4;
    int y = coord.y();

    var counterClockwiseNode = new Coord(x * 4 + ((currentDir + turn + 4) % 4), y);

    if (scoreGrid.at(counterClockwiseNode.x(), counterClockwiseNode.y()) > currentScore + 1000) {
      scoreGrid.set(counterClockwiseNode.x(), counterClockwiseNode.y(), currentScore + 1000);
      stack.push(counterClockwiseNode);
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(CharGrid input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(CharGrid grid) {
      log(grid);
      var startPos = grid.find('S');
      if (startPos == null) {
        throw new AssertionError("no start position found");
      }

      var endPos = grid.find('E');
      if (endPos == null) {
        throw new AssertionError("no end position found");
      }

      int x0 = startPos[0];
      int y0 = startPos[1];
      int x1 = endPos[0];
      int y1 = endPos[1];

      IntGrid scoreGrid = getScoreGrid(grid, x0, y0);

      Deque<Coord> stack = new ArrayDeque<>();
      Set<Coord> goodSeats = new HashSet<>();

      // process all final positions
      long lowestScore = getLowestScoreInTile(scoreGrid, x1, y1);

      for (int i = 0; i < 4; i++) {
        if (lowestScore == scoreGrid.at(x1 * 4 + i, y1)) {
          stack.push(new Coord(x1 * 4 + i, y1));
        }
      }
      goodSeats.add(new Coord(x1, y1));

      while (!stack.isEmpty()) {
        var node = stack.pop();
        int x = node.x() / 4;
        int y = node.y();
        int dir = node.x() % 4;
        var score = scoreGrid.at(node.x(), node.y());

        var d = DIRS[dir];
        int dx = d[0];
        int dy = d[1];

        var cwNode = new Coord(x * 4 + ((dir + 1) % 4), y);
        visitNode(scoreGrid, stack, goodSeats, score, cwNode, 1000);

        var ccwNode = new Coord(x * 4 + ((dir - 1 + 4) % 4), y);
        visitNode(scoreGrid, stack, goodSeats, score, ccwNode, 1000);

        var prevNode = new Coord((x - dx) * 4 + dir, y - dy);
        visitNode(scoreGrid, stack, goodSeats, score, prevNode, 1);
      }

      var out = grid.copy();
      out.replace('.', ' ');
      // so that I can visually make out the gotdam answer
      out.replace('#', '+');
      for (Coord goodSeat : goodSeats) {
        out.set(goodSeat.x(), goodSeat.y(), 'O');
      }
      log(out);

      return goodSeats.size();
    }

    private static void visitNode(
        IntGrid scoreGrid,
        Deque<Coord> stack,
        Set<Coord> goodSeats,
        long currentScore,
        Coord node,
        long cost) {
      long nodeScore = scoreGrid.atSafe(node.x(), node.y());
      if (nodeScore > -1 && nodeScore == currentScore - cost) {
        goodSeats.add(new Coord(node.x() / 4, node.y()));
        stack.push(node);
      }
    }
  }
}
