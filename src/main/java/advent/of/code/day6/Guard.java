package advent.of.code.day6;

import java.util.*;

public class Guard {
  private GuardDirection state;
  private int x;
  private int y;
  private boolean loopComplete = false;

  public static Guard from(Entry first) {
    return new Guard(first.dir, first.x(), first.y());
  }

  public List<Point> getTilesSteppedOn() {
    return history().stream().map(e -> new Point(e.x(), e.y())).distinct().toList();
  }

  public record Entry(int x, int y, GuardDirection dir) {}

  List<Entry> history = new ArrayList<>();
  Set<Entry> historySet = new HashSet<>();

  public Guard(GuardDirection state, int x, int y) {
    this.state = state;
    this.x = x;
    this.y = y;
    addHistoryEntry(toEntry());
  }

  private void addHistoryEntry(Entry entry) {
    history.add(entry);
    historySet.add(entry);
  }

  public boolean advance(World world) {
    var i = state.x();
    var j = state.y();

    int x1 = x + i;
    int y1 = y + j;
    var tileOpt = world.mappedArea().at(x1, y1);

    if (tileOpt.isEmpty()) { // out of bounds
      x = x1;
      y = y1;
      return false;
    }
    var tile = tileOpt.get();

    if (tile.equals(MapTile.WALL)) {
      state = state.onWall();
    } else {
      x = x1;
      y = y1;
    }
    var entry = toEntry();
    if (historySet.contains(entry)) {
      loopComplete = true;
    } else {
      addHistoryEntry(entry);
    }
    return true;
  }

  public List<Entry> history() {
    return Collections.unmodifiableList(history);
  }

  private Entry toEntry() {
    return new Entry(x, y, state);
  }

  public boolean isLoopComplete() {
    return loopComplete;
  }

  public GuardDirection state() {
    return state;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }
}
