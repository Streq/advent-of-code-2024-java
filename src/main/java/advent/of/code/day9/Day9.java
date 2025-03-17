package advent.of.code.day9;

import java.util.*;
import java.util.stream.Collectors;

import static advent.of.code.util.Util.*;

public class Day9 {

  private static final String INPUT_PATH = inputPath(Day9.class);
  public static final int FREE = -1;

  record FileSystem(int[] rawBlocks, int[] freeBlockIndexes, int[] usedBlockIndexes) {}

  static FileSystem inputFromFile(String path) {
    String str = getFileAsLines(path).getFirst();
    var fsLength = 0;
    int[] def = new int[str.length()];
    int freeBlockSize = 0;
    int usedBlockSize = 0;
    for (int i = 0; i < str.length(); i++) {
      int space = Integer.parseUnsignedInt(str.substring(i, i + 1));
      if (i % 2 == 0) {
        usedBlockSize += space;
      } else {
        freeBlockSize += space;
      }
      fsLength += space;
      def[i] = space;
    }
    int[] rawBlocks = new int[fsLength];
    int[] freeBlockIndexes = new int[freeBlockSize];
    int[] usedBlockIndexes = new int[usedBlockSize];
    var index = 0;
    var freeBlockCount = 0;
    var usedBlockCount = 0;
    for (int i = 0; i < def.length; i++) {
      int c;
      int len = def[i];
      if (i % 2 == 0) {
        c = i / 2;
        for (int j = 0; j < len; j++) {
          usedBlockIndexes[usedBlockCount++] = index + j;
        }
      } else {
        c = FREE;
        for (int j = 0; j < len; j++) {
          freeBlockIndexes[freeBlockCount++] = index + j;
        }
      }
      Arrays.fill(rawBlocks, index, index + len, c);

      index += len;
    }

    //System.out.println(stringify(rawBlocks));

    return new FileSystem(rawBlocks, freeBlockIndexes, usedBlockIndexes);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(FileSystem fileSystem) {

      SortedSet<Integer> freeBlockIndexes = new TreeSet<>(Integer::compareTo);
      for (int pos : fileSystem.freeBlockIndexes) {
        freeBlockIndexes.add(pos);
      }

      int[] usedBlockIndexes = fileSystem.usedBlockIndexes();
      int[] rawBlocks = fileSystem.rawBlocks();
      int freeBlockIndex = 0;
      for (int i = 0; i < usedBlockIndexes.length; i++) {
        freeBlockIndex = freeBlockIndexes.removeFirst();
        int usedBlockIndex = usedBlockIndexes[usedBlockIndexes.length - 1 - i];
        if (freeBlockIndex > usedBlockIndex) {
          break;
        }
        int id = rawBlocks[usedBlockIndex];

        rawBlocks[usedBlockIndex] = FREE;
        rawBlocks[freeBlockIndex] = id;
        // System.out.println(stringify(rawBlocks));
        freeBlockIndexes.add(usedBlockIndex);
      }

      System.out.println(stringify(rawBlocks));

      long count = 0;
      for (int i = 0; i < freeBlockIndex; i++) {
        count += i * Long.parseLong(String.valueOf(rawBlocks[i]));
      }

      return count;
    }
  }

  private static String stringify(int[] rawBlocks) {
    return Arrays.stream(rawBlocks)
        .mapToObj(
            i -> {
              if (i == -1) {
                return ".";
              }
              if (i > 9) {
                return "(" + i + ")";
              }
              return String.valueOf(i);
            })
        .collect(Collectors.joining());
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    // probably could've been done easier with a Chunk class, but I started from copypasting part1
    // and just made the changes needed
    static long solve(FileSystem fileSystem) {

      SortedSet<Integer> freeBlockIndexes = new TreeSet<>(Integer::compareTo);
      for (int pos : fileSystem.freeBlockIndexes) {
        freeBlockIndexes.add(pos);
      }

      int[] usedBlockIndexes = fileSystem.usedBlockIndexes();
      int[] rawBlocks = fileSystem.rawBlocks();

      for (int i = 0; i < usedBlockIndexes.length; ) {
        int usedBlockIndex = usedBlockIndexes[usedBlockIndexes.length - 1 - i];
        int id = rawBlocks[usedBlockIndex];

        // let's find the range of blocks this chunk occupies
        int end = usedBlockIndex + 1;
        var start = usedBlockIndex;
        while (start >= 1 && rawBlocks[start - 1] == id) {
          --start;
        }
        int size = end - start;
        i += size;

        int freePos =
            freeBlockIndexes.stream()
                .filter(idx -> idx < end - size)
                .filter(
                    idx -> {
                      for (int off = 0; off < size; off++) {
                        int current = idx + off;
                        if (current == rawBlocks.length) {
                          return false;
                        }
                        if (rawBlocks[current] != -1) {
                          return false;
                        }
                      }
                      return true;
                    })
                .findFirst()
                .orElse(-1); // don't move
        if (freePos == -1) {
          continue;
        }
        for (int j = 0; j < size; j++) {
          int emptiedBlock = start + j;
          rawBlocks[emptiedBlock] = FREE;
          freeBlockIndexes.add(emptiedBlock);
          int filledBlock = freePos + j;
          rawBlocks[filledBlock] = id;
          freeBlockIndexes.remove(filledBlock);
        }
        // System.out.println(stringify(rawBlocks));
      }

      //System.out.println(stringify(rawBlocks));

      long count = 0;
      for (int i = 0; i < rawBlocks.length; i++) {
        long id = Long.parseLong(String.valueOf(rawBlocks[i]));
        if (id == FREE) {
          continue;
        }
        count += i * id;
      }

      return count;
    }
  }
}
