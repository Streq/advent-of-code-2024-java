package advent.of.code.day23;

import java.util.*;

import static advent.of.code.util.Util.*;

public class Day23 {

  private static final String INPUT_PATH = inputPath(Day23.class);

  record Entry(String a, String b) {}

  static SortedMap<String, SortedSet<String>> inputFromFile(String path) {
    SortedMap<String, SortedSet<String>> ret = new TreeMap<>();
    getFileAsLines(path).stream()
        .map(l -> l.split("-"))
        .map(s -> Arrays.stream(s).sorted().toArray(String[]::new))
        .map(s -> new Entry(s[0], s[1]))
        .distinct()
        .forEach(e -> ret.computeIfAbsent(e.a, _ -> new TreeSet<>()).add(e.b));
    return ret;
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(SortedMap<String, SortedSet<String>> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(SortedMap<String, SortedSet<String>> input) {

      long counter = 0;
      for (Map.Entry<String, SortedSet<String>> entry : input.sequencedEntrySet()) {
        var a = entry.getKey();
        var set = entry.getValue();
        var bs = set.stream().toList();
        for (int j = 0; j < bs.size(); j++) {
          var b = bs.get(j);
          for (int k = j + 1; k < bs.size(); k++) {
            var c = bs.get(k);
            if (input.containsKey(b)
                && input.get(b).contains(c)
                && (a.startsWith("t") || b.startsWith("t") || c.startsWith("t"))) {
              counter++;
            }
          }
        }
      }

      return counter;
    }
  }

  static class Part2 {
    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static String solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static String solve(SortedMap<String, SortedSet<String>> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static String solveInternal(SortedMap<String, SortedSet<String>> input) {

      SortedSet<String> largestSet = new TreeSet<>();
      for (Map.Entry<String, SortedSet<String>> entry : input.sequencedEntrySet()) {
        var largestSetOfThisEntry = findLargestSet(input, entry);
        if (largestSetOfThisEntry.size() > largestSet.size()) {
          largestSet = largestSetOfThisEntry;
        }
      }

      return String.join(",", largestSet);
    }

    /// iterate every element B linked to A, for every element C not yet iterated:
    /// - is C linked to B?
    ///   - yes: then they are a group, push the group to the stack, if this is the largest group
    ///     found until now, then set largestGroup to it, continue
    ///
    /// generalized to N-sized groups
    /// for every group in the stack:
    ///   - get the index of the last element, for every element after it:
    ///     - is it linked to every element in the group?
    ///       - yes: then it is a group, push the group to the stack
    ///
    private static SortedSet<String> findLargestSet(
        SortedMap<String, SortedSet<String>> input, Map.Entry<String, SortedSet<String>> entry) {
      var a = entry.getKey();
      var bs = entry.getValue().stream().toList();
      Deque<List<Integer>> stack = new ArrayDeque<>();
      for (int i = 0; i < bs.size(); i++) {
        stack.push(List.of(i));
      }

      List<Integer> largestGroup = List.of(0);
      while (!stack.isEmpty()) {
        List<Integer> group = stack.pop();
        int last = group.getLast();
        for (int i = last + 1; i < bs.size(); i++) {
          String s1 = bs.get(i);
          if (group.stream()
              .map(bs::get)
              .map(input::get)
              .allMatch(set -> set != null && set.contains(s1))) {
            ArrayList<Integer> group1 = new ArrayList<>(group.size() + 1);
            group1.addAll(group);
            group1.add(i);
            stack.push(group1);
            if (group1.size() > largestGroup.size()) {
              largestGroup = group1;
            }
          }
        }
      }

      SortedSet<String> ret = new TreeSet<>();
      ret.add(a);
      largestGroup.stream().map(bs::get).forEach(ret::add);
      return ret;
    }
  }
}
