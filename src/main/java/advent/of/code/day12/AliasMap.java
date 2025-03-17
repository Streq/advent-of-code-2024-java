package advent.of.code.day12;

import java.util.*;

class AliasMap<T> {
  Map<T, SortedSet<T>> map = new HashMap<>();

  public void alias(T a, T b) {
    var setA = map.get(a);
    var setB = map.get(b);

    if (setA == null) {
      if (setB == null) {
        setA = new TreeSet<>();

        setA.add(a);
        setA.add(b);
        map.put(a, setA);
        map.put(b, setA);
        return;
      }
      setB.add(a);
      map.put(a, setB);
      return;
    }

    if (setA == setB) {
      return;
    }

    if (setB == null) {
      setA.add(b);
      map.put(b, setA);
      return;
    }

    setA.addAll(setB);
    for (T c : setB) {
      map.put(c, setA);
    }
  }

  public T toOgId(T alias) {
    var set = map.get(alias);
    if (set == null) {
      return alias;
    }
    return set.getFirst();
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    var keys = map.keySet();

    if (!keys.isEmpty()) {
      var it = keys.iterator();
      boolean comma = appendNext(it, builder);
      while (it.hasNext()) {
        if (comma) {
          builder.append(", ");
        }
        comma = appendNext(it, builder);
      }
    }

    builder.append("]");
    return builder.toString();
  }

  private boolean appendNext(Iterator<T> it, StringBuilder builder) {
    T id = it.next();
    T og = toOgId(id);
    if (id.equals(og)) {
      return false;
    }
    builder.append("%s = %s".formatted(og, id));
    return true;
  }
}
