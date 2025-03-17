package advent.of.code.day8;

import advent.of.code.util.Util;

import java.util.*;

import static advent.of.code.util.Util.getFileAsLines;
import static advent.of.code.util.Util.inputPath;

/// --- Day 8: Resonant Collinearity ---
///
/// You find yourselves on the roof of a top-secret Easter Bunny installation.
///
/// While The Historians do their thing, you take a look at the familiar huge antenna. Much to
/// your surprise, it seems to have been reconfigured to emit a signal that makes people 0.1% more
/// likely to buy Easter Bunny brand Imitation Mediocre Chocolate as a Christmas gift!
/// Unthinkable!
///
/// Scanning across the city, you find that there are actually many such antennas. Each antenna is
/// tuned to a specific frequency indicated by a single lowercase letter, uppercase letter, or
/// digit. You create a map (your puzzle input) of these antennas. For example:
///
/// ```
/// ............
/// ........0...
/// .....0......
/// .......0....
/// ....0.......
/// ......A.....
/// ............
/// ............
/// ........A...
/// .........A..
/// ............
/// ............
/// ```
/// The signal only applies its nefarious effect at specific antinodes based on the resonant
/// frequencies of the antennas. In particular, an antinode occurs at any point that is perfectly
/// in line with two antennas of the same frequency - but only when one of the antennas is twice
/// as far away as the other. This means that for any pair of antennas with the same frequency,
/// there are two antinodes, one on either side of them.
///
/// So, for these two antennas with frequency a, they create the two antinodes marked with #:
///
/// ```
/// ..........
/// ...#......
/// ..........
/// ....a.....
/// ..........
/// .....a....
/// ..........
/// ......#...
/// ..........
/// ..........
/// ```
/// Adding a third antenna with the same frequency creates several more antinodes. It would
/// ideally add four antinodes, but two are off the right side of the map, so instead it adds only
/// two:
///
/// ```
/// ..........
/// ...#......
/// #.........
/// ....a.....
/// ........a.
/// .....a....
/// ..#.......
/// ......#...
/// ..........
/// ..........
/// ```
/// Antennas with different frequencies don't create antinodes; A and a count as different
/// frequencies. However, antinodes can occur at locations that contain antennas. In this diagram,
/// the lone antenna with frequency capital A creates no antinodes but has a lowercase-a-frequency
/// antinode at its location:
///
/// ```
/// ..........
/// ...#......
/// #.........
/// ....a.....
/// ........a.
/// .....a....
/// ..#.......
/// ......A...
/// ..........
/// ..........
/// ```
/// The first example has antennas with two different frequencies, so the antinodes they create
/// look like this, plus an antinode overlapping the topmost A-frequency antenna:
///
/// ```
/// ......#....#
/// ...#....0...
/// ....#0....#.
/// ..#....0....
/// ....0....#..
/// .#....A.....
/// ...#........
/// #......#....
/// ........A...
/// .........A..
/// ..........#.
/// ..........#.
/// ```
/// Because the topmost A-frequency antenna overlaps with a 0-frequency antinode, there are 14
/// total unique locations that contain an antinode within the bounds of the map.
///
/// Calculate the impact of the signal. How many unique locations within the bounds of the map
/// contain an antinode?
///
/// To begin, get your puzzle input.
///
/// Answer: 247
///
/// --- Part Two ---
///
/// Watching over your shoulder as you work, one of The Historians asks if you took the effects of
/// resonant harmonics into your calculations.
///
/// Whoops!
///
/// After updating your model, it turns out that an antinode occurs at any grid position exactly in
/// line with at least two antennas of the same frequency, regardless of distance. This means that
/// some of the new antinodes will occur at the position of each antenna (unless that antenna is the
/// only one of its frequency).
///
/// So, these three T-frequency antennas now create many antinodes:
///
/// ```
/// T....#....
/// ...T......
/// .T....#...
/// .........#
/// ..#.......
/// ..........
/// ...#......
/// ..........
/// ....#.....
/// ..........
/// ```
/// In fact, the three T-frequency antennas are all exactly in line with two antennas, so they are
/// all also antinodes! This brings the total number of antinodes in the above example to 9.
///
/// The original example now has 34 antinodes, including the antinodes that appear on every antenna:
///
/// ```
/// ##....#....#
/// .#.#....0...
/// ..#.#0....#.
/// ..##...0....
/// ....0....#..
/// .#...#A....#
/// ...#..#.....
/// #....#.#....
/// ..#.....A...
/// ....#....A..
/// .#........#.
/// ...#......##
/// ```
/// Calculate the impact of the signal using this updated model. How many unique locations within
/// the bounds of the map contain an antinode?
///
/// Answer: 861
public class Day8 {

  private static final String INPUT_PATH = inputPath(Day8.class);
  public static final char ANTINODE_CHAR = '#';

  public static void overwrite(char[] source, char[] dest, char[] ignore) {
    for (int i = 0; i < source.length; i++) {
      var c = source[i];
      var skip = false;
      for (var ci : ignore) {
        if (c == ci) {
          skip = true;
          break;
        }
      }
      if (skip) {
        continue;
      }
      dest[i] = c;
    }
  }

  @FunctionalInterface
  interface AntiNodePairProcessor {
    long writeAntiNodes(Grid antiNodeMap, Util.Coord a, Util.Coord b);
  }

  record Result(Grid grid, Grid antinodes, long count) {}

  record Grid(char[] elements, int width, int height) {

    public static final Grid EMPTY = new Grid(new char[0], 0, 0);

    public char at(int x, int y) {
      return elements[x + y * width];
    }

    public void set(int x, int y, char val) {
      elements[x + y * width] = val;
    }

    // returns 0 if not within bounds
    public char atSafe(int x, int y) {
      return withinBounds(x, y) ? elements[x + y * width] : 0;
    }

    /// @return true if the grid changed, false if out of bounds or not changed
    public boolean setSafe(int x, int y, char val) {
      if (!withinBounds(x, y) || at(x, y) == val) {
        return false;
      }

      set(x, y, val);
      return true;
    }

    public boolean withinBounds(int x, int y) {
      return x >= 0 && y >= 0 && x < width && y < height;
    }

    public Grid copy() {
      return new Grid(elements.clone(), width, height);
    }

    /// Assumes str to be a valid grid, that is, every line has the same length
    public static Grid from(String str) {
      var lines = str.lines().toList();
      return from(lines);
    }

    /// Assumes lines to be a valid grid, that is, every line has the same length
    private static Grid from(List<String> lines) {
      if (lines.isEmpty()) {
        return EMPTY;
      }

      int width = lines.getFirst().length();
      int height = lines.size();
      var chars = String.join("", lines).toCharArray();

      return new Grid(chars, width, height);
    }

    public String toString() {
      StringBuilder stringBuilder = new StringBuilder(elements.length + height);
      for (int j = 0; j < height; j++) {
        stringBuilder.append(elements, width * j, width);
        stringBuilder.append('\n');
      }
      return stringBuilder.toString();
    }
  }

  static Grid inputFromFile(String path) {
    return Grid.from(getFileAsLines(path));
  }

  static class Part1 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Grid grid) {

      return Day8.process(grid, Day8::writeAntiNodePair).count();
    }
  }

  private static Result process(Grid grid, AntiNodePairProcessor writer) {
    var antiNodeGrid = grid.copy();

    Map<Character, List<Util.Coord>> charToCoord = new HashMap<>();

    for (int y = 0; y < grid.height; y++) {
      for (int x = 0; x < grid.width; x++) {
        char c = grid.at(x, y);
        if (c == '.') {
          continue;
        }
        var set = charToCoord.computeIfAbsent(c, k -> new ArrayList<>());
        set.add(new Util.Coord(x, y));
      }
    }

    long count = 0;
    for (char c : charToCoord.keySet()) {
      List<Util.Coord> coords = charToCoord.get(c);
      for (var i = coords.listIterator(); i.hasNext(); ) {
        Util.Coord coord0 = i.next();
        for (var j = coords.listIterator(i.nextIndex()); j.hasNext(); ) {
          Util.Coord coord1 = j.next();
          count += writer.writeAntiNodes(antiNodeGrid, coord1, coord0);
        }
      }
    }
    System.out.println(grid);
    System.out.println();
    System.out.println(antiNodeGrid);
    return new Result(grid, antiNodeGrid, count);
  }

  private static long writeAntiNodePair(Grid antiNodeGrid, Util.Coord coord1, Util.Coord coord0) {
    var x01 = coord1.x() - coord0.x();
    var y01 = coord1.y() - coord0.y();

    long count = 0;
    count += writeAntiNode(antiNodeGrid, coord1.x() + x01, coord1.y() + y01);
    count += writeAntiNode(antiNodeGrid, coord0.x() - x01, coord0.y() - y01);
    return count;
  }

  private static long writeAntiNode(Grid antiNodeGrid, int coord0, int coord1) {
    long count = 0;
    if (antiNodeGrid.setSafe(coord0, coord1, ANTINODE_CHAR)) {
      ++count;
    }

    return count;
  }

  private static long writeAntiNodePairRepeat(Grid antiNodeGrid, Util.Coord coord1, Util.Coord coord0) {
    var x01 = coord1.x() - coord0.x();
    var y01 = coord1.y() - coord0.y();

    long count = 0;

    count += writeAntiNodeRepeat(antiNodeGrid, coord0, x01, y01);
    count += writeAntiNodeRepeat(antiNodeGrid, coord1, -x01, -y01);
    return count;
  }

  private static long writeAntiNodeRepeat(Grid antiNodeGrid, Util.Coord coord, int x01, int y01) {
    long count = 0;
    var i = 0;
    var j = 0;
    while (antiNodeGrid.withinBounds(coord.x() + x01 * i, coord.y() + y01 * j)) {
      count += writeAntiNode(antiNodeGrid, coord.x() + x01 * i, coord.y() + y01 * j);
      ++i;
      ++j;
    }
    return count;
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH).count);
    }

    static Result solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static Result solve(Grid grid) {
      return process(grid, Day8::writeAntiNodePairRepeat);
    }
  }
}
