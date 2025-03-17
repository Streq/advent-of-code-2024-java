package advent.of.code.day21;

import advent.of.code.util.CharGrid;
import advent.of.code.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static advent.of.code.util.Util.toDir;

public class PathMatrix {

  static final CharGrid NUMPAD =
      CharGrid.from(
          """
              789
              456
              123
               0A
              """);

  public static final PathMatrix NUM_MATRIX = new PathMatrix(NUMPAD);
  static final CharGrid DIRPAD =
      CharGrid.from(
          """
               ^A
              <v>
              """);
  public static final PathMatrix DIR_MATRIX = new PathMatrix(DIRPAD);

  private final Map<Character, Map<Character, List<String>>> pathMap = new HashMap<>();

  public PathMatrix(CharGrid grid) {
    grid.forEach(
        (x, y, c) -> {
          HashMap<Character, List<String>> map = new HashMap<>();
          pathMap.put(c, map);

          grid.forEach(
              (x1, y1, c1) -> {
                if (c1 == ' ') {
                  return false;
                }
                var pathGrid = grid.findPaths(x, y, new char[] {' '});
                var paths = Util.findAllShortestPaths(pathGrid, x1, y1);
                map.put(
                    c1,
                    paths.stream()
                        .map(
                            path -> {
                              var seq = new StringBuilder();
                              for (int i = 0; i < path.size() - 1; i++) {
                                var p0 = path.get(i);
                                var p1 = path.get(i + 1);
                                var dir = toDir(p1.x() - p0.x(), p1.y() - p0.y());
                                seq.append(
                                    switch (dir) {
                                      case 0 -> '>';
                                      case 1 -> 'v';
                                      case 2 -> '<';
                                      case 3 -> '^';
                                      default -> throw new IllegalStateException();
                                    });
                              }
                              seq.append('A');
                              return seq.toString();
                            })
                        .toList());
                return false;
              });
          return false;
        });
  }

  public List<String> pathsTo(char a, char b) {
    return pathMap.get(a).get(b);
  }
}
