import util.Pos;
import util.Util;

import java.util.*;

public class Day8 {

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(char[][] board) {
        Map<Character, List<Pos>> frequencies = mapFrequencies(board);
        int ans = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (isAntiNode2(new Pos(x, y), frequencies)) {
                    ++ans;
                }
            }
        }
        return ans;
    }

    private static int part1(char[][] board) {
        Map<Character, List<Pos>> frequencies = mapFrequencies(board);
        int ans = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (isAntiNode(new Pos(x, y), frequencies)) {
                    ++ans;
                }
            }
        }
        return ans;
    }

    private static Map<Character, List<Pos>> mapFrequencies(char[][] board) {
        Map<Character, List<Pos>> frequencies = new HashMap<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                char c = board[y][x];
                if (c != '.') {
                    if (!frequencies.containsKey(c)) {
                        frequencies.put(c, new ArrayList<>());
                    }
                    frequencies.get(c).add(new Pos(x, y));
                }
            }
        }
        return frequencies;
    }

    private static boolean isAntiNode2(Pos pos, Map<Character, List<Pos>> frequencies) {
        for (List<Pos> value : frequencies.values()) {
            for (int i = 0; i < value.size() - 1; i++) {
                var p1 = value.get(i);
                var d1 = p1.dist(pos);
                for (int j = i + 1; j < value.size(); j++) {
                    var p2 = value.get(j);
                    var d2 = p2.dist(pos);
                    if (inLine2(pos, p1, p2) && divides(p1, p2, d1, d2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean inLine2(Pos p1, Pos p2, Pos p3) {
        int dx = p3.x - p2.x;
        int dy = p3.y - p2.y;
        for (int i = 0; i < 100; ++i) {
            if (p1.x + (i * dx) == p2.x && p1.y + (i * dy) == p2.y) {
                return true;
            }
        }
        dx = p2.x - p3.x;
        dy = p2.y - p3.y;
        for (int i = 0; i < 100; ++i) {
            if (p1.x + (i * dx) == p2.x && p1.y + (i * dy) == p2.y) {
                return true;
            }
        }
        return false;
    }

    private static boolean divides(Pos p1, Pos p2, int d1, int d2) {
        var dist = p1.dist(p2);
        if (d1 == 0 || d2 == 0) {
            return true;
        }
        return d1 % dist == 0 || d2 % dist == 0;
    }

    private static boolean isAntiNode(Pos pos, Map<Character, List<Pos>> frequencies) {
        for (List<Pos> value : frequencies.values()) {
            for (int i = 0; i < value.size() - 1; i++) {
                var p1 = value.get(i);
                var d1 = p1.dist(pos);
                for (int j = i + 1; j < value.size(); j++) {
                    var p2 = value.get(j);
                    var d2 = p2.dist(pos);
                    if (inLine(pos, p1, p2) && (d1 * 2 == d2 || d2 * 2 == d1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean inLine(Pos p1, Pos p2, Pos p3) {
        var list = new ArrayList<>(List.of(p1, p2, p3));
        list.sort(Comparator.comparingInt(pp -> pp.x));
        var dx = list.get(2).x - list.get(1).x;
        if (dx != list.get(1).x - list.get(0).x) {
            return false;
        }
        list.sort(Comparator.comparingInt(pp -> pp.y));
        var dy = list.get(2).y - list.get(1).y;
        return dy == list.get(1).y - list.get(0).y;
    }
}
