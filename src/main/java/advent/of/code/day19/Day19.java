package advent.of.code.day19;

import java.util.*;
import java.util.stream.Stream;

import static advent.of.code.util.Util.*;

public class Day19 {

  record Input(List<String> parts, List<String> designs) {}

  private static final String INPUT_PATH = inputPath(Day19.class);

  static Input inputFromFile(String path) {
    List<String> fileAsLines = getFileAsLines(path);

    var parts =
        Arrays.stream(fileAsLines.getFirst().split(", "))
            .sorted(Comparator.reverseOrder())
            .toList();
    var designs = fileAsLines.subList(2, fileAsLines.size());

    return new Input(parts, designs);
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Input input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Input input) {
      log(input.designs);
      log(input.parts);
      return input.designs.stream().filter(design -> canBeMadeBy(design, input.parts)).count();
    }
  }

  static class Part2 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Input input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Input input) {
      log(input.designs);
      log(input.parts);
      HashMap<String, Long> cache = new HashMap<>();
      long ret =
          input.designs.stream()
              .mapToLong(design -> countBeMadeBy(design, input.parts, cache))
              .sum();
      // log(cache);
      return ret;
    }
  }

  private static boolean canBeMadeBy(String design, List<String> parts) {
    record Todo(String substring, List<String> parts) {}
    var queue = new ArrayDeque<Todo>();
    queue.add(new Todo(design, parts));
    while (!queue.isEmpty()) {
      var todo = queue.removeLast();
      var subDesign = todo.substring;
      if (subDesign.isEmpty()) {
        log("%s ok", design);
        return true;
      }

      parts = todo.parts.stream().filter(subDesign::contains).toList();
      for (String part : parts) {
        if (subDesign.startsWith(part)) {
          queue.add(new Todo(subDesign.substring(part.length()), parts));
        }
      }
    }
    log("%s nok", design);
    return false;
  }

  private static long countBeMadeBy(String design, List<String> parts, Map<String, Long> cache) {
    long ret = 0;

    record Node(List<String> path, String subPath, List<String> parts) {
      public void addBranches(Deque<Node> stack) {
        var newPath =
            Stream.of(path.stream(), Stream.of(Node.this.subPath)).flatMap(s -> s).toList();
        for (String part : parts) {
          if (subPath.startsWith(part)) {
            String newSubPath = subPath.substring(part.length());
            var newParts = parts.stream().filter(newSubPath::contains).toList();
            stack.addLast(new Node(newPath, newSubPath, newParts));
          }
        }
      }
    }
    var stack = new ArrayDeque<Node>();
    stack.add(new Node(List.of(), design, parts));

    while (!stack.isEmpty()) {
      var node = stack.removeLast();
      List<String> path = node.path;
      String subDesign = node.subPath;

      long solvedValue = subDesign.isEmpty() ? 1 : cache.getOrDefault(subDesign, 0L);
      if (solvedValue > 0) {
        for (String s : path) {
          // We increment the cached value for every ancestor.
          // Using a stack prevents ever reading a cached value that's still growing:
          // the first time we encounter a string that's not in the cache, we process all of its
          // branches recursively, meaning we can only ever re-encounter a said string when we're
          // done with all of its substrings, and therefore itself.
          // Think of it as counting the leaves on a literal tree:
          // - we start with the trunk,
          // - we look for a branch
          // - then we look for a branch in said branch
          // - repeat until finding a branch that has no branches
          // - if that branch has a leaf, then we know the branch it branches from has (at least) a
          // leaf, and the branch said branch branches from has at least a leaf, and so on until we
          // arrive at the fact that the tree itself has at least a leaf
          // - when we are done counting the leaves for that branch, we continue with the next
          // branch that stems from the same branch as this one
          // - when the leaves for all branches that stem from a branch have been counted, then that
          // branch has been counted, meaning we can proceed with that branch's parent's siblings,
          // and so on
          // - this means that, as long as we are not finished counting a branch, we cannot
          // encounter a branch as big as said branch (let alone identical to it), because by a
          // tree's definition, a branch can never have a branch as "big" (number of child
          // nodes+leaves) as itself.
          cache.compute(s, (_, b) -> (b == null ? 0 : b) + solvedValue);
        }
        ret += solvedValue;
        continue;
      }
      node.addBranches(stack);
    }
    log("%s:%d", design, ret);
    return ret;
  }
}
