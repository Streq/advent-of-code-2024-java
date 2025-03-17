package advent.of.code.day6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldReader {
  public static List<String> toLines(World world) {

    var lines = new ArrayList<StringBuilder>();

    MappedArea mappedArea = world.mappedArea();
    for (int j = 0; j < mappedArea.height(); j++) {
      var builder = new StringBuilder();
      for (int i = 0; i < mappedArea.width(); i++) {
        builder.append(mappedArea.at(i, j).orElseThrow().printChar());
      }
      lines.add(builder);
    }

    for (var guard : world.guards()) {
      for (var entry : guard.history()) {
        lines.get(entry.y()).setCharAt(entry.x(), entry.dir().printChar);
      }
      var x = guard.x();
      var y = guard.y();
      if (mappedArea.isWithinBounds(x, y)) {
        lines.get(y).setCharAt(x, guard.state().printChar);
      }
    }

    return lines.stream().map(StringBuilder::toString).toList();
  }

  public static World fromLines(List<String> lines) {

    List<List<MapTile>> map = new ArrayList<>(lines.size());
    List<Guard> guards = new ArrayList<>();

    for (int j = 0; j < lines.size(); j++) {
      String line = lines.get(j);
      List<MapTile> mapLine = new ArrayList<>(line.length());
      for (int i = 0; i < line.length(); i++) {
        var c = line.charAt(i);

        if (MapTile.isWall(c)) {
          mapLine.add(MapTile.WALL);
        } else {
          mapLine.add(MapTile.NOTHING);
        }

        if (GuardDirection.isGuard(c)) {
          guards.add(new Guard(GuardDirection.fromChar(c), i, j));
        }
      }
      map.add(Collections.unmodifiableList(mapLine));
    }

    return new World(new MappedArea(Collections.unmodifiableList(map)), guards);
  }
}
