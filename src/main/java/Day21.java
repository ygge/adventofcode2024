import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day21 {

    private static final Map<Character, Pos> NUM_MAP = new HashMap<>();
    private static final Map<Character, Pos> DIR_MAP = new HashMap<>();
    private static final Pos START_NUM = new Pos(2, 3);
    private static final Pos START_DIR = new Pos(2, 0);

    static {
        NUM_MAP.put('7', new Pos(0, 0));
        NUM_MAP.put('8', new Pos(1, 0));
        NUM_MAP.put('9', new Pos(2, 0));
        NUM_MAP.put('4', new Pos(0, 1));
        NUM_MAP.put('5', new Pos(1, 1));
        NUM_MAP.put('6', new Pos(2, 1));
        NUM_MAP.put('1', new Pos(0, 2));
        NUM_MAP.put('2', new Pos(1, 2));
        NUM_MAP.put('3', new Pos(2, 2));
        NUM_MAP.put('0', new Pos(1, 3));
        NUM_MAP.put('A', new Pos(2, 3));

        DIR_MAP.put('^', new Pos(1, 0));
        DIR_MAP.put('A', new Pos(2, 0));
        DIR_MAP.put('<', new Pos(0, 1));
        DIR_MAP.put('v', new Pos(1, 1));
        DIR_MAP.put('>', new Pos(2, 1));
    }

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        long sum = 0;
        for (String code : input) {
            var len = calc(code, 25);
            sum += len * Long.parseLong(code.substring(0, code.length() - 1));
        }
        return sum;
    }

    private static long part1(List<String> input) {
        long sum = 0;
        for (String code : input) {
            var len = calc(code, 2);
            sum += len * Long.parseLong(code.substring(0, code.length() - 1));
        }
        return sum;
    }

    private static long calc(String code, int limit) {
        var list = new ArrayList<Character>();
        for (int i = 0; i < code.length(); i++) {
            list.add(code.charAt(i));
        }
        var map = new HashMap<Node, List<List<Character>>>();
        var depthMap = new HashMap<Node2, Long>();
        return calc(list, 0, limit, map, depthMap);
    }

    private static long calc(List<Character> code,
                             int depth,
                             int limit,
                             Map<Node, List<List<Character>>> map,
                             HashMap<Node2, Long> depthMap) {
        var node2 = new Node2(code, depth);
        if (depthMap.containsKey(node2)) {
            return depthMap.get(node2);
        }
        long moves = 0;
        var startPos = depth == 0 ? START_NUM : START_DIR;
        var pad = depth == 0 ? NUM_MAP : DIR_MAP;
        for (char c : code) {
            var goalPos = pad.get(c);
            List<List<Character>> alts;
            if (depth > 0) {
                alts = map.get(new Node(startPos, goalPos));
                if (alts == null) {
                    alts = calcMove(startPos, goalPos, pad);
                    map.put(new Node(startPos, goalPos), alts);
                }
            } else {
                alts = calcMove(startPos, goalPos, pad);
            }
            if (depth == limit) {
                var ans = alts.stream().min(Comparator.comparingInt(List::size)).orElseThrow();
                moves += ans.size();
            } else {
                Long best = null;
                for (List<Character> alt : alts) {
                    var recAlt = calc(alt, depth + 1, limit, map, depthMap);
                    if (best == null || recAlt < best) {
                        best = recAlt;
                    }
                }
                if (best == null) {
                    throw new IllegalStateException();
                }
                moves += best;
            }
            startPos = goalPos;
        }
        depthMap.put(node2, moves);
        return moves;
    }

    private static List<List<Character>> calcMove(Pos start, Pos goalPos, Map<Character, Pos> keyPad) {
        var list = new ArrayList<List<Character>>();
        calcMove(start, goalPos, keyPad, new ArrayList<>(), list);
        return list;
    }

    private static void calcMove(Pos start,
                                 Pos goalPos,
                                 Map<Character, Pos> keyPad,
                                 List<Character> soFar,
                                 List<List<Character>> total) {
        var moves = new ArrayList<>(soFar);
        if (goalPos.equals(start)) {
            moves.add('A');
            total.add(moves);
            return;
        }
        var dist = start.dist(goalPos);
        for (Direction dir : Direction.values()) {
            var newPos = start.move(dir);
            if (newPos.dist(goalPos) < dist && keyPad.containsValue(newPos)) {
                var path = new ArrayList<>(soFar);
                path.add(dir.c);
                calcMove(newPos, goalPos, keyPad, path, total);
            }
        }
    }

    private record Node(Pos start, Pos end) {
    }

    private record Node2(List<Character> code, int depth) {
    }
}
