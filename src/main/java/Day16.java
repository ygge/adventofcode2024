import util.Direction;
import util.Pos;
import util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Day16 {

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(char[][] board) {
        var pos = findStart(board);
        var goal = findEnd(board);
        Integer best = null;
        var seen = new HashMap<Seen, Integer>();
        var path = new HashSet<Pos>();
        var queue = new PriorityQueue<Node>();
        queue.add(new Node(pos, Direction.RIGHT, 0, null));
        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (best != null && best < node.score) {
                continue;
            }
            var s = new Seen(node.pos, node.dir);
            if (seen.containsKey(s) && seen.get(s) < node.score) {
                continue;
            }
            seen.put(s, node.score);
            if (node.pos.equals(goal)) {
                best = node.score;
                var n = node;
                while (n != null) {
                    path.add(n.pos);
                    n = n.parent;
                }
            }
            if (board[node.pos.y][node.pos.x] == '.') {
                queue.add(new Node(node.pos.move(node.dir), node.dir, node.score + 1, node));
                queue.add(new Node(node.pos, node.dir.turnRight(), node.score + 1000, node));
                queue.add(new Node(node.pos, node.dir.turnLeft(), node.score + 1000, node));
            }
        }
        return path.size();
    }

    private static long part1(char[][] board) {
        var pos = findStart(board);
        var goal = findEnd(board);
        var seen = new HashSet<Seen>();
        var queue = new PriorityQueue<Node>();
        queue.add(new Node(pos, Direction.RIGHT, 0, null));
        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (!seen.add(new Seen(node.pos, node.dir))) {
                continue;
            }
            if (node.pos.equals(goal)) {
                return node.score;
            }
            if (board[node.pos.y][node.pos.x] == '.') {
                queue.add(new Node(node.pos.move(node.dir), node.dir, node.score + 1, node));
                queue.add(new Node(node.pos, node.dir.turnRight(), node.score + 1000, node));
                queue.add(new Node(node.pos, node.dir.turnLeft(), node.score + 1000, node));
            }
        }
        throw new RuntimeException("Part 1 failed");
    }

    private static Pos findStart(char[][] board) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 'S') {
                    board[y][x] = '.';
                    return new Pos(x, y);
                }
            }
        }
        throw new IllegalArgumentException("No start found");
    }

    private static Pos findEnd(char[][] board) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 'E') {
                    board[y][x] = '.';
                    return new Pos(x, y);
                }
            }
        }
        throw new IllegalArgumentException("No end found");
    }

    private record Node(Pos pos, Direction dir, int score, Node parent) implements Comparable<Node> {

        @Override
        public int compareTo(Node o) {
            return score - o.score;
        }
    }

    private record Seen(Pos pos, Direction dir) {
    }
}
