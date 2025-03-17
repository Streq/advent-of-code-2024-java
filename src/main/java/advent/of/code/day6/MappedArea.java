package advent.of.code.day6;

import java.util.*;

public final class MappedArea {

  private final List<List<MapTile>> lines;

  private final int width;
  private final int height;
  private Point obstacle = null;

  public void setObstacle(Point obstacle) {
    this.obstacle = null;

    if (atOrThrow(obstacle.x(), obstacle.y()).isWall()) {
      throw new RuntimeException(
          "Can't put an obstacle where there's a wall: %s".formatted(obstacle));
    }

    this.obstacle = obstacle;
  }

  public MappedArea(List<List<MapTile>> lines) {
    this.lines = lines;
    this.width = lines.getFirst().size();
    this.height = lines.size();
  }

  Optional<MapTile> at(int x, int y) {
    return Optional.ofNullable(atUnsafe(x, y));
  }

  MapTile atOrThrow(int x, int y) {
    return Objects.requireNonNull(atUnsafe(x, y));
  }

  MapTile atUnsafe(int x, int y) {
    if (!isWithinBounds(x, y)) {
      return null;
    }
    if (obstacle != null && x == obstacle.x() && y == obstacle.y()) {
      return MapTile.WALL;
    }

    return lines.get(y).get(x);
  }

  int width() {
    return width;
  }

  int height() {
    return height;
  }

  public boolean isWithinBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width() && y < height();
  }
}
