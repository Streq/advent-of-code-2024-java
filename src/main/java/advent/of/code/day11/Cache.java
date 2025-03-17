package advent.of.code.day11;

import java.util.HashMap;
import java.util.Map;

import static advent.of.code.util.Util.log;

public class Cache {
  Map<Long, Map<Long, Long>> map = new HashMap<>();

  public Long get(long height, long stone) {
    return getSubMap(height).get(stone);
  }

  public void put(long height, long stone, long value) {
    getSubMap(height).put(stone, value);
  }

  private Map<Long, Long> getSubMap(long height) {
    return map.computeIfAbsent(height, s -> new HashMap<>());
  }
}
