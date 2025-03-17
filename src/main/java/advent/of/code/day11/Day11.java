package advent.of.code.day11;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.LongStream;

import static advent.of.code.util.Util.*;

public class Day11 {

  private static final String INPUT_PATH = inputPath(Day11.class);

  private static final Map<Long, long[]> CACHE = new HashMap<>();

  static long[] blink(long value) {
    return CACHE.computeIfAbsent(value, Day11::applyLogic);
  }

  private static final Map<Long, long[]> DEPTH5_CACHE = new HashMap<>();

  static long[] tryCache5(long value) {
    return DEPTH5_CACHE.computeIfAbsent(value, Day11::applyLogic5);
  }

  private static long[] applyLogic5(long value) {
    return LongStream.of(value)
        .flatMap(l -> Arrays.stream(blink(l)))
        .flatMap(l -> Arrays.stream(blink(l)))
        .flatMap(l -> Arrays.stream(blink(l)))
        .flatMap(l -> Arrays.stream(blink(l)))
        .flatMap(l -> Arrays.stream(blink(l)))
        .toArray();
  }

  private static final Map<Long, long[]> DEPTH25_CACHE = new HashMap<>();

  static long[] tryCache25(long value) {
    return DEPTH25_CACHE.computeIfAbsent(value, Day11::applyLogic25);
  }

  private static final Map<Long, Long> DEPTH25_COUNT_CACHE = new HashMap<>();

  static long tryCountCache25(long value) {
    return DEPTH25_COUNT_CACHE.computeIfAbsent(value, Day11::applyLogicCount25);
  }

  private static long applyLogicCount25(long value) {
    return tryCache25(value).length;
  }

  private static long[] applyLogic25(long value) {
    return LongStream.of(value)
        .flatMap(l -> Arrays.stream(tryCache5(l)))
        .flatMap(l -> Arrays.stream(tryCache5(l)))
        .flatMap(l -> Arrays.stream(tryCache5(l)))
        .flatMap(l -> Arrays.stream(tryCache5(l)))
        .flatMap(l -> Arrays.stream(tryCache5(l)))
        .toArray();
  }

  static long[] applyLogic(long value) {
    // If the stone is engraved with the number 0, it is replaced by a stone engraved with the
    // number 1.
    if (value == 0) {
      value = 1;
      return new long[] {value};
    }

    // If the stone is engraved with a number that has an even number of digits, it is replaced
    // by
    // two stones. The left half of the digits are engraved on the new left stone, and the right
    // half of the digits are engraved on the new right stone. (The new numbers don't keep extra
    // leading zeroes: 1000 would become stones 10 and 0.)
    var str = Long.toUnsignedString(value);
    int length = str.length();
    if (length % 2 == 0) {
      int half = length / 2;

      return new long[] {
        Long.parseUnsignedLong(str.substring(0, half)), Long.parseUnsignedLong(str.substring(half))
      };
    }

    // If none of the other rules apply, the stone is replaced by a new stone; the old stone's
    // number multiplied by 2024 is engraved on the new stone.
    return new long[] {value * 2024};
  }

  static long[] inputFromFile(String path) {
    return Arrays.stream(getFileAsString(path).split(" "))
        .mapToLong(Long::parseUnsignedLong)
        .toArray();
  }

  public static class Node {
    public Node(long value) {
      this.value = value;
    }

    long value;
    long depth = 0;
  }

  private static long blink(long[] input, int times) {

    Node node = null;

    Stack<Node> siblings = null;
    long count = 0;

    var nodes = Arrays.stream(input).mapToObj(Node::new).toList();

    siblings = new Stack<>();

    siblings.addAll(nodes);
    while (!siblings.isEmpty()) {
      node = siblings.pop();

      if (nodes.contains(node)) {
        System.out.printf("node %d%n", nodes.indexOf(node));
      }

      while (node.depth < times) {

        if (times - node.depth == 25) {
          count += tryCountCache25(node.value);
          /*
          System.out.println("cache 25 = " + DEPTH25_CACHE.size());
          System.out.println("cache 25c = " + DEPTH25_COUNT_CACHE.size());
          System.out.println("cache 5 = " + DEPTH5_CACHE.size());
          System.out.println("cache 1 = " + CACHE.size());
          System.out.println(count);

           */
          break;
        }

        long[] res = tryCache25(node.value);
        node.depth += 25;

        node.value = res[0];

        for (int i = 1; i < res.length; i++) {
          var n = new Node(res[i]);
          n.depth = node.depth;
          siblings.push(n);
        }
      }
    }

    return count;
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(long[] input) {
      return Arrays.stream(input).map(s -> NonRecursiveVersion.blink(s, 25)).sum();
    }

    public static long solveFromFileCustomBlinks(String example, int blinks) {
      return Arrays.stream(inputFromFile(example))
          .map(s -> NonRecursiveVersion.blink(s, blinks))
          .sum();
    }
  }

  static class Part2NonRecursive {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {

      return solve(inputFromFile(input));
    }

    static long solve(long[] input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(long[] input) {
      return Arrays.stream(input).map(s -> NonRecursiveVersion.blink(s, 75)).sum();
    }
  }

  static class Part2Recursive {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(long[] input) {
      var start = Instant.now();
      log("starting at %s", start);
      var ret = Arrays.stream(input).map(s -> RecursiveVersion.blink(s, 1000)).sum();
      Instant end = Instant.now();
      log("ended at %s", end);
      var diff = Duration.between(start, end);
      log("this shit took me %s", diff);
      return ret;
    }
  }
}
