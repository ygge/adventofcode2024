import util.Direction;
import util.Pos;
import util.Util;

import java.util.HashSet;
import java.util.LinkedList;

public class Day10 {

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(char[][] board) {
        int sum = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == '0') {
                    sum += score(board, new Pos(x, y), false);
                }
            }
        }
        return sum;
    }

    private static int part1(char[][] board) {
        int sum = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == '0') {
                    sum += score(board, new Pos(x, y), true);
                }
            }
        }
        return sum;
    }

    private static int score(char[][] board, Pos pos, boolean distinct) {
        var queue = new LinkedList<Pos>();
        var goals = new HashSet<Pos>();
        int count = 0;
        queue.add(pos);
        while (!queue.isEmpty()) {
            var p = queue.pop();
            if (board[p.y][p.x] == '9') {
                goals.add(p);
                ++count;
            } else {
                for (Direction dir : Direction.values()) {
                    var p2 = p.move(dir);
                    if (p2.y >= 0 && p2.y < board.length && p2.x >= 0 && p2.x < board[p2.y].length
                            && board[p2.y][p2.x] == board[p.y][p.x] + 1) {
                        queue.add(p2);
                    }
                }
            }
        }
        return distinct ? goals.size() : count;
    }
}
