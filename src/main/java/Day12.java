import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day12 {

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(char[][] board) {
        var seen = new HashSet<Pos>();
        long cost = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                var pos = new Pos(x, y);
                if (!seen.contains(pos)) {
                    cost += calcCost2(board, pos, seen);
                }
            }
        }
        return cost;
    }

    private static long part1(char[][] board) {
        var seen = new HashSet<Pos>();
        long cost = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                var pos = new Pos(x, y);
                if (!seen.contains(pos)) {
                    cost += calcCost(board, pos, seen);
                }
            }
        }
        return cost;
    }

    private static long calcCost2(char[][] board, Pos pos, Set<Pos> seen) {
        var c = board[pos.y][pos.x];
        var current = new HashMap<Pos, List<Direction>>();
        var queue = new LinkedList<Pos>();
        queue.add(pos);
        long per = 0;
        while (!queue.isEmpty()) {
            var p = queue.poll();
            if (current.containsKey(p)) {
                continue;
            }
            var list = new ArrayList<Direction>();
            current.put(p, list);
            for (Direction dir : Direction.values()) {
                var pp = p.move(dir);
                if (pp.y >= 0 && pp.y < board.length && pp.x >= 0 && pp.x < board[pp.y].length
                        && board[pp.y][pp.x] == c && !current.containsKey(pp)) {
                    queue.add(pp);
                }
            }
        }
        var list = new ArrayList<>(current.keySet());
        list.sort((p1, p2) -> p1.y == p2.y ? p2.x - p1.x : p2.y - p1.y);
        for (Pos p : list) {
            for (Direction dir : Direction.values()) {
                var pp = p.move(dir);
                if (pp.y < 0 || pp.y >= board.length || pp.x < 0 || pp.x >= board[pp.y].length || board[pp.y][pp.x] != c) {
                    var left = dir.turnLeft();
                    var right = dir.turnRight();
                    var leftPos = p.move(left);
                    var rightPos = p.move(right);
                    if (!current.getOrDefault(leftPos, Collections.emptyList()).contains(dir)
                            && !current.getOrDefault(rightPos, Collections.emptyList()).contains(dir)) {
                        ++per;
                    }
                    current.get(p).add(dir);
                }
            }
        }
        seen.addAll(current.keySet());
        return current.size() * per;
    }

    private static long calcCost(char[][] board, Pos pos, Set<Pos> seen) {
        var c = board[pos.y][pos.x];
        var current = new HashSet<Pos>();
        var queue = new LinkedList<Pos>();
        queue.add(pos);
        long per = 0;
        while (!queue.isEmpty()) {
            var p = queue.poll();
            if (!current.add(p)) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                var pp = p.move(dir);
                if (pp.y < 0 || pp.y >= board.length || pp.x < 0 || pp.x >= board[pp.y].length) {
                    ++per;
                } else if (board[pp.y][pp.x] != c) {
                    ++per;
                } else if (!current.contains(pp)) {
                    queue.add(pp);
                }
            }
        }
        seen.addAll(current);
        return current.size() * per;
    }
}
