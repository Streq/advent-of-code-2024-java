package advent.of.code.util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Util {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  public static final String ANSI_BRIGHT_BLACK = "\u001B[90m";
  public static final String ANSI_BRIGHT_RED = "\u001B[91m";
  public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
  public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
  public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
  public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
  public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
  public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

  public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
  public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
  public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
  public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
  public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
  public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
  public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

  public static final String ANSI_BRIGHT_BLACK_BACKGROUND = "\u001B[100m";
  public static final String ANSI_BRIGHT_RED_BACKGROUND = "\u001B[101m";
  public static final String ANSI_BRIGHT_GREEN_BACKGROUND = "\u001B[102m";
  public static final String ANSI_BRIGHT_YELLOW_BACKGROUND = "\u001B[103m";
  public static final String ANSI_BRIGHT_BLUE_BACKGROUND = "\u001B[104m";
  public static final String ANSI_BRIGHT_PURPLE_BACKGROUND = "\u001B[105m";
  public static final String ANSI_BRIGHT_CYAN_BACKGROUND = "\u001B[106m";
  public static final String ANSI_BRIGHT_WHITE_BACKGROUND = "\u001B[107m";

  public static final List<String> BACKGROUND_COLORS =
      List.of(
          ANSI_BLACK_BACKGROUND,
          ANSI_RED_BACKGROUND,
          ANSI_GREEN_BACKGROUND,
          ANSI_YELLOW_BACKGROUND,
          ANSI_BLUE_BACKGROUND,
          ANSI_PURPLE_BACKGROUND,
          ANSI_CYAN_BACKGROUND,
          ANSI_WHITE_BACKGROUND,
          ANSI_BRIGHT_BLACK_BACKGROUND,
          ANSI_BRIGHT_RED_BACKGROUND,
          ANSI_BRIGHT_GREEN_BACKGROUND,
          ANSI_BRIGHT_YELLOW_BACKGROUND,
          ANSI_BRIGHT_BLUE_BACKGROUND,
          ANSI_BRIGHT_PURPLE_BACKGROUND,
          ANSI_BRIGHT_CYAN_BACKGROUND,
          ANSI_BRIGHT_WHITE_BACKGROUND);
  public static final List<String> COLORS =
      List.of(
          ANSI_BLACK,
          ANSI_RED,
          ANSI_GREEN,
          ANSI_YELLOW,
          ANSI_BLUE,
          ANSI_PURPLE,
          ANSI_CYAN,
          ANSI_WHITE,
          ANSI_BRIGHT_BLACK,
          ANSI_BRIGHT_RED,
          ANSI_BRIGHT_GREEN,
          ANSI_BRIGHT_YELLOW,
          ANSI_BRIGHT_BLUE,
          ANSI_BRIGHT_PURPLE,
          ANSI_BRIGHT_CYAN,
          ANSI_BRIGHT_WHITE);
  public static final List<String> RAINBOW_COLORS =
      List.of(
          ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN
          //          ANSI_BRIGHT_RED,
          //          ANSI_BRIGHT_GREEN,
          //          ANSI_BRIGHT_YELLOW,
          //          ANSI_BRIGHT_BLUE,
          //          ANSI_BRIGHT_PURPLE,
          //          ANSI_BRIGHT_CYAN
          );

  public static final String INT_TO_CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static char toChar(long d) {
    return INT_TO_CHAR.charAt(Math.floorMod(d, INT_TO_CHAR.length()));
  }

  public static void log(String s, Object... others) {
    logInternal(s, others);
  }

  public static void log(Object others) {
    logInternal("%s", String.valueOf(others));
  }

  private static void logInternal(String s, Object... others) {
    System.out.printf(s, others);
    System.out.println();
  }

  public static String inputPath(Class<?> c) {
    return getPath(c, "input.txt");
  }

  private static String getPath(Class<?> c, String file) {
    return Optional.ofNullable(c.getResource(file)).map(URL::getPath).orElse("");
  }

  public static String exampleInputPath(Class<?> c) {
    return getPath(c, "example.txt");
  }

  public static String exampleInputPath(Class<?> c, int num) {
    return getPath(c, "example" + num + ".txt");
  }

  public static List<String> getFileAsLines(String path) {
    try {
      return Files.readAllLines(Paths.get(path));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getFileAsString(String path) {
    try {
      return Files.readString(Paths.get(path));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T timeSolution(Supplier<T> solver) {
    var start = Instant.now();
    log("starting at %s", start);
    T ret = solver.get();
    Instant end = Instant.now();
    log("ended at %s", end);
    var diff = Duration.between(start, end);
    log("this shit took me %s", diff);
    return ret;
  }

  public static final int[] NODIR = new int[] {0, 0};

  public static final int[][] DIRS = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
  public static final int[][] DIRS8 =
      new int[][] {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
  public static final int[][] RIGHTDOWN = new int[][] {{1, 0}, {0, 1}};

  public static class Counter<T> {
    Map<T, Long> map = new HashMap<>();

    public long getAndIncrement(T key) {
      return incrementAndGet(key) - 1;
    }

    public long incrementAndGet(T key) {
      return map.compute(key, (k, v) -> v == null ? 1 : v + 1);
    }

    public Set<T> keySet() {
      return map.keySet();
    }

    public long get(T id) {
      return map.getOrDefault(id, 0L);
    }
  }

  public record Coord(int x, int y) {
    int toDir() {
      return Util.toDir(x, y);
    }
  }

  public static int toDir(int x, int y) {
    return x > 0 ? 0 : y > 0 ? 1 : x < 0 ? 2 : y < 0 ? 3 : -1;
  }

  public record CoordL(long x, long y) {

    public String toString() {
      return ("(%d,%d)".formatted(x, y));
    }
  }

  /// returns a list of coordinates from end position to start position
  public static List<Coord> findPath(IntGrid pathGrid, int x, int y) {
    List<Coord> ret = new ArrayList<>();
    ret.add(new Coord(x, y));

    var weight = pathGrid.atSafe(x, y);

    if (weight == -1) {
      return ret;
    }

    while (weight != 0) {
      for (int[] dir : DIRS) {
        int x1 = x + dir[0];
        int y1 = y + dir[1];
        var weight1 = pathGrid.atSafe(x1, y1);
        if (weight1 == -1) {
          continue;
        }

        if (weight1 >= weight) {
          continue;
        }
        weight = weight1;
        x = x1;
        y = y1;
        ret.add(new Coord(x, y));
        break;
      }
    }

    return ret;
  }

  public static List<List<Coord>> findAllShortestPaths(IntGrid pathGrid, int x, int y) {
    List<List<Coord>> ret = new ArrayList<>();

    record Node(int x, int y, Node nextNode) {}

    Deque<Node> stack = new ArrayDeque<>();
    stack.push(new Node(x, y, null));

    while (!stack.isEmpty()) {
      Node node = stack.pop();

      x = node.x;
      y = node.y;
      var stepsFromBeginning = pathGrid.atSafe(x, y);
      if (stepsFromBeginning == -1) {
        continue;
      }

      if (stepsFromBeginning == 0) {
        var l = new ArrayList<Coord>();
        while (node != null) {
          l.add(new Coord(node.x, node.y));
          node = node.nextNode;
        }
        ret.add(l);
        continue;
      }

      for (int[] dir : DIRS) {
        var x1 = x + dir[0];
        var y1 = y + dir[1];

        var stepsFromBeginning1 = pathGrid.atSafe(x1, y1);
        if (stepsFromBeginning1 == -1 || stepsFromBeginning1 != stepsFromBeginning - 1) {
          continue;
        }
        stack.push(new Node(x1, y1, node));
      }
    }
    return ret;
  }

  public static <T> Stream<T> filterMatchingMin(Stream<T> stream, Comparator<T> comparator) {
    var l = stream.toList();
    var minOpt = l.stream().min(comparator);

    if (minOpt.isEmpty()) {
      return Stream.of();
    }

    var min = minOpt.get();

    return l.stream().filter(v -> comparator.compare(min, v) == 0);
  }

  public static <K, V> void swap(Map<K, V> map, K a, K b) {
    var av = map.get(a);
    var bv = map.get(b);
    map.put(b, av);
    map.put(a, bv);
  }

  public static <T> List<T> concat(List<T> a, List<T> b) {
    List<T> ret = new ArrayList<>(a);
    ret.addAll(b);
    return ret;
  }

  @SafeVarargs
  public static <T> List<T> concat(List<T> a, T... b) {
    List<T> ret = new ArrayList<>(a);
    ret.addAll(Arrays.asList(b));
    return ret;
  }
}
