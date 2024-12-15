import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day15 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        int index = 0;
        for (; index < input.size(); index++) {
            if (input.get(index).isBlank()) {
                break;
            }
        }
        var board = new char[index][];
        var robot = new Pos(-1, -1);
        for (int y = 0; y < index; ++y) {
            board[y] = new char[input.get(y).length() * 2];
            for (int x = 0; x < input.get(y).length(); ++x) {
                board[y][x * 2] = input.get(y).charAt(x);
                if (board[y][x * 2] == '.') {
                    board[y][x * 2 + 1] = '.';
                } else if (board[y][x * 2] == '#') {
                    board[y][x * 2 + 1] = '#';
                } else if (board[y][x * 2] == '@') {
                    board[y][x * 2] = '.';
                    board[y][x * 2 + 1] = '.';
                    robot = new Pos(x * 2, y);
                } else if (board[y][x * 2] == 'O') {
                    board[y][x * 2] = '[';
                    board[y][x * 2 + 1] = ']';
                }
            }
        }
        if (robot.x == -1) {
            throw new IllegalStateException("Robot not found");
        }
        var moves = new ArrayList<Direction>();
        for (int i = index + 1; i < input.size(); ++i) {
            for (int j = 0; j < input.get(i).length(); ++j) {
                moves.add(parseDir(input.get(i).charAt(j)));
            }
        }
        int move = 0;
        for (Direction dir : moves) {
            if (move == 4458) {
                System.out.println("problem!");
            }
            var r = robot.move(dir);
            if (board[r.y][r.x] == '.') {
                robot = r;
            } else if (board[r.y][r.x] == '[' || board[r.y][r.x] == ']') {
                if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                    while (board[r.y][r.x] == '[' || board[r.y][r.x] == ']') {
                        r = r.move(dir);
                    }
                    if (board[r.y][r.x] == '.') {
                        char c = '.';
                        robot = robot.move(dir);
                        r = robot;
                        while (board[r.y][r.x] != '.') {
                            char cc = board[r.y][r.x];
                            board[r.y][r.x] = c;
                            c = cc;
                            r = r.move(dir);
                        }
                        board[r.y][r.x] = c;
                    }
                } else {
                    var xList = new HashSet<Integer>();
                    xList.add(r.x);
                    if (board[r.y][r.x] == '[') {
                        xList.add(r.x + 1);
                    } else {
                        xList.add(r.x - 1);
                    }
                    boolean walls = false;
                    while (true) {
                        r = r.move(dir);
                        var newXList = new HashSet<Integer>();
                        for (int x : xList) {
                            if (board[r.y][x] == '[') {
                                newXList.add(x);
                                newXList.add(x + 1);
                            } else if (board[r.y][x] == ']') {
                                newXList.add(x);
                                newXList.add(x - 1);
                            } else if (board[r.y][x] == '#') {
                                walls = true;
                            }
                        }
                        if (walls || newXList.isEmpty()) {
                            break;
                        }
                        xList = newXList;
                    }
                    if (!walls) {
                        robot = robot.move(dir);
                        r = robot;
                        var boxes = new HashMap<Integer, Character>();
                        boxes.put(r.x, board[r.y][r.x]);
                        if (board[r.y][r.x] == '[') {
                            boxes.put(r.x + 1, board[r.y][r.x + 1]);
                            board[r.y][r.x] = '.';
                            board[r.y][r.x + 1] = '.';
                        } else {
                            boxes.put(r.x - 1, board[r.y][r.x - 1]);
                            board[r.y][r.x] = '.';
                            board[r.y][r.x - 1] = '.';
                        }
                        while (true) {
                            r = r.move(dir);
                            var newBoxes = new HashMap<Integer, Character>();
                            var seen = new HashSet<Integer>();
                            for (Map.Entry<Integer, Character> entry : boxes.entrySet()) {
                                int x = entry.getKey();
                                if (!seen.add(x)) {
                                    continue;
                                }
                                if (board[r.y][x] == '[') {
                                    newBoxes.put(x, board[r.y][x]);
                                    newBoxes.put(x + 1, board[r.y][x + 1]);
                                    board[r.y][x + 1] = boxes.getOrDefault(x + 1, '.');
                                    seen.add(x + 1);
                                } else if (board[r.y][x] == ']') {
                                    newBoxes.put(x, board[r.y][x]);
                                    newBoxes.put(x - 1, board[r.y][x - 1]);
                                    board[r.y][x - 1] = boxes.getOrDefault(x - 1, '.');
                                    seen.add(x - 1);
                                }
                                board[r.y][x] = entry.getValue();
                            }
                            if (newBoxes.isEmpty()) {
                                break;
                            }
                            boxes = newBoxes;
                        }
                    }
                }
            }
        }
        long sum = 0;
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == '[') {
                    sum += y * 100L + x;
                }
            }
        }
        return sum;
    }

    private static long part1(List<String> input) {
        int index = 0;
        for (; index < input.size(); index++) {
            if (input.get(index).isBlank()) {
                break;
            }
        }
        var board = new char[index][];
        var robot = new Pos(-1, -1);
        for (int y = 0; y < index; ++y) {
            board[y] = input.get(y).toCharArray();
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == '@') {
                    robot = new Pos(x, y);
                    board[y][x] = '.';
                }
            }
        }
        if (robot.x == -1) {
            throw new IllegalStateException("Robot not found");
        }
        var moves = new ArrayList<Direction>();
        for (int i = index + 1; i < input.size(); ++i) {
            for (int j = 0; j < input.get(i).length(); ++j) {
                moves.add(parseDir(input.get(i).charAt(j)));
            }
        }
        for (Direction dir : moves) {
            var r = robot.move(dir);
            if (board[r.y][r.x] == '.') {
                robot = r;
            } else if (board[r.y][r.x] == 'O') {
                while (board[r.y][r.x] == 'O') {
                    r = r.move(dir);
                }
                if (board[r.y][r.x] == '.') {
                    robot = robot.move(dir);
                    board[robot.y][robot.x] = '.';
                    board[r.y][r.x] = 'O';
                }
            }
        }
        long sum = 0;
        for (int y = 0; y < board.length; ++y) {
            System.out.println(new String(board[y]));
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == 'O') {
                    sum += y * 100L + x;
                }
            }
        }
        return sum;
    }

    private static Direction parseDir(char c) {
        return switch (c) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DOWN;
            case '<' -> Direction.LEFT;
            case '>' -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("" + c);
        };
    }
}
