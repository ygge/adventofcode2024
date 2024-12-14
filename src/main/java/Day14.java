import util.Pos;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 {

    private static final int MAX_X = 101;
    private static final int MAX_Y = 103;

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<String> input) {
        List<Robot> robots = readRobots(input);
        for (int i = 0; ; ++i) {
            robots = robots.stream()
                    .map(Robot::move)
                    .toList();
            if (isChristmasTree(robots)) {
                print(i, robots);
                return i + 1;
            }
        }
    }

    private static long part1(List<String> input) {
        List<Robot> robots = readRobots(input);
        for (int i = 0; i < 100; ++i) {
            robots = robots.stream()
                    .map(Robot::move)
                    .toList();
        }
        return count(robots,0, MAX_X/2, 0, MAX_Y/2)
                * count(robots, 0, MAX_X/2, MAX_Y/2 + 1, MAX_Y)
                * count(robots, MAX_X/2 + 1, MAX_X, 0, MAX_Y/2)
                * count(robots, MAX_X/2 + 1, MAX_X, MAX_Y/2 + 1, MAX_Y);
    }

    private static boolean isChristmasTree(List<Robot> robots) {
        for (int y = 0; y < MAX_Y; ++y) {
            final int yy = y;
            var onRow = robots.stream()
                    .map(r -> r.pos)
                    .filter(r -> r.y == yy)
                    .map(p -> p.x)
                    .sorted()
                    .toList();
            if (onRow.size() < 20) {
                continue;
            }
            boolean seen = true;
            for (int x = 1; x < 15; ++x) {
                if (onRow.get(x - 1) != onRow.get(x) - 1) {
                    seen = false;
                    break;
                }
            }
            if (seen) {
                return true;
            }
        }
        return false;
    }

    private static void print(int turn, List<Robot> robots) {
        char[][] board = new char[103][101];
        for (char[] value : board) {
            Arrays.fill(value, '.');
        }
        for (Robot robot : robots) {
            board[robot.pos.y][robot.pos.x] = '#';
        }
        System.out.printf("%nAfter %d seconds%n", turn);
        for (char[] chars : board) {
            System.out.println(new String(chars));
        }
    }

    private static List<Robot> readRobots(List<String> input) {
        List<Robot> robots = new ArrayList<>();
        for (String row : input) {
            var s = row.split(" ");
            var p = s[0].substring(2).split(",");
            var v = s[1].substring(2).split(",");
            robots.add(new Robot(
                    new Pos(Integer.parseInt(p[0]), Integer.parseInt(p[1])),
                    new Pos(Integer.parseInt(v[0]), Integer.parseInt(v[1]))
            ));
        }
        return robots;
    }

    private static long count(List<Robot> robots, int sx, int ex, int sy, int ey) {
        return robots.stream()
                .filter(r -> r.inside(sx, sy, ex, ey))
                .count();
    }

    private record Robot (Pos pos, Pos vel) {
        Robot move() {
            var x = pos.x + vel.x;
            var y = pos.y + vel.y;
            if (x < 0) {
                x += Day14.MAX_X;
            }
            if (y < 0) {
                y += Day14.MAX_Y;
            }
            if (x >= Day14.MAX_X) {
                x -= Day14.MAX_X;
            }
            if (y >= Day14.MAX_Y) {
                y -= Day14.MAX_Y;
            }
            return new Robot(new Pos(x, y), vel);
        }

        public boolean inside(int sx, int sy, int ex, int ey) {
            return pos.x >= sx && pos.x < ex && pos.y >= sy && pos.y < ey;
        }
    }
}
