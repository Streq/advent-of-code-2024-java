package advent.of.code.day24;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static advent.of.code.util.Util.exampleInputPath;
import static advent.of.code.util.Util.swap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class Day24Test {

  static final String EXAMPLE = exampleInputPath(Day24Test.class);
  static final String EXAMPLE1 = exampleInputPath(Day24Test.class, 1);
  static final String EXAMPLE2 = exampleInputPath(Day24Test.class, 2);

  private static Arguments args(String... x) {
    return arguments((Object) x);
  }

  @Test
  void testPart1() {
    assertEquals(4, Day24.Part1.solveFromFile(EXAMPLE));
  }

  @Test
  void testPart1_larger() {
    assertEquals(2024, Day24.Part1.solveFromFile(EXAMPLE1));
  }

  @Test
  void testPart1_verifyItWorks() {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++) {
        assertEquals(i + j, Day24.Part1.solve(initializeTo(i, j)));
      }
    }
  }

  static Stream<Arguments> assertSolvesSwapSource() {
    String[] keys =
        Day24.inputFromFile(EXAMPLE2).entrySet().stream()
            .filter(e -> e.getValue() instanceof Day24.Gate)
            .map(Map.Entry::getKey)
            .toArray(String[]::new);

    ArrayList<Arguments> arguments = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      for (int j = i + 1; j < keys.length; j++) {
        arguments.add(args(keys[i], keys[j]));
      }
    }

    return Stream.of(Stream.of(args()), arguments.stream()).flatMap(s -> s);
  }

  @ParameterizedTest
  @MethodSource("assertSolvesSwapSource")
  void assertSolvesSwap(String... toSwap) {
    if ((toSwap.length != Arrays.stream(toSwap).distinct().count())) {
      throw new AssertionError("the same key cannot be involved in multiple swaps");
    }
    if ((toSwap.length % 2 != 0)) {
      throw new AssertionError("only an even number of keys can be swapped");
    }
    Map<String, Day24.Value> input = Day24.inputFromFile(EXAMPLE2);
    for (int i = 0; i < toSwap.length; i += 2) {
      swap(input, toSwap[i], toSwap[i + 1]);
    }
    toSwap = removeInterchangeableSwaps(toSwap, input);
    var expected = Arrays.stream(toSwap).sorted().collect(Collectors.joining(","));
    assertEquals(expected, Day24.Part2.solve(input));
  }

  private String[] removeInterchangeableSwaps(String[] toSwap, Map<String, Day24.Value> input) {
    ArrayList<String> ret = new ArrayList<>();
    for (int i = 0; i < toSwap.length; i += 2) {
      var a = toSwap[i];
      var b = toSwap[i + 1];

      var gatesUsingA = getGatesUsingB(input, a);
      var gatesUsingB = getGatesUsingB(input, b);
      if (gatesUsingA.isEmpty() || !gatesUsingA.equals(gatesUsingB)) {
        ret.add(a);
        ret.add(b);
      }
    }
    return ret.toArray(String[]::new);
  }

  private static Set<String> getGatesUsingB(Map<String, Day24.Value> input, String b) {
    return input.entrySet().stream()
        .filter(v -> v.getValue() instanceof Day24.Gate)
        .map(v -> Map.entry(v.getKey(), (Day24.Gate) v.getValue()))
        .filter(v -> v.getValue().a().equals(b) || v.getValue().b().equals(b))
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  private Map<String, Day24.Value> initializeTo(int a, int b) {
    var ret = Day24.inputFromFile(EXAMPLE2);
    var xs = ret.keySet().stream().filter(e -> e.startsWith("x")).sorted().toList();
    var ys = ret.keySet().stream().filter(e -> e.startsWith("y")).sorted().toList();

    for (int i = 0; i < xs.size(); i++) {
      ret.put(xs.get(i), new Day24.LiteralValue((a & (1 << i)) != 0));
    }
    for (int i = 0; i < ys.size(); i++) {
      ret.put(ys.get(i), new Day24.LiteralValue((b & (1 << i)) != 0));
    }

    return ret;
  }
}
