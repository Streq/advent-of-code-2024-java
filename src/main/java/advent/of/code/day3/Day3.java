package advent.of.code.day3;

import java.util.Comparator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static advent.of.code.util.Util.getFileAsString;
import static advent.of.code.util.Util.inputPath;

public class Day3 {

  private static final String INPUT_PATH = inputPath(Day3.class);

  public static final Pattern MUL_REGEX = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
  public static final Pattern DO_REGEX = Pattern.compile("do\\(\\)");
  public static final Pattern DONT_REGEX = Pattern.compile("don't\\(\\)");

  public static class Part1 {
    public static void main(String[] args) {
      System.out.println(sumValidMuls(getFileAsString(INPUT_PATH)));
    }

    /// mul(X,Y), where X and Y are each 1-3 digit numbers. For instance, mul(44,46) multiplies 44
    /// by 46 to get a result of 2024. Similarly, mul(123,4) would multiply 123 by 4.
    /// Invalid characters should be ignored, even if they look like part of a mul
    /// instruction. Sequences like mul(4*, mul(6,9!, ?(12,34), or mul ( 2 , 4 ) do nothing.
    public static long sumValidMuls(String text) {
      return MUL_REGEX
          .matcher(text)
          .results()
          .map(
              r -> {
                long left = Long.parseUnsignedLong(r.group(1));
                long right = Long.parseUnsignedLong(r.group(2));
                return left * right;
              })
          .reduce(Long::sum)
          .orElse(0L);
    }
  }

  public static class Part2 {
    public static void main(String[] args) {
      System.out.println(sumValidMulsWithConditionals(getFileAsString(INPUT_PATH)));
    }

    record Conditional(boolean allow, int position) {}

    public static long sumValidMulsWithConditionals(String text) {

      var dos = toConditionals(DO_REGEX, true, text);
      var donts = toConditionals(DONT_REGEX, false, text);
      var conditionals =
          Stream.concat(dos, donts)
              .sorted(Comparator.comparing(Conditional::position, Integer::compareTo).reversed())
              .toList();

      return MUL_REGEX
          .matcher(text)
          .results()
          .filter(
              result ->
                  conditionals.stream()
                      .filter(c -> c.position < result.start())
                      .findFirst()
                      .map(Conditional::allow)
                      .orElse(true))
          .map(
              r -> {
                long left = Long.parseUnsignedLong(r.group(1));
                long right = Long.parseUnsignedLong(r.group(2));
                return left * right;
              })
          .reduce(Long::sum)
          .orElse(0L);
    }

    private static Stream<Conditional> toConditionals(Pattern doRegex, boolean allow, String text) {
      return doRegex
          .matcher(text)
          .results()
          .map(MatchResult::start)
          .map(position -> new Conditional(allow, position));
    }
  }
}
