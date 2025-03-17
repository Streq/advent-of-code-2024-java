package advent.of.code.day12;

import advent.of.code.util.CharGrid;
import advent.of.code.util.IntGrid;

import java.util.*;

import static advent.of.code.util.Util.*;

public class Day12 {
  private static final String INPUT_PATH = inputPath(Day12.class);

  static CharGrid inputFromFile(String path) {
    return CharGrid.from(getFileAsString(path));
  }

  record Id(char type, long id) implements Comparable<Id> {

    public static final Id NO_ID = new Id('#', 0);

    public String toString() {
      return type + "" + id;
    }

    @Override
    public int compareTo(Id o) {
      int t = Character.compare(type, o.type);
      if (t != 0) {
        return t;
      }
      return Long.compare(id, o.id);
    }
  }

  record Info(Grid<Id> idMap, IntGrid perimeterMap, AliasMap<Id> aliases, IntGrid fenceMap) {}

  private static final long RIGHT_FENCE = 0b0001;
  private static final long DOWN_FENCE = 0b0010;
  private static final long LEFT_FENCE = 0b0100;
  private static final long UP_FENCE = 0b1000;

  private static Info extractInfo(CharGrid grid) {
    Grid<Id> idMap =
        new Grid<>(new Id[grid.width() * grid.height()], grid.width(), grid.height(), Id.NO_ID);
    Arrays.fill(idMap.elements(), Id.NO_ID);
    IntGrid perimeterMap =
        new IntGrid(new long[grid.width() * grid.height()], grid.width(), grid.height(), 0);
    IntGrid fenceMap =
        new IntGrid(new long[grid.width() * grid.height()], grid.width(), grid.height(), 0);

    Arrays.fill(perimeterMap.elements(), 0);

    AliasMap<Id> aliases = new AliasMap<>();

    Counter<Character> idIndex = new Counter<>();

    // top fence
    for (int x = 0; x < grid.width(); x++) {
      perimeterMap.add(x, 0, 1);
    }
    // left fence
    for (int y = 0; y < grid.height(); y++) {
      perimeterMap.add(0, y, 1);
    }

    for (int y = 0; y < grid.height(); y++) {
      for (int x = 0; x < grid.width(); x++) {
        var type = grid.at(x, y);
        Id id = idMap.at(x, y);
        if (id == Id.NO_ID) {
          id = new Id(type, idIndex.getAndIncrement(type));
          idMap.set(x, y, id);
        }
        for (int[] dir : RIGHTDOWN) {
          var x1 = x + dir[0];
          var y1 = y + dir[1];

          if (grid.atSafe(x1, y1) != type) {
            perimeterMap.add(x, y, 1);
            perimeterMap.addSafe(x1, y1, 1);
          } else {
            Id otherId = idMap.atSafe(x1, y1);
            if (otherId == Id.NO_ID) {
              idMap.setSafe(x1, y1, id);
            } else if (otherId != id) {
              aliases.alias(id, otherId);
            }
          }
        }
      }
    }
    return new Info(idMap, perimeterMap, aliases, fenceMap);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(CharGrid input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(CharGrid input) {
      log(input);
      return getAreaPerimeterSum(input);
    }

    private static long getAreaPerimeterSum(CharGrid grid) {

      Info info = extractInfo(grid);
      log(info.aliases);
      log(info.idMap.toString());
      log(info.perimeterMap);

      var aliases = info.aliases;
      var idMap = info.idMap;
      var perimeterMap = info.perimeterMap;

      Map<Id, Long> perimeterCounter = new HashMap<>();
      Map<Id, Long> areaCounter = new HashMap<>();

      for (int y = 0; y < grid.height(); y++) {
        for (int x = 0; x < grid.width(); x++) {
          Id id = aliases.toOgId(idMap.at(x, y));
          areaCounter.compute(id, (k, counter) -> (counter == null) ? 1 : counter + 1);
          long perimeter = perimeterMap.at(x, y);
          perimeterCounter.compute(
              id, (k, counter) -> (counter == null) ? perimeter : counter + perimeter);
        }
      }

      long count = 0;
      for (Id id : perimeterCounter.keySet()) {
        long area = areaCounter.get(id);
        long perimeter = perimeterCounter.get(id);
        var price = perimeter * area;

        log("region %s has area %d and perimeter %d, price is %d", id, area, perimeter, price);

        count += price;
      }

      log("total price is %d", count);
      return count;
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(CharGrid input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(CharGrid input) {
      return getAreaFenceCountSum(input);
    }

    private static long getAreaFenceCountSum(CharGrid grid) {
      Info info = extractInfo(grid);
      log(grid);
      log(info.aliases);
      log(info.idMap.toString());
      log(info.perimeterMap);

      var aliases = info.aliases;
      var idMap = info.idMap;

      Counter<Id> fenceCounter = new Counter<>();
      Counter<Id> areaCounter = new Counter<>();

      for (int y = 0; y < grid.height(); y++) {
        for (int x = 0; x < grid.width(); x++) {
          Id id = aliases.toOgId(idMap.at(x, y));
          areaCounter.incrementAndGet(id);
        }
      }

      Id lastTopFence = Id.NO_ID;
      Id lastBotFence = Id.NO_ID;

      // horizontal fences
      for (int y = 0; y < grid.height(); y++) {
        for (int x = 0; x < grid.width(); x++) {
          Id id = aliases.toOgId(idMap.at(x, y));
          Id id1 = aliases.toOgId(idMap.atSafe(x, y - 1));
          Id id2 = aliases.toOgId(idMap.atSafe(x, y + 1));
          boolean hasTopFence = !id.equals(id1);
          boolean hasBotFence = !id.equals(id2);
          if (hasTopFence) {
            if (lastTopFence != id) {
              fenceCounter.incrementAndGet(id);
              lastTopFence = id;
            }
          } else {
            lastTopFence = Id.NO_ID;
          }
          if (hasBotFence) {
            if (lastBotFence != id) {
              fenceCounter.incrementAndGet(id);
              lastBotFence = id;
            }
          } else {
            lastBotFence = Id.NO_ID;
          }
        }
      }

      Id lastLeftFence = Id.NO_ID;
      Id lastRightFence = Id.NO_ID;
      // vertical fences
      for (int x = 0; x < grid.width(); x++) {
        for (int y = 0; y < grid.height(); y++) {
          Id id = aliases.toOgId(idMap.at(x, y));
          Id id1 = aliases.toOgId(idMap.atSafe(x - 1, y));
          Id id2 = aliases.toOgId(idMap.atSafe(x + 1, y));
          boolean hasLeftFence = !id.equals(id1);
          boolean hasRightFence = !id.equals(id2);
          if (hasLeftFence) {
            if (lastLeftFence != id) {
              fenceCounter.incrementAndGet(id);
              lastLeftFence = id;
            }
          } else {
            lastLeftFence = Id.NO_ID;
          }
          if (hasRightFence) {
            if (lastRightFence != id) {
              fenceCounter.incrementAndGet(id);
              lastRightFence = id;
            }
          } else {
            lastRightFence = Id.NO_ID;
          }
        }
      }

      long count = 0;
      for (Id id : fenceCounter.keySet()) {
        long area = areaCounter.get(id);
        long fenceCount = fenceCounter.get(id);
        var price = fenceCount * area;

        log("region %s has area %d and %d fences, price is %d", id, area, fenceCount, price);

        count += price;
      }

      log("total price is %d", count);
      return count;
    }
  }
}
