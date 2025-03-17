package advent.of.code.day10;

import java.util.*;

import static advent.of.code.util.Util.getFileAsString;
import static advent.of.code.util.Util.inputPath;

public class Day10 {

  static final int[][] DIRS = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
  private static final String INPUT_PATH = inputPath(Day10.class);

  private static long countHikingPaths(IntGrid grid, boolean skipRedundantRoutes) {
    System.out.println(grid.toFirstDigitString());
    Queue<Node> trailheads = new ArrayDeque<>();
    Map<Tile, Set<Tile>> reachableTrailheadsMap = new HashMap<>();
    for (int j = 0; j < grid.height(); j++) {
      for (int i = 0; i < grid.width(); i++) {
        if (grid.at(i, j) == 0) {
          trailheads.add(Node.of(null, 0, i, j));
        }
      }
    }
    long count = 0;
    var byOrder = List.copyOf(trailheads);
    Map<Tile, Long> tileCount = new HashMap<>();
    trailheads.stream().map(Node::tile).forEach(t -> tileCount.put(t, 0L));
    while (!trailheads.isEmpty()) {
      Node head = trailheads.remove();
      var trailheadsReachableFromHere =
          reachableTrailheadsMap.computeIfAbsent(head.tile, s -> new HashSet<>());

      Tile root = head.root();
      if (skipRedundantRoutes && trailheadsReachableFromHere.contains(root)) {
        continue;
      }
      trailheadsReachableFromHere.add(root);
      if (head.tile.number() == 9) {
        Part1.printRoad(grid, head);
        tileCount.computeIfPresent(root, (k, v) -> v + 1);
        count += 1;
      }
      int x = head.tile.x;
      int y = head.tile.y;

      for (int[] dir : DIRS) {
        int x1 = x + dir[0];
        int y1 = y + dir[1];
        var number = grid.atSafe(x1, y1);

        if (number == head.tile.number() + 1) {
          Node node = Node.of(head, number, x1, y1);
          trailheads.add(node);
        }
      }
    }

    System.out.println(byOrder.stream().map(Node::tile).map(tileCount::get).toList());

    return count;
  }

  record Coord(int x, int y) {}

  record IntGrid(int[] elements, int width, int height) {

    public static final IntGrid EMPTY = new IntGrid(new int[0], 0, 0);
    public static final int NOT_FOUND = -1;

    public int at(int x, int y) {
      return elements[x + y * width];
    }

    public void set(int x, int y, int val) {
      elements[x + y * width] = val;
    }

    // returns -1 if not within bounds
    public int atSafe(int x, int y) {
      return withinBounds(x, y) ? elements[x + y * width] : NOT_FOUND;
    }

    /// @return true if the grid changed, false if out of bounds or not changed
    public boolean setSafe(int x, int y, int val) {
      if (!withinBounds(x, y) || at(x, y) == val) {
        return false;
      }

      set(x, y, val);
      return true;
    }

    public boolean withinBounds(int x, int y) {
      return x >= 0 && y >= 0 && x < width && y < height;
    }

    public IntGrid copy() {
      return new IntGrid(elements.clone(), width, height);
    }

    /// Assumes str to be a valid grid, that is, every line has the same length
    public static IntGrid from(String str) {
      var lines = str.lines().toList();
      return from(lines);
    }

    /// Assumes lines to be a valid grid, that is, every line has the same length
    private static IntGrid from(List<String> lines) {
      if (lines.isEmpty()) {
        return EMPTY;
      }

      int width = lines.getFirst().length();
      int height = lines.size();
      System.out.println("width is " + width);
      System.out.println("height is " + height);
      var chars =
          String.join("", lines)
              .chars()
              .mapToObj(c -> (char) c)
              .map(String::valueOf)
              .mapToInt(Integer::parseUnsignedInt)
              .toArray();

      return new IntGrid(chars, width, height);
    }

    public String toFirstDigitString() {
      StringBuilder stringBuilder = new StringBuilder(elements.length + height);
      for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
          stringBuilder.append(String.valueOf(at(i, j)).charAt(0));
        }
        stringBuilder.append('\n');
      }
      return stringBuilder.toString();
    }
  }

  static IntGrid inputFromFile(String path) {
    return IntGrid.from(getFileAsString(path));
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(IntGrid grid) {
      return countHikingPaths(grid, true);
    }

    private static void printRoad(IntGrid grid, Node head) {
      var c = grid.copy();
      Arrays.fill(c.elements, -1);
      while (head != null) {
        var tile = head.tile();
        c.set(tile.x(), tile.y(), tile.number());
        head = head.starter;
      }
      System.out.println(c.toFirstDigitString());
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(IntGrid grid) {
      return countHikingPaths(grid, false);
    }
  }
  record Node(Node starter, Tile tile) {
    public static Node of(Node starter, int number, int x, int y) {
      return new Node(starter, new Tile(number, x, y));
    }

    public Tile root() {
      var n = this;
      while (n.starter != null) {
        n = n.starter;
      }
      return n.tile;
    }
  }

  record Tile(int number, int x, int y) {}
}
