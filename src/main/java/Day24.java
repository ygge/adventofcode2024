import util.Util;

import java.util.*;
import java.util.function.BiFunction;

public class Day24 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static String part2(List<String> input) {
        var inputs = new HashMap<String, Boolean>();
        int index = 0;
        for (; index < input.size(); index++) {
            if (input.get(index).isBlank()) {
                break;
            }
            var s = input.get(index).split(": ");
            inputs.put(s[0], s[1].equals("1"));
        }
        var operations = new HashMap<String, Calc>();
        for (++index; index < input.size(); index++) {
            var s = input.get(index).split(" -> ");
            var ss = s[0].split(" ");
            var inputA = ss[0];
            var inputB = ss[2];
            if (inputA.compareTo(inputB) > 0) {
                inputA = ss[2];
                inputB = ss[0];
            }
            operations.put(s[1], new Calc(inputA, inputB, Operation.valueOf(ss[1]), s[1]));
        }
        var seen = new HashMap<String, String>();
        for (String x : inputs.keySet()) {
            if (x.charAt(0) == 'x') {
                var y = "y" + x.substring(1);
                var xor = operations.values().stream()
                        .filter(calc -> (calc.inputA.equals(x) && calc.inputB.equals(y)) || (calc.inputA.equals(y) && calc.inputB.equals(x)))
                        .filter(calc -> calc.op == Operation.XOR)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Cannot find XOR for " + x));
                var and = operations.values().stream()
                        .filter(calc -> (calc.inputA.equals(x) && calc.inputB.equals(y)) || (calc.inputA.equals(y) && calc.inputB.equals(x)))
                        .filter(calc -> calc.op == Operation.AND)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Cannot find AND for " + x));
                seen.put(x + "XOR" + y, xor.result);
                seen.put(x + "AND" + y, and.result);
            }
        }
        List<String> wires = new ArrayList<>();
        // hardcoded
        String error1 = "pmd";
        String error2 = "cgh";
        wires.add(error1);
        wires.add(error2);
        Collections.sort(wires);
        operations.put(error1, operations.get(error2).swap(error1));
        operations.put(error2, operations.get(error1).swap(error2));

        for (int x = 1; x < 45; ++x) {
            String xx = String.format("x%02d", x);
            String yy = String.format("y%02d", x);
            String xor = seen.get(xx + "XOR" + yy);
            String result = operations.values().stream()
                    .filter(operation -> operation.op == Operation.XOR)
                    .filter(operation -> operation.inputA.equals(xor) || operation.inputB.equals(xor))
                    .findFirst()
                    .map(operation -> operation.result)
                    .orElseThrow();
            String zResult = String.format("z%02d", x);
            if (!result.equals(zResult)) {
                wires.add(result);
                wires.add(zResult);
                Collections.sort(wires);
                operations.put(result, operations.get(zResult).swap(result));
                operations.put(zResult, operations.get(result).swap(zResult));
                if (wires.size() == 8) {
                    return String.join(",", wires);
                }
            }
        }
        throw new IllegalStateException();
    }

    private static long part1(List<String> input) {
        var inputs = new HashMap<String, Boolean>();
        int index = 0;
        for (; index < input.size(); index++) {
            if (input.get(index).isBlank()) {
                break;
            }
            var s = input.get(index).split(": ");
            inputs.put(s[0], s[1].equals("1"));
        }
        var operations = new HashMap<String, Calc>();
        for (++index; index < input.size(); index++) {
            var s = input.get(index).split(" -> ");
            var ss = s[0].split(" ");
            operations.put(s[1], new Calc(ss[0], ss[2], Operation.valueOf(ss[1]), s[1]));
        }
        while (!operations.isEmpty()) {
            for (Map.Entry<String, Calc> entry : operations.entrySet()) {
                if (inputs.containsKey(entry.getValue().inputA)
                    && inputs.containsKey(entry.getValue().inputB)) {
                    inputs.put(entry.getKey(), entry.getValue().calculate(inputs));
                }
            }
            for (String seen : inputs.keySet()) {
                operations.remove(seen);
            }
        }
        long value = 0;
        for (int i = 0; ; ++i) {
            var key = String.format("z%02d", i);
            if (!inputs.containsKey(key)) {
                break;
            }
            if (inputs.get(key)) {
                value += 1L << i;
            }
        }
        return value;
    }

    private enum Operation {
        AND((a, b) -> a && b),
        OR((a, b) -> a || b),
        XOR((a, b) -> a ^ b);

        private final BiFunction<Boolean, Boolean, Boolean> operation;

        Operation(BiFunction<Boolean, Boolean, Boolean> operation) {
            this.operation = operation;
        }

        public boolean calculate(boolean a, boolean b) {
            return operation.apply(a, b);
        }
    }

    private record Calc(String inputA, String inputB, Operation op, String result) {

        public boolean calculate(Map<String, Boolean> inputs) {
            return op.calculate(inputs.get(inputA), inputs.get(inputB));
        }

        public Calc swap(String newResult) {
            return new Calc(inputA, inputB, op, newResult);
        }
    }
}
