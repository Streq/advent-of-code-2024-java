package advent.of.code.day7;

import java.util.*;
import java.util.function.BiFunction;

public enum Operator implements BiFunction<Long, Long, Long> {
  SUM(Long::sum),
  MUL((a, b) -> a * b),
  CON((a, b) -> Long.parseUnsignedLong(Long.toUnsignedString(a) + Long.toUnsignedString(b)));
  private final BiFunction<Long, Long, Long> operator;

  Operator(BiFunction<Long, Long, Long> operator) {
    this.operator = operator;
  }

  record OpTuple(int members, Operator... options) {}

  private static final Map<OpTuple, Operator[][]> COMBINATION_CACHE = new HashMap<>();

  public static Operator[][] combinations(int members, Operator... options) {
    var key = new OpTuple(members, options);
    return COMBINATION_CACHE.computeIfAbsent(key, k -> computeCombinations(members, options));
  }

  private static Operator[][] computeCombinations(int length, Operator... options) {
    int size = options.length;
    for (int i = 1; i < length; i++) {
      size *= options.length;
    }
    Operator[][] ret = new Operator[size][length];

    for (int i = 0; i < size; i++) {
      int divider = 1;
      for (int j = 0; j < length; j++) {
        var index = (i/divider)%options.length;
        ret[i][j] = options[index];
        divider *= options.length;
      }
    }

    return ret;
  }

  @Override
  public Long apply(Long a, Long b) {
    return operator.apply(a, b);
  }
}
