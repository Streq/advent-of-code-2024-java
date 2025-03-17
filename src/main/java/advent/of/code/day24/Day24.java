package advent.of.code.day24;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static advent.of.code.util.Util.*;
import static java.util.function.Predicate.not;

public class Day24 {

  private static final String INPUT_PATH = inputPath(Day24.class);

  private static final Pattern REGEX_INIT_VALUES = Pattern.compile("(\\w{3}): (\\d)");
  private static final String SEPARATOR = "\n\n";
  private static final Pattern REGEX_GATES =
      Pattern.compile("(\\w{3}) (\\w+) (\\w{3}) -> (\\w{3})");

  sealed interface Value permits LiteralValue, Gate {}

  record LiteralValue(boolean value) implements Value {}

  sealed interface GateTree extends Comparable<GateTree> permits GateBranch, Gate {

    String flattenLeaves();

    @Override
    default int compareTo(GateTree o) {
      return -flattenLeaves().compareTo(o.flattenLeaves());
    }

    GateTree as(String op);

    String operation();
  }

  record GateBranch(String operation, GateTree a, GateTree b) implements GateTree {
    public String toString() {
      return operation + "(\n" + (a + ",\n" + b).indent(1) + ")";
    }

    public static GateBranch from(String op, GateTree a, GateTree b) {
      if (a != null && a.compareTo(b) > 0) {
        var aux = a;
        a = b;
        b = aux;
      }

      return new GateBranch(op, a, b);
    }

    public static GateBranch xor(GateTree a, GateTree b) {
      return from("XOR", a, b);
    }

    public static GateBranch and(GateTree a, GateTree b) {
      return from("AND", a, b);
    }

    public static GateBranch or(GateTree a, GateTree b) {
      return from("OR", a, b);
    }

    @Override
    public String flattenLeaves() {
      return b.flattenLeaves() + a.flattenLeaves() + operation;
    }

    @Override
    public GateTree as(String op) {
      return GateBranch.from(op, a, b);
    }
  }

  record Gate(String operation, String a, String b) implements Value, GateTree {
    public String toString() {
      return operation + "(" + a + ", " + b + ")";
    }

    public boolean isInputGate() {
      return a.startsWith("x") && b.startsWith("y");
    }

    public static Gate from(String a, String b, String op) {
      var lst = sorted(a, b);
      return new Gate(op, lst.getFirst(), lst.getLast());
    }

    public static Gate xor(String a, String b) {
      return from(a, b, "XOR");
    }

    public static Gate and(String a, String b) {
      return from(a, b, "AND");
    }

    public static Gate or(String a, String b) {
      return from(a, b, "OR");
    }

    public static boolean operate(String operator, boolean a, boolean b) {
      return switch (operator) {
        case "AND" -> a && b;
        case "OR" -> a || b;
        case "XOR" -> a ^ b;
        default -> throw new IllegalStateException("operation " + operator + " not contemplated");
      };
    }

    @Override
    public String flattenLeaves() {
      return b + a + operation;
    }

    @Override
    public GateTree as(String op) {
      return Gate.from(a, b, op);
    }
  }

  private static List<String> sorted(String a, String b) {
    return Stream.of(a, b).sorted().toList();
  }

  static Map<String, Value> inputFromFile(String path) {
    String[] split = getFileAsString(path).split(SEPARATOR);
    var inputs = split[0];
    var gates = split[1];

    Map<String, Value> bitmap = new HashMap<>();
    REGEX_INIT_VALUES
        .matcher(inputs)
        .results()
        .forEach(
            r -> {
              var bitName = r.group(1);
              var bitValue = r.group(2).equals("1");
              bitmap.put(bitName, new LiteralValue(bitValue));
            });

    REGEX_GATES
        .matcher(gates)
        .results()
        .forEach(
            r -> {
              String op = r.group(2);
              String a = r.group(1);
              String b = r.group(3);
              var c = r.group(4);
              bitmap.put(c, Gate.from(a, b, op));
            });
    return bitmap;
  }

  static class Part1 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static long solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static long solve(Map<String, Value> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static long solveInternal(Map<String, Value> input) {
      Set<String> keys = input.keySet();

      var zs = keys.stream().filter(k -> k.startsWith("z")).sorted().toList();
      log(zs);

      long zVal = 0;

      for (int i = 0; i < zs.size(); i++) {
        if (solveValueFor(zs.get(i), input)) {
          zVal |= 1L << i;
        }
      }
      log(input);
      return zVal;
    }

    private static boolean solveValueFor(String s, Map<String, Value> input) {
      LiteralValue val =
          switch (input.get(s)) {
            case LiteralValue lv -> lv;
            case Gate gt ->
                new LiteralValue(
                    Gate.operate(
                        gt.operation, solveValueFor(gt.a, input), solveValueFor(gt.b, input)));
          };
      input.put(s, val);
      return val.value;
    }
  }

  static class Part2 {

    public static void main(String[] args) {
      System.out.println(solveFromFile(INPUT_PATH));
    }

    static String solveFromFile(String input) {
      return solve(inputFromFile(input));
    }

    static String solve(Map<String, Value> input) {
      return timeSolution(() -> solveInternal(input));
    }

    static String solveInternal(Map<String, Value> input) {
      // printAsTree(input);

      Map<String, Gate> keyToGate =
          input.entrySet().stream()
              .filter(e -> e.getValue() instanceof Gate)
              .collect(Collectors.toMap(Map.Entry::getKey, e -> (Gate) e.getValue()));

      var zs = input.keySet().stream().filter(k -> k.startsWith("z")).sorted().toList();

      GateTree previousZ = null;
      GateTree overflow = null;
      List<String> ret = new ArrayList<>();
      for (int i = 0; i < zs.size(); i++) {
        String z = zs.get(i);
        String x = z.replace('z', 'x');
        String y = z.replace('z', 'y');
        Gate add = Gate.xor(x, y);
        GateTree mustTree;
        if (i == 0) {
          mustTree = add;
        } else if (i == 1) {
          mustTree = GateBranch.xor(add, previousZ.as("AND"));
        } else if (i < zs.size() - 1) {
          mustTree = GateBranch.xor(add, overflow);
        } else {
          mustTree = overflow;
        }
        var loopPath = findLoopPath(z, keyToGate);
        if (loopPath != null) {

          log("%s doesn't track", z);
          log("%s causes a loop", loopPath);
          for (int j = loopPath.size() - 1; j >= 0; j--) {
            String badKey = loopPath.get(j);
            var otherKey = bruteforceFix(badKey, z, keyToGate, mustTree);
            if (otherKey != null) {

              i = -1;
              ret.add(badKey);
              ret.add(otherKey);
              break;
            }
          }
          continue;
        }
        var tree = toTree(z, keyToGate);

        if (mustTree.equals(tree)) {
          log("%s tracks", z);
        } else {
          log("%s doesn't track", z);
          var diff = getOddWires(z, mustTree, keyToGate);
          log(diff);
          for (var firstDiff : diff) {
            var otherKey = bruteforceFix(firstDiff, z, keyToGate, mustTree);
            if (otherKey != null) {
              i = -1;
              ret.add(otherKey);
              ret.add(firstDiff);
              break;
            }
          }
          continue;
        }

        previousZ = tree;
        overflow = GateBranch.or(add.as("AND"), tree.as("AND"));
      }

      return ret.stream().sorted().collect(Collectors.joining(","));
    }

    private static String bruteforceFix(
        String badKey, String z, Map<String, Gate> keyToGate, GateTree mustTree) {
      for (String otherKey : keyToGate.keySet()) {
        swap(keyToGate, badKey, otherKey);
        if (findLoopPath(z, keyToGate) == null && toTree(z, keyToGate).equals(mustTree)) {
          log("swapping %s with %s fixes it", badKey, otherKey);
          return otherKey;
        }
        swap(keyToGate, badKey, otherKey);
      }
      log("no key swap found that fixes %s", badKey);
      return null;
    }

    private static List<String> findLoopPath(String z, Map<String, Gate> keyToGate) {
      Set<String> visited = new HashSet<>();
      Deque<List<String>> toVisit = new ArrayDeque<>();
      toVisit.push(List.of(z));
      while (!toVisit.isEmpty()) {
        var path = toVisit.pop();
        // log(path);
        var node = path.getLast();
        var gate = keyToGate.get(node);
        if (gate == null) {
          continue;
        }
        if (visited.contains(node)) {
          return path;
        }

        toVisit.push(concat(path, gate.a));
        toVisit.push(concat(path, gate.b));
        visited.add(node);
      }
      return null;
    }

    private static List<String> getOddWires(
        String key, GateTree mustTree, Map<String, Gate> keyToGate) {
      record Node(List<String> path, String key, GateTree mustTree) {}
      Deque<Node> stack = new ArrayDeque<>();
      stack.push(new Node(List.of(), key, mustTree));

      List<List<String>> successfulPaths = new ArrayList<>();
      List<List<String>> failedPaths = new ArrayList<>();
      while (!stack.isEmpty()) {
        var node = stack.pop();
        key = node.key;
        var path = concat(node.path, key);
        mustTree = node.mustTree;
        var gate = keyToGate.get(key);
        if (!gate.operation().equals(mustTree.operation())) {
          failedPaths.add(path);
          continue;
        }

        if (gate.isInputGate()) {
          if (!(mustTree instanceof Gate g)) {
            failedPaths.add(path);
            continue;
          }

          if (!g.a.equals(gate.a) || !g.b.equals(gate.b)) {
            failedPaths.add(path);
            continue;
          }
          successfulPaths.add(path);
          continue;
        }

        if (!(!gate.isInputGate() && mustTree instanceof GateBranch branch)) {
          failedPaths.add(path);
          continue;
        }

        var mustA = branch.a();
        var mustB = branch.b();

        stack.push(new Node(path, gate.a, mustA));
        stack.push(new Node(path, gate.b, mustB));

        stack.push(new Node(path, gate.a, mustB));
        stack.push(new Node(path, gate.b, mustA));
      }
      successfulPaths = successfulPaths.stream().distinct().toList();
      List<List<String>> finalSuccessfulPaths = successfulPaths;
      failedPaths =
          failedPaths.stream()
              .distinct()
              .filter(not(successfulPaths::contains))
              .filter(
                  failed ->
                      finalSuccessfulPaths.stream()
                          .noneMatch(
                              successful ->
                                  successful.size() > failed.size()
                                      && successful.subList(0, failed.size()).equals(failed)))
              .toList();
      log("logging successful paths");

      for (List<String> successfulPath : successfulPaths) {
        log(successfulPath);
      }
      log("finished successful paths");
      log("logging failed paths");
      for (List<String> failedPath : failedPaths) {
        log(failedPath);
      }
      log("finished failed paths");
      List<String> candidates =
          failedPaths.stream().flatMap(Collection::stream).distinct().toList().reversed();
      log("possible swapped gates are %s", candidates);

      return candidates;
    }

    private static GateTree toTree(String key, Map<String, Gate> input) {
      var v = input.get(key);
      if (v.isInputGate()) {
        return v;
      }
      return GateBranch.from(v.operation, toTree(v.b, input), toTree(v.a, input));
    }
  }
}
