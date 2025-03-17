package advent.of.code.day17;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static advent.of.code.util.Util.log;

public final class Computer {

  enum OperandType {
    literal((c, n) -> n.longValue()),
    combo(
        (c, n) ->
            switch (n) {
              case 0, 1, 2, 3 -> n.longValue();
              case 4 -> c.A;
              case 5 -> c.B;
              case 6 -> c.C;
              default -> throw new AssertionError("invalid opcode");
            });

    private final BiFunction<Computer, Byte, Long> func;

    OperandType(BiFunction<Computer, Byte, Long> func) {
      this.func = func;
    }

    long toOperand(Computer computer, byte param) {
      return this.func.apply(computer, param);
    }
  }

  @FunctionalInterface
  interface Method {
    void execute(long operand, Computer computer);
  }

  enum Instruction {
    adv(OperandType.combo, (n, c) -> c.A = c.A >> n),
    bxl(OperandType.literal, (n, c) -> c.B ^= n),
    bst(OperandType.combo, (n, c) -> c.B = n & 0b111),
    jnz(OperandType.literal, (n, c) -> c.jump(n)),
    bxc(OperandType.literal, (n, c) -> c.B ^= c.C),
    out(OperandType.combo, (n, c) -> c.output.add((int) n & 0b111)),
    bdv(OperandType.combo, (n, c) -> c.B = c.A >> n),
    cdv(OperandType.combo, (n, c) -> c.C = c.A >> n);

    private final OperandType operandType;
    private final Method method;

    Instruction(OperandType operandType, Method method) {
      this.operandType = operandType;
      this.method = method;
    }

    public void execute(Computer c, long operand) {
      method.execute(operand, c);
    }
  }

  private void jump(long n) {
    if (A != 0) {
      idx = (int) n - 2;
    }
  }

  public long A;
  public long B;
  public long C;
  public final int[] program;
  public int idx = 0;
  public final List<Integer> output = new ArrayList<>();

  Computer(long A, long B, long C, int[] program) {
    this.A = A;
    this.B = B;
    this.C = C;
    this.program = program;
  }

  public String run() {
    int maxIdx = 0;
    while (idx < program.length) {
      int opcode = program[idx];
      int operand = program[idx + 1];

      Instruction instruction = Instruction.values()[opcode];
      long finalOperand = instruction.operandType.toOperand(this, (byte) operand);
      instruction.execute(this, finalOperand);
      idx += 2;
      maxIdx = Math.max(idx, maxIdx);
    }

    return output.stream().map(Objects::toString).collect(Collectors.joining(","));
  }

  public long findSelfProducingA() {
    var expected = Arrays.stream(program).boxed().toList();

    record Candidate(long value, int bits) {}
    Deque<Candidate> candidates = new ArrayDeque<>();

    // every digit depends on the first ten bits of A at most
    var bits = 10;

    var size = 1 << bits;

    for (int i = 0; i < size; i++) {
      var ret = runUntilOutOfBits(i, bits);
      if (expected.subList(0, ret.size()).equals(ret)) {
        candidates.push(new Candidate(i, bits));
      }
    }

    SortedSet<Long> answers = new TreeSet<>();
    while (!candidates.isEmpty()) {
      var candidate = candidates.removeFirst();

      bits = candidate.bits + 3;
      if (bits > 64) {
        continue;
      }

      for (int i = 0; i < 1 << 3; i++) {
        var v = candidate.value + ((long) i << candidate.bits);
        var ret = runUntilOutOfBits(v, bits);

        if (ret.size() > expected.size()) {
          continue;
        }
        if (!expected.subList(0, ret.size()).equals(ret)) {
          continue;
        }
        if (expected.equals(ret) && expected.equals(run(v))) {
          answers.add(v);
        }
        candidates.push(new Candidate(v, bits));
      }
    }

    log(answers);

    return answers.first();
  }

  // part 2
  public static List<Integer> run(long A) {
    List<Integer> output = new ArrayList<>();
    do {
      long B = (A & 0b111);
      B ^= 0b101;
      long C = A >> B;
      A >>= 3;
      B ^= 0b110;
      B ^= C;
      output.add((int) B & 0b111);
    } while (A > 0);
    return output;
  }

  List<Integer> runUntilOutOfBits(long A, int bits) {
    List<Integer> output = new ArrayList<>();
    do {
      long B = (A & 0b111);
      B ^= 0b101;
      long C = A >> B; // A shifted B bits
      // if available bits are less than the amount shifted by B
      // plus the amount that will be read into C
      if (bits < B + 3) {
        return output;
      }
      A >>= 3;
      bits -= 3;

      B ^= 0b110;
      B ^= C;
      output.add((int) B & 0b111);
    } while (A > 0);
    return output;
  }
}
