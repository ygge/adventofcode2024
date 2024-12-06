import util.Direction;
import util.Pos;
import util.Util;

import java.util.HashSet;

public class Day6 {

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(char[][] board) {
        var state = getInitialState(board);
        int count = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == '.') {
                    board[y][x] = '#';
                    if (isLoop(board, state)) {
                        count++;
                    }
                    board[y][x] = '.';
                }
            }
        }
        return count;
    }

    private static int part1(char[][] board) {
        var state = getInitialState(board);
        var seen = new HashSet<Pos>();
        seen.add(state.pos);
        while (true) {
            var newPos = state.pos.move(state.dir);
            if (newPos.y == -1 || newPos.y == board.length || newPos.x == -1 || newPos.x == board[0].length) {
                break;
            }
            if (board[newPos.y][newPos.x] == '#') {
                state = new State(state.pos, state.dir.turnRight());
            } else {
                seen.add(newPos);
                state = new State(newPos, state.dir);
            }
        }
        return seen.size();
    }

    private static State getInitialState(char[][] board) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] != '#' && board[y][x] != '.') {
                    var pos = new Pos(x, y);
                    Direction dir;
                    if (board[y][x] == '^') {
                        dir = Direction.UP;
                    } else if (board[y][x] == '>') {
                        dir = Direction.RIGHT;
                    } else if (board[y][x] == 'v') {
                        dir = Direction.DOWN;
                    } else if (board[y][x] == '<') {
                        dir = Direction.LEFT;
                    } else {
                        throw new IllegalStateException("Unexpected value: " + board[y][x]);
                    }
                    return new State(pos, dir);
                }
            }
        }
        throw new IllegalStateException("Could not determine initial state");
    }

    private static boolean isLoop(char[][] board, State state) {
        var seen = new HashSet<State>();
        seen.add(state);
        while (true) {
            var newPos = state.pos.move(state.dir);
            if (newPos.y == -1 || newPos.y == board.length || newPos.x == -1 || newPos.x == board[0].length) {
                return false;
            }
            if (board[newPos.y][newPos.x] == '#') {
                state = new State(state.pos, state.dir.turnRight());
            } else {
                state = new State(newPos, state.dir);
            }
            if (!seen.add(state)) {
                return true;
            }
        }
    }

    private record State(Pos pos, Direction dir) {}
}
