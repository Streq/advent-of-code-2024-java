package advent.of.code.day4;

import advent.of.code.util.Util;

import java.util.List;
import java.util.Optional;

import static advent.of.code.util.Util.inputPath;

public class Day4 {

  private static final String INPUT_PATH = inputPath(Day4.class);

  record WordSearchPuzzle(List<String> lines) {

    Optional<Character> at(int x, int y) {
      if (x < 0 || y < 0 || x >= width() || y >= height()) {
        return Optional.empty();
      }

      return Optional.of(lines.get(y).charAt(x));
    }

    int width() {
      return lines.getFirst().length();
    }

    int height() {
      return lines.size();
    }
  }

  static WordSearchPuzzle inputFromFile(String path) {
    return new WordSearchPuzzle(Util.getFileAsLines(path));
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    /**
     * This word search allows words to be horizontal, vertical, diagonal, written backwards, or
     * even overlapping other words. It's a little unusual, though, as you don't merely need to find
     * one instance of XMAS - you need to find all of them. Here are a few ways XMAS might appear,
     * where irrelevant characters have been replaced with
     */
    static long solve(WordSearchPuzzle input) {
      long count = 0;

      for (int x = 0; x < input.width(); x++) {
        for (int y = 0; y < input.height(); y++) {
          count += findXMAS(input, x, y);
        }
      }

      return count;
    }

    static final String WORD = "XMAS";

    private static long findXMAS(WordSearchPuzzle puzzle, int x, int y) {
      long finds = 0;

      // find in every direction
      for (int i = -1; i < 2; i++) {
        for (int j = -1; j < 2; j++) {
          if (i == 0 && j == 0) {
            continue;
          }
          int wordIndex = 0;
          for (; wordIndex < WORD.length(); wordIndex++) {
            var optC = puzzle.at(x + i * wordIndex, y + j * wordIndex);

            if (optC.isEmpty()) {
              break;
            }

            char c = optC.get();

            if (c != WORD.charAt(wordIndex)) {
              break;
            }
          }
          if (wordIndex == WORD.length()) {
            finds += 1;
          }
        }
      }

      return finds;
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(WordSearchPuzzle input) {
      long count = 0;

      for (int x = 0; x < input.width(); x++) {
        for (int y = 0; y < input.height(); y++) {
          if (findX_MAS(input, x, y)) {
            count += 1;
          }
        }
      }

      return count;
    }

    /// find two MAS in the shape of an X
    private static boolean findX_MAS(WordSearchPuzzle input, int x, int y) {
      char c1 = input.at(x, y).orElseThrow();
      return c1 == 'A'
          // find the '/'
          && findDiagonal(input, x, y, false)
          // find the '\'
          && findDiagonal(input, x, y, true);
    }

    private static boolean findDiagonal(WordSearchPuzzle input, int x, int y, boolean flipY) {
      int vflip = flipY ? -1 : 1;
      var optC0 = input.at(x - 1, y - vflip);
      if (optC0.isEmpty()) {
        return false;
      }
      var c0 = optC0.get();
      char expectedC2;
      switch (c0) {
        case 'M':
          expectedC2 = 'S';
          break;
        case 'S':
          expectedC2 = 'M';
          break;
        default:
          return false;
      }

      var optC2 = input.at(x + 1, y + vflip);
      if (optC2.isEmpty()) {
        return false;
      }
      var c2 = optC2.get();
      return c2 == expectedC2;
    }
  }
}
