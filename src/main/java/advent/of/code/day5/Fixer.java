package advent.of.code.day5;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.*;

class Fixer {
  private final Map<Integer, Set<Integer>> transitionTable;
  private final Set<Day5.Rule> rules;

  private Fixer(Set<Day5.Rule> rules, Map<Integer, Set<Integer>> transitionTable) {
    this.rules = rules;
    this.transitionTable = transitionTable;
  }

  public static Fixer from(Set<Day5.Rule> rules) {
    var ret =
            rules.stream()
                    .collect(groupingBy(Day5.Rule::a, mapping(Day5.Rule::b, toUnmodifiableSet())));

    return new Fixer(rules, unmodifiableMap(ret));
  }

  public List<Integer> fix(List<Integer> update) {
    // for debugging
    var brokenRules = rules.stream().filter(rule -> !rule.matches(update)).toList();
    System.out.printf("fixing %s, which broke rules %s%n", update, brokenRules);

    var ret =
            update.stream().sorted((a, b) -> precedes(a, b) ? -1 : precedes(b, a) ? 1 : 0).toList();

    // for debugging
    System.out.printf("result is %s%n", ret);
    if (brokenRules.isEmpty()) {
      if (!ret.equals(update)) {
        throw new AssertionError(
                "fix of unbroken update shouldn't change order, yet %s!=%s".formatted(ret, update));
      }
    } else {
      brokenRules = rules.stream().filter(rule -> !rule.matches(ret)).toList();
      if (!brokenRules.isEmpty())
        throw new AssertionError("rules broken by supposed fix %s: %s".formatted(ret, brokenRules));
    }

    return ret;
  }

  private boolean precedes(int a, int b) {
    Set<Integer> successors = transitionTable.getOrDefault(a, Set.of());

    return successors.stream().anyMatch(s -> s == b);
  }
}
