package advent.of.code.day22;

import advent.of.code.util.Util;

import java.util.*;

import static advent.of.code.util.Util.*;

public class Day22 {

  private static final String INPUT_PATH = inputPath(Day22.class);

  static List<Long> inputFromFile(String path) {
    return getFileAsLines(path).stream().map(Long::parseLong).toList();
  }

  public static final int MODULO_MASK = (1 << 24) - 1;

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(List<Long> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(List<Long> input) {

      return input.stream()
          .mapToLong(l -> l)
          .map(
              l -> {
                for (int i = 0; i < 2000; i++) {
                  if (l == 0) {
                    return l;
                  }
                  l = getNextNumber(l);
                }
                return l;
              })
          .sum();
    }
  }

  private static long getNextNumber(long secret) {
    secret ^= secret << 6;
    secret &= MODULO_MASK;
    secret ^= secret >> 5;
    secret &= MODULO_MASK;
    secret ^= secret << 11;
    secret &= MODULO_MASK;

    return secret;
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(List<Long> input) {
      return timeSolution(() -> solveInternal(input));
    }

    record Sequence(int a, int b, int c, int d) {

      Sequence shift(int e) {
        return new Sequence(b, c, d, e);
      }

      public String toString() {
        return a + "," + b + "," + c + "," + d;
      }
    }

    static long solveInternal(List<Long> input) {
      Map<Sequence, Long> bananaMap = new HashMap<>();
      for (long secret : input) {
        // long startingSecret = secret;
        Map<Sequence, Integer> firstBuyBySequence = new HashMap<>();

        Sequence sequence = new Sequence(0, 0, 0, 0);
        int price = getPrice(secret);
        int previousPrice;
        for (int i = 0; i < 4; i++) {
          previousPrice = price;
          secret = getNextNumber(secret);
          price = getPrice(secret);

          sequence = sequence.shift(price - previousPrice);
        }

        firstBuyBySequence.putIfAbsent(sequence, price);

        for (int i = 0; i < 2000 - 4; i++) {
          previousPrice = price;
          secret = getNextNumber(secret);
          price = getPrice(secret);

          sequence = sequence.shift(price - previousPrice);

          if (!firstBuyBySequence.containsKey(sequence)) {
            firstBuyBySequence.put(sequence, price);

            //            log(
            //                "first price for secret "
            //                    + startingSecret
            //                    + " when sequence is "
            //                    + sequence
            //                    + " is "
            //                    + price);
          }
        }

        firstBuyBySequence.forEach((seq, bananas) -> incrementBananaMap(bananas, bananaMap, seq));
      }

      var list =
          bananaMap.entrySet().stream()
              .sorted(Comparator.comparingLong((Map.Entry<?, Long> e) -> e.getValue()).reversed())
              .filter((Map.Entry<?, Long> e) -> e.getValue() > 0)
              .peek(Util::log)
              .toList();

      return list.stream().findFirst().orElseThrow().getValue();
    }

    private static void incrementBananaMap(
        int price, Map<Sequence, Long> bananaMap, Sequence sequence) {
      bananaMap.compute(sequence, (_, l) -> l == null ? price : l + price);
    }

    private static int getPrice(long secret) {
      long l = secret % 10;
      return (int) l;
    }
  }
}
