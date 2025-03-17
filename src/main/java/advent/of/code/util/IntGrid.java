package advent.of.code.util;

import java.util.Arrays;
import java.util.function.Function;

public record IntGrid(long[] elements, int width, int height, long defaultValue) {

  public IntGrid(int width, int height, long initialValue, long outOfBoundsValue) {
    var elements = new long[width * height];
    Arrays.fill(elements, initialValue);
    this(elements, width, height, outOfBoundsValue);
  }

  public long at(int x, int y) {
    return elements[x + y * width];
  }

  public void set(int x, int y, long val) {
    elements[x + y * width] = val;
  }

  public void add(int x, int y, long val) {
    elements[x + y * width] += val;
  }

  public boolean addSafe(int x, int y, long val) {
    if (!withinBounds(x, y)) {
      return false;
    }

    add(x, y, val);
    return true;
  }

  // returns -1 if not within bounds
  public long atSafe(int x, int y) {
    return withinBounds(x, y) ? elements[x + y * width] : defaultValue;
  }

  /// @return true if the grid changed, false if out of bounds or not changed
  public boolean setSafe(int x, int y, long val) {
    if (!withinBounds(x, y) || at(x, y) == val) {
      return false;
    }

    set(x, y, val);
    return true;
  }

  public boolean withinBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }

  public IntGrid copy() {
    return new IntGrid(elements.clone(), width, height, defaultValue);
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(elements.length + height);
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        stringBuilder.append(at(i, j));
      }
      stringBuilder.append('\n');
    }
    return stringBuilder.toString();
  }

  public String toStringSingleChar() {
    StringBuilder stringBuilder = new StringBuilder(elements.length + height);
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        stringBuilder.append(Util.toChar(at(i, j)));
      }
      stringBuilder.append('\n');
    }
    return stringBuilder.toString();
  }

  public String toString(Function<Long, String> mapper) {
    StringBuilder stringBuilder = new StringBuilder(elements.length + height);
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        stringBuilder.append(mapper.apply(at(i, j)));
      }
      stringBuilder.append('\n');
    }
    return stringBuilder.toString();
  }

  public void compute(int x, int y, Function<Long, Long> mapper) {
    int idx = x + y * width;
    var val = elements[idx];
    elements[idx] = mapper.apply(val);
  }

  public void computeSafe(int x, int y, Function<Long, Long> mapper) {
    if (!withinBounds(x, y)) {
      return;
    }
    compute(x, y, mapper);
  }
}
