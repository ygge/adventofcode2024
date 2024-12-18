import util.Direction;
import util.Pos;
import util.Util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Day18 {

    public static final int BOARD_SIZE = 71;

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static String part2(List<String> input) {
        for (int i = 1000; ; ++i) {
            var board = readBoard(input, i);
            var queue = new LinkedList<Node>();
            queue.add(new Node(new Pos(0, 0), 0));
            var seen = new HashSet<Pos>();
            boolean found = false;
            while (!queue.isEmpty()) {
                var node = queue.poll();
                if (!seen.add(node.pos)) {
                    continue;
                }
                if (node.pos.equals(new Pos(BOARD_SIZE - 1, BOARD_SIZE - 1))) {
                    found = true;
                    break;
                }
                for (Direction dir : Direction.values()) {
                    var pos = node.pos.move(dir);
                    if (pos.y >= 0 && pos.y < BOARD_SIZE && pos.x >= 0 && pos.x < BOARD_SIZE && !board[pos.y][pos.x]) {
                        queue.add(new Node(pos, node.steps + 1));
                    }
                }
            }
            if (!found) {
                return input.get(i - 1);
            }
        }
    }

    private static long part1(List<String> input) {
        var board = readBoard(input, 1024);
        var queue = new LinkedList<Node>();
        queue.add(new Node(new Pos(0, 0), 0));
        var seen = new HashSet<Node>();
        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (!seen.add(node)) {
                continue;
            }
            if (node.pos.equals(new Pos(BOARD_SIZE - 1, BOARD_SIZE - 1))) {
                return node.steps;
            }
            for (Direction dir : Direction.values()) {
                var pos = node.pos.move(dir);
                if (pos.y >= 0 && pos.y < BOARD_SIZE && pos.x >= 0 && pos.x < BOARD_SIZE && !board[pos.y][pos.x]) {
                    queue.add(new Node(pos, node.steps + 1));
                }
            }
        }
        throw new IllegalStateException("Path not found");
    }

    private static boolean[][] readBoard(List<String> input, int num) {
        var board = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < num; ++i) {
            var s = input.get(i).split(",");
            board[Integer.parseInt(s[1])][Integer.parseInt(s[0])] = true;
        }
        return board;
    }

    public record Node(Pos pos, int steps) {
    }
}
