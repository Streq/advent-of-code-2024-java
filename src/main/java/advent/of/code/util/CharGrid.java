package advent.of.code.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static advent.of.code.util.Util.DIRS;

// Utility class for a grid of chars
public record CharGrid(
    char[] elements, int width, int height, char defaultValue, char outOfBoundsValue) {

  public CharGrid(char[] elements, int width, int height) {
    this(elements, width, height, '\0', '\0');
  }

  public void replace(char what, char with) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (at(x, y) == what) {
          set(x, y, with);
        }
      }
    }
  }

  public char at(int x, int y) {
    return elements[x + y * width];
  }

  public void set(int x, int y, char val) {
    elements[x + y * width] = val;
  }

  // returns outOfBoundsValue if not within bounds
  public char atSafe(int x, int y) {
    return withinBounds(x, y) ? elements[x + y * width] : outOfBoundsValue;
  }

  public void writeLine(int x, int y, char[] chars, int length) {
    System.arraycopy(chars, 0, elements, y * width + x, length);
  }

  /// @return true if the grid changed, false if out of bounds or not changed
  public boolean setSafe(int x, int y, char val) {
    if (!withinBounds(x, y) || at(x, y) == val) {
      return false;
    }

    set(x, y, val);
    return true;
  }

  public boolean withinBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }

  public CharGrid copy() {
    return new CharGrid(elements.clone(), width, height, '\0', outOfBoundsValue);
  }

  /// Assumes str to be a valid grid, that is, every line has the same length
  public static CharGrid from(String str, char defaultValue) {
    var lines = str.lines().toList();
    return from(lines, defaultValue);
  }

  /// Assumes lines to be a valid grid, that is, every line has the same length
  private static CharGrid from(List<String> lines, char outOfBoundsValue) {
    int width = lines.getFirst().length();
    int height = lines.size();
    var chars = String.join("", lines).toCharArray();

    return new CharGrid(chars, width, height, '\0', outOfBoundsValue);
  }

  /// Assumes str to be a valid grid, that is, every line has the same length
  public static CharGrid from(String str) {
    return from(str, '\0');
  }

  /// Assumes lines to be a valid grid, that is, every line has the same length
  private static CharGrid from(List<String> lines) {
    return from(lines, '\0');
  }

  public int[] find(char value) {
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        if (at(i, j) == value) {
          return new int[] {i, j};
        }
      }
    }
    return null;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(elements.length + height);
    for (int j = 0; j < height; j++) {
      stringBuilder.append(elements, width * j, width);
      stringBuilder.append('\n');
    }
    return stringBuilder.toString();
  }

  public char[] getLine(int x, int y, int length) {
    return Arrays.copyOfRange(elements, y * width + x, y * width + x + length);
  }

  public CharGrid(int width, int height, char outOfBoundsValue, char defaultValue) {
    var elements = new char[width * height];
    Arrays.fill(elements, defaultValue);
    this(elements, width, height, defaultValue, outOfBoundsValue);
  }

  @FunctionalInterface
  public interface IterationMethod {
    boolean run(int x, int y, char c);
  }

  public void forEach(IterationMethod f) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (f.run(x, y, at(x, y))) {
          return;
        }
      }
    }
  }

  public IntGrid findPaths(int x0, int y0, char[] disallowedTerrain) {
    var res = new IntGrid(width, height, Long.MAX_VALUE, -1);
    String disallowed = String.valueOf(disallowedTerrain);

    forEach(
        (x, y, c) -> {
          if (disallowed.indexOf(c) != -1) {
            res.set(x, y, -1);
          }
          return false;
        });

    Deque<Util.Coord> nodes = new ArrayDeque<>();

    res.set(x0, y0, 0);

    nodes.add(new Util.Coord(x0, y0));

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
          nodes.add(new Util.Coord(xn, yn));
        }
      }
    }

    return res;
  }
}
