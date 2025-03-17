package advent.of.code.day2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static advent.of.code.util.Util.getFileAsLines;

public class ReportLoader {

  /**
   * @return a mutable list of reports with mutable levels
   */
  public static List<Report> loadReports(String path) {
    var lines = getFileAsLines(path);
    return lines.stream().map(ReportLoader::lineToReport).collect(Collectors.toList());
  }

  private static Report lineToReport(String line) {
    return new Report(
        Arrays.stream(line.split(" ")).map(Integer::parseUnsignedInt).collect(Collectors.toList()));
  }
}
