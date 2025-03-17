package advent.of.code.day6;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum GuardDirection {
  UP('^'),
  RIGHT('>'),
  DOWN('v'),
  LEFT('<');
  public final char printChar;
  private final String printStr;

  static final Map<Character, GuardDirection> CHAR_TO_STATE =
      Arrays.stream(GuardDirection.values()).collect(Collectors.toMap(p -> p.printChar, p -> p));

  GuardDirection(char printChar) {
    this.printChar = printChar;
    this.printStr = String.valueOf(printChar);
  }

  public String toString() {
    return printStr;
  }

  public static boolean isGuard(char c) {
    return CHAR_TO_STATE.containsKey(c);
  }

  public static GuardDirection fromChar(char c) {
    return CHAR_TO_STATE.get(c);
  }

  public GuardDirection onWall() {
    return switch (this) {
      case UP -> RIGHT;
      case RIGHT -> DOWN;
      case DOWN -> LEFT;
      case LEFT -> UP;
    };
  }

  public int x() {
    return switch (this) {
      case UP, DOWN -> 0;
      case RIGHT -> 1;
        case LEFT -> -1;
    };
  }

  public int y() {
    return switch (this) {
      case UP -> -1;
      case RIGHT, LEFT -> 0;
      case DOWN -> 1;
    };
  }
}
