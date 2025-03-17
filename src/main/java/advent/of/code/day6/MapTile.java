package advent.of.code.day6;

public enum MapTile {
  NOTHING('.'),
  WALL('#');

  public char printChar() {
    return printChar;
  }

  private final char printChar;
  private final String printStr;

  MapTile(char printChar) {
    this.printChar = printChar;
    this.printStr = String.valueOf(printChar);
  }

  public static boolean isWall(char c) {
    return c == WALL.printChar;
  }

  public boolean isWall(){
    return WALL==this;
  }

  public String toString() {
    return printStr;
  }
}
