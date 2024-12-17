import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Day17 {

    public static void main(String[] args) {
        Util.verifySubmission();
        var input = Util.readStrings();
        //Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        int b = Integer.parseInt(input.get(1).substring("Register B: ".length()));
        int c = Integer.parseInt(input.get(2).substring("Register C: ".length()));
        var program = Arrays.stream(input.get(4).substring("Program: ".length()).split(","))
                .map(Integer::parseInt)
                .toList();

        long v = 0;
        for (int len = program.size() - 1; len >= 0; --len) {
            for (int a = 0; a < 8; ++a) {
                long value = v + a;
                if (calc(value, b, c, program, program.subList(len, program.size()))) {
                    v = value * 8;
                    break;
                }
            }
        }
        System.out.println(v);
        System.out.println(v / 8);

        for (int i = 1; ; ++i) {
            var test = calc(175657589193542L - i, b, c, program);
            if (test) {
                System.out.println("Found: " + (175657589193542L - i));
                break;
            }
        }
        var queue = new PriorityQueue<Node>();
        queue.add(new Node(1, 0));
        Long min = null;
        while (!queue.isEmpty()) {
            var node = queue.poll();
            for (int aa = 0; aa < 8; ++aa) {
                long value = node.a + aa;
                if (runOnce(value, 0, 0, program) == program.get(program.size() - node.len)) {
                    if (calc(value, b, c, program)) {
                        return value;
                    }
                    if (node.len == program.size()) {
                        if (min == null || min > value) {
                            min = value;
                        }
                    } else {
                        queue.add(new Node(node.len + 1, value * 8));
                    }
                }
            }
        }
        return min;
        // 175657589193542
        // 175657589193542
        // 1405260713548336
    }

    private static int runOnce(long a, long b, long c, List<Integer> program) {
        int index = 0;
        while (index < program.size()) {
            var ins = program.get(index);
            if (ins == 0) {
                long op = combo(a, b, c, program.get(index + 1));
                a >>= op;
                if (a < 0) {
                    throw new IllegalArgumentException("a=" + a);
                }
                index += 2;
            } else if (ins == 1) {
                b ^= program.get(index + 1);
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 2) {
                b = combo(a, b, c, program.get(index + 1)) % 8;
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 3) {
                if (a == 0) {
                    index += 2;
                } else {
                    index = program.get(index + 1);
                }
            } else if (ins == 4) {
                b ^= c;
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 5) {
                return (int)combo(a, b, c, program.get(index + 1)) % 8;
            } else if (ins == 6) {
                long op = combo(a, b, c, program.get(index + 1));
                b >>= op;
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 7) {
                long op = combo(a, b, c, program.get(index + 1));
                c >>= op;
                if (c < 0) {
                    throw new IllegalArgumentException("c=" + c);
                }
                index += 2;
            }
        }
        throw new IllegalStateException();
    }

    private static boolean calc(long a, long b, long c, List<Integer> program) {
        return calc(a, b, c, program, program);
    }

    private static boolean calc(long a, long b, long c, List<Integer> program, List<Integer> goal) {
        int index = 0;
        var out = new ArrayList<Integer>();
        int outIndex = 0;
        while (index < program.size()) {
            var ins = program.get(index);
            if (ins == 0) {
                long op = combo(a, b, c, program.get(index + 1));
                a >>= op;
                if (a < 0) {
                    throw new IllegalArgumentException("a=" + a);
                }
                index += 2;
            } else if (ins == 1) {
                b ^= program.get(index + 1);
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 2) {
                b = combo(a, b, c, program.get(index + 1)) % 8;
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 3) {
                if (a == 0) {
                    index += 2;
                } else {
                    index = program.get(index + 1);
                }
            } else if (ins == 4) {
                b ^= c;
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 5) {
                int value = (int)(combo(a, b, c, program.get(index + 1))) % 8;
                if (value != goal.get(outIndex++)) {
                    return false;
                }
                out.add(value);
                index += 2;
            } else if (ins == 6) {
                long op = combo(a, b, c, program.get(index + 1));
                b >>= op;
                if (b < 0) {
                    throw new IllegalArgumentException("b=" + b);
                }
                index += 2;
            } else if (ins == 7) {
                long op = combo(a, b, c, program.get(index + 1));
                c >>= op;
                if (c < 0) {
                    throw new IllegalArgumentException("c=" + c);
                }
                index += 2;
            }
        }
        return out.equals(goal);
    }

    private static String part1(List<String> input) {
        int a = Integer.parseInt(input.get(0).substring("Register A: ".length()));
        int b = Integer.parseInt(input.get(1).substring("Register B: ".length()));
        int c = Integer.parseInt(input.get(2).substring("Register C: ".length()));
        int index = 0;
        var program = Arrays.stream(input.get(4).substring("Program: ".length()).split(","))
                .map(Integer::parseInt)
                .toList();
        var out = new ArrayList<Integer>();
        while (index < program.size()) {
            var ins = program.get(index);
            if (ins == 0) {
                int op = (int)combo(a, b, c, program.get(index + 1));
                a /= (int)Math.pow(2, op);
                index += 2;
            } else if (ins == 1) {
                b ^= program.get(index + 1);
                index += 2;
            } else if (ins == 2) {
                b = (int)combo(a, b, c, program.get(index + 1)) % 8;
                index += 2;
            } else if (ins == 3) {
                if (a == 0) {
                    index += 2;
                } else {
                    index = program.get(index + 1);
                }
            } else if (ins == 4) {
                b ^= c;
                index += 2;
            } else if (ins == 5) {
                out.add((int)combo(a, b, c, program.get(index + 1)) % 8);
                index += 2;
            } else if (ins == 6) {
                int op = (int)combo(a, b, c, program.get(index + 1));
                b = a / (int)Math.pow(2, op);
                index += 2;
            } else if (ins == 7) {
                int op = (int)combo(a, b, c, program.get(index + 1));
                c = a / (int)Math.pow(2, op);
                index += 2;
            }
        }
        return String.join(",", out.stream().map(i -> Integer.toString(i)).toList());
    }

    private static long combo(long a, long b, long c, int value) {
        if (value <= 3) {
            return value;
        } else if (value == 4) {
            return a;
        } else if (value == 5) {
            return b;
        } else if (value == 6) {
            return c;
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

    private record Node(int len, long a) implements Comparable<Node> {
        @Override
        public int compareTo(Node o) {
            return Long.compare(a, o.a);
        }
    }
}
