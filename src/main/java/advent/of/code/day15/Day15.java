package advent.of.code.day15;

import advent.of.code.util.CharGrid;

import java.util.stream.Collectors;

import static advent.of.code.util.Util.*;

public class Day15 {

  private static final char BOT = '@';
  private static final char EMPTY = '.';
  private static final char WALL = '#';
  private static final char BOX = 'O';
  private static final char BOX_LEFT = '[';
  private static final char BOX_RIGHT = ']';

  private static final String INPUT_PATH = inputPath(Day15.class);

  record Data(CharGrid map, String instructions) {}

  static Data inputFromFile(String path) {
    String fileAsString = getFileAsString(path);
    String[] split = fileAsString.split("\n\n");
    var cg = CharGrid.from(split[0], '#');

    return new Data(cg, split[1].lines().collect(Collectors.joining()));
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Data input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Data input) {
      var map = input.map();
      var pos = map.find(BOT);

      if (pos == null) {
        log("BOT not found");
        return 0;
      }

      var x = pos[0];
      var y = pos[1];

      for (char instruction : input.instructions().toCharArray()) {
        followInstruction(map, instruction, x, y);
      }

      long count = 0;
      for (int j = 0; j < map.height(); j++) {
        for (int i = 0; i < map.width(); i++) {
          if (map.at(i, j) == BOX) {
            count += i + j * 100L;
          }
        }
      }

      return count;
    }

    private static void followInstruction(CharGrid map, char instruction, int x, int y) {
      var dir = toDir(instruction);

      if (dir == NODIR) {
        return;
      }

      var what = map.at(x + dir[0], y + dir[1]);
      boolean moveSuccess = false;

      switch (what) {
        case EMPTY -> moveSuccess = true;
        case BOX -> moveSuccess = moveBoxSimple(map, x, y, dir[0], dir[1]);
      }

      if (moveSuccess) {
        map.set(x, y, EMPTY);
        x += dir[0];
        y += dir[1];
        map.set(x, y, BOT);
      }
      log(map);
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Data input) {
      return timeSolution(
          () -> {
            var map = toPart2Map(input);
            return solveInternal(map, input.instructions());
          });
    }

    private static long solveInternal(CharGrid map, String instructions) {
      var pos = map.find(BOT);

      if (pos == null) {
        log("BOT not found");
        return 0;
      }

      var x = pos[0];
      var y = pos[1];

      for (char instruction : instructions.toCharArray()) {
        int[] dir = toDir(instruction);
        int dx = dir[0];
        int dy = dir[1];
        if (followInstruction(map, x, y, dx, dy)) {
          x += dx;
          y += dy;
        }
      }

      long count = 0;
      for (int j = 0; j < map.height(); j++) {
        for (int i = 0; i < map.width(); i++) {
          if (map.at(i, j) == BOX_LEFT) {
            count += i + j * 100L;
          }
        }
      }

      return count;
    }

    private static boolean followInstruction(CharGrid map, int x, int y, int dx, int dy) {

      var what = map.at(x + dx, y + dy);
      boolean moveSuccess = false;

      switch (what) {
        case EMPTY -> moveSuccess = true;
        case BOX_LEFT, BOX_RIGHT -> moveSuccess = moveBoxTree(map, x, y, dx, dy);
      }

      if (moveSuccess) {
        map.set(x, y, EMPTY);
        x += dx;
        y += dy;
        map.set(x, y, BOT);
      }

      log("move from %d,%d to %d,%d", x - dx, y - dy, x, y);
      log(map);
      return moveSuccess;
    }

    private static CharGrid toPart2Map(Data input) {
      var actualMap = input.map();
      var map =
          new CharGrid(
              new char[actualMap.width() * 2 * actualMap.height()],
              actualMap.width() * 2,
              actualMap.height());

      for (int j = 0; j < actualMap.height(); j++) {
        for (int i = 0; i < actualMap.width(); i++) {
          var c = actualMap.at(i, j);
          var val =
              switch (c) {
                case BOT -> "" + BOT + EMPTY;
                case BOX -> "" + BOX_LEFT + BOX_RIGHT;
                default -> "" + c + c;
              };
          map.writeLine(i * 2, j, val.toCharArray(), 2);
        }
      }
      log(actualMap);
      log(map);

      return map;
    }

    public static long solveFromFileNoConvert(String example2) {
      var data = inputFromFile(example2);
      return solveInternal(data.map, data.instructions);
    }
  }

  private static boolean moveBoxSimple(CharGrid map, int x, int y, int dx, int dy) {
    var x1 = x + dx;
    var y1 = y + dy;
    var what = BOX;
    var moveSuccess = false;
    while (what == BOX) {
      x1 += dx;
      y1 += dy;
      what = map.atSafe(x1, y1);
    }

    if (what == EMPTY) {
      moveSuccess = true;
      map.set(x1, y1, BOX);
    }
    return moveSuccess;
  }

  private static boolean moveBoxTree(CharGrid map, int x, int y, int dx, int dy) {

    if (dy == 0) {
      return moveBoxTreeH(map, x, y, dx);
    }

    return moveBoxTreeV(map, x, y, dy);
  }

  private static boolean moveBoxTreeV(CharGrid map, int x, int y, int dy) {
    if (canMoveBoxTreeV(map, x, y, dy)) {
      moveBoxTreeVUnsafe(map, x, y, dy);
      return true;
    }
    return false;
  }

  private static void moveBoxTreeVUnsafe(CharGrid map, int x, int y, int dy) {
    y = y + dy;
    var what = map.atSafe(x, y);
    int right;
    if (what == BOX_LEFT) {
      right = x + 1;
    } else if (what == BOX_RIGHT) {
      right = x;
    } else {
      return;
    }
    int left = right - 1;

    moveBoxTreeVUnsafe(map, left, y, dy);
    moveBoxTreeVUnsafe(map, right, y, dy);
    map.set(left, y, EMPTY);
    map.set(left, y + dy, BOX_LEFT);

    map.set(right, y, EMPTY);
    map.set(right, y + dy, BOX_RIGHT);
  }

  private static boolean canMoveBoxTreeV(CharGrid map, int x, int y, int dy) {
    y = y + dy;
    var what = map.atSafe(x, y);
    if (what == BOX_LEFT) {
      return canMoveBoxTreeV(map, x, y, dy) && canMoveBoxTreeV(map, x + 1, y, dy);
    }
    if (what == BOX_RIGHT) {
      return canMoveBoxTreeV(map, x - 1, y, dy) && canMoveBoxTreeV(map, x, y, dy);
    }
    return what == EMPTY;
  }

  private static boolean moveBoxTreeH(CharGrid map, int firstBox, int y, int dx) {
    firstBox = firstBox + dx;
    var x1 = firstBox;
    var what = map.atSafe(firstBox, y);
    var moveSuccess = false;
    while (what == BOX_LEFT || what == BOX_RIGHT) {
      x1 += dx * 2;
      what = map.atSafe(x1, y);
    }

    if (what == EMPTY) {
      moveSuccess = true;
      int lastBox = x1 - dx;
      var start = Math.min(firstBox, lastBox);
      var end = Math.max(firstBox, lastBox) + 1;
      int length = end - start;
      char[] line = map.getLine(start, y, length);
      map.writeLine(start + dx, y, line, length);
    }
    return moveSuccess;
  }

  private static int[] toDir(char instruction) {
    return switch (instruction) {
      case '>' -> DIRS[0];
      case 'v' -> DIRS[1];
      case '<' -> DIRS[2];
      case '^' -> DIRS[3];
      default -> {
        log("invalid instruction");
        yield NODIR;
      }
    };
  }
}
