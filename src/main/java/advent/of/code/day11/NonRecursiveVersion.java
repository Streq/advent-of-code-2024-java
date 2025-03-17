package advent.of.code.day11;

import static advent.of.code.day11.Rule.applyRules;

public class NonRecursiveVersion {

  static final Cache CACHE = new Cache();

  static class Frame {
    long stone;
    long[] subStones = new long[2];
    int i = 0;
    int size = 0;
    long count = 0;

    public Frame(long stone) {
      this.stone = stone;
    }

    public boolean step(int blinks, Frame[] stack) {
      if (blinks == 0) {
        count = 1;
        return true;
      }
      if (size == 0) {
        size = applyRules(stone, subStones);
      } else if (i == size) {
        return true;
      }
      var nextFrame = stack[blinks - 1];
      nextFrame.stone = subStones[i++];
      nextFrame.count = 0;
      nextFrame.i = 0;
      nextFrame.size = 0;
      return false;
    }
  }

  public static long blink(long stone, int blinks) {
    final int stackSize = blinks + 1;
    final Frame[] stack = new Frame[stackSize];
    for (int i = 0; i < stackSize; i++) {
      stack[i] = new Frame(0);
    }

    int stackIndex = blinks;
    stack[stackIndex].stone = stone;

    long count = 0;
    while (stackIndex < stackSize) {
      stack[stackIndex].count += count;
      count = 0;

      Frame frame = stack[stackIndex];
      Long cached = CACHE.get(stackIndex, frame.stone);

      // if cached, just skip it
      if (cached != null) {
        ++stackIndex;
        count = cached;
        continue;
      }

      boolean finished = frame.step(stackIndex, stack);
      // if finished, cache and return to parent
      if (finished) {
        CACHE.put(stackIndex, frame.stone, frame.count);
        ++stackIndex;
        count = frame.count;
        continue;
      }

      // not finished, explore first child
      --stackIndex;
    }
    return count;
  }
}
