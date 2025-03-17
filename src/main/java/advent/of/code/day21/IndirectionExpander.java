package advent.of.code.day21;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndirectionExpander {

  record Params(char a, char b, int indirections) {}

  private static final Map<Params, Long> cache = new HashMap<>();

  static long getLength(String str, int indirections) {
    long length = 0;

    char previous = 'A';
    for (int i = 0; i < str.length(); i++) {
      char current = str.charAt(i);
      length += getLength(previous, current, indirections);
      previous = current;
    }

    return length;
  }

  static long getLength(char a, char b, int indirections) {
    if (indirections == 0) {
      return 1;
    }

    Params key = new Params(a, b, indirections);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    List<String> expansions = PathMatrix.DIR_MATRIX.pathsTo(a, b);

    long length =
        expansions.stream().mapToLong(s -> getLength(s, indirections - 1)).min().orElseThrow();

    cache.put(key, length);

    return length;
  }
}
