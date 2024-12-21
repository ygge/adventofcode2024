import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day20 {

    private static final int SAVED_TURNS = 100;

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(char[][] board) {
        var pos = findStart(board);
        var goal = findEnd(board);
        var path = runRace(board, pos, goal);
        long count = 0;
        Map<Integer, Integer> saved = new HashMap<>();
        for (int i = 0; i < path.size() - 1; i++) {
            for (int j = i + SAVED_TURNS; j < path.size(); ++j) {
                int len = path.get(i).dist(path.get(j));
                if (len <= 20 && j - i - len >= SAVED_TURNS) {
                    ++count;
                }
            }
        }
        return count;
    }

    private static long part1(char[][] board) {
        var pos = findStart(board);
        var goal = findEnd(board);
        var turns = runRace(board, pos, goal).size();
        int count = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == '#') {
                    board[y][x] = '.';
                    var t = runRace(board, pos, goal).size();
                    if (t <= turns - SAVED_TURNS) {
                        ++count;
                    }
                    board[y][x] = '#';
                }
            }
        }
        return count;
    }

    private static int findCheat(char[][] board, Pos start, Pos goal) {
        var seen = new HashSet<Pos>();
        var queue = new LinkedList<Node>();
        for (Direction dir : Direction.values()) {
            queue.add(new Node(start.move(dir), 1, null));
        }
        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (!seen.add(node.pos)) {
                continue;
            }
            if (board[node.pos.y][node.pos.x] == '#') {
                for (Direction dir : Direction.values()) {
                    Pos newPos = node.pos.move(dir);
                    if (newPos.y >= 0 && newPos.y < board.length
                            && newPos.x >= 0 && newPos.x < board[newPos.y].length) {
                        if (newPos.equals(goal)) {
                            return node.turns + 1;
                        } else if (board[newPos.y][newPos.x] == '#') {
                            queue.add(new Node(newPos, node.turns + 1, node));
                        }
                    }
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    private static List<Pos> runRace(char[][] board, Pos pos, Pos goal) {
        var seen = new HashSet<Pos>();
        var queue = new LinkedList<Node>();
        queue.add(new Node(pos, 0, null));
        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (!seen.add(node.pos)) {
                continue;
            }
            if (node.pos.equals(goal)) {
                var list = new ArrayList<Pos>();
                createPath(list, node);
                return list;
            }
            if (board[node.pos.y][node.pos.x] == '.') {
                for (Direction dir : Direction.values()) {
                    Pos newPos = node.pos.move(dir);
                    if (newPos.y >= 0 && newPos.y < board.length
                            && newPos.x >= 0 && newPos.x < board[newPos.y].length
                            && board[newPos.y][newPos.x] == '.') {
                        queue.add(new Node(newPos, node.turns + 1, node));
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    private static void createPath(List<Pos> list, Node node) {
        if (node.prev != null) {
            createPath(list, node.prev);
        }
        list.add(node.pos);
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

    private record Node(Pos pos, int turns, Node prev) {
    }
}
