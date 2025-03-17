package advent.of.code.day12;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

record Grid<T>(T[] elements, int width, int height, T defaultValue) {

  public T at(int x, int y) {
    return elements[x + y * width];
  }

  public void set(int x, int y, T val) {
    elements[x + y * width] = val;
  }

  public void compute(int x, int y, Function<T, T> val) {
    elements[x + y * width] = val.apply(elements[x + y * width]);
  }

  public boolean computeSafe(int x, int y, Function<T, T> val) {
    if (!withinBounds(x, y)) {
      return false;
    }

    val.apply(at(x, y));
    return true;
  }

  // returns -1 if not within bounds
  public T atSafe(int x, int y) {
    return withinBounds(x, y) ? elements[x + y * width] : defaultValue;
  }

  /// @return true if the grid changed, false if out of bounds or not changed
  public boolean setSafe(int x, int y, T val) {
    if (!withinBounds(x, y) || at(x, y) == val) {
      return false;
    }

    set(x, y, val);
    return true;
  }

  public boolean withinBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }

  public Grid<T> copy(Function<T, T> copier, IntFunction<T[]> generator) {
    return new Grid<>(
        Arrays.stream(elements()).map(copier).toArray(generator), width, height, defaultValue);
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
}
