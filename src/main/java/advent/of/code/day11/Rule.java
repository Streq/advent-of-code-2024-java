package advent.of.code.day11;

import java.util.HashMap;
import java.util.Map;

public class Rule {

  static class Cache {
    Map<Long, long[]> map = new HashMap<>();

    long[] get(long key) {
      return map.get(key);
    }

    void put(long key, long[] values) {
      map.put(key, values);
    }
  }

  private static final Cache CACHE = new Cache();

  public static long[] applyRules(long value) {
    long[] cached = CACHE.get(value);
    if (cached != null) {
      return cached;
    }

    var ret = applyRulesInternal(value);
    CACHE.put(value, ret);
    return ret;
  }

  public static int applyRules(long value, long[] subStones) {
    // If the stone is engraved with the number 0, it is replaced by a stone engraved with the
    // number 1.
    if (value == 0) {
      subStones[0] = 1;
      return 1;
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
      subStones[0] = Long.parseUnsignedLong(str.substring(0, half));
      subStones[1] = Long.parseUnsignedLong(str.substring(half));
      return 2;
    }

    // If none of the other rules apply, the stone is replaced by a new stone; the old stone's
    // number multiplied by 2024 is engraved on the new stone.
    subStones[0] = value * 2024;
    return 1;
  }

  private static long[] applyRulesInternal(long value) {
    // If the stone is engraved with the number 0, it is replaced by a stone engraved with the
    // number 1.
    if (value == 0) {
      return new long[] {1};
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
}
