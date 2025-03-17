package advent.of.code.day6;

import java.util.List;

public class World {
  private final MappedArea mappedArea;
  private final List<Guard> guards;

  public World(MappedArea mappedArea, List<Guard> guards) {
    this.mappedArea = mappedArea;
    this.guards = guards;
  }

    public List<Guard> guards() {
        return guards;
    }

    public MappedArea mappedArea() {
        return mappedArea;
    }
}
