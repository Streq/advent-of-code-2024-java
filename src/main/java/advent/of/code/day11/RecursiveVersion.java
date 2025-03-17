package advent.of.code.day11;

import static advent.of.code.day11.Rule.applyRules;

public class RecursiveVersion {
  static final Cache CACHE = new Cache();

  static long blink(long stone, long blinks, Cache cache) {
    if (blinks == 0) {
      return 1;
    }
    Long cached = cache.get(blinks, stone);
    if (cached != null) {
      return cached;
    }

    long[] subStones = applyRules(stone);
    long ret = 0;
    for (long subStone : subStones) {
      ret += blink(subStone, blinks - 1, cache);
    }

    cache.put(blinks, stone, ret);
    return ret;
  }

  public static long blink(long stone, long height) {
    return blink(stone, height, CACHE);
  }
}
