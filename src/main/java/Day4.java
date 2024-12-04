import util.Util;

public class Day4 {

    public static void main(String[] args) {
        var input = Util.readBoard();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(char[][] board) {
        int ans = 0;
        for (int y = 1; y < board.length - 1; y++) {
            for (int x = 1; x < board[y].length - 1; x++) {
                if (board[y][x] == 'A') {
                    char a1 = board[y-1][x-1];
                    char a2 = board[y+1][x+1];
                    char b1 = board[y-1][x+1];
                    char b2 = board[y+1][x-1];
                    if (((a1 == 'M' && a2 == 'S') || (a1 == 'S' && a2 == 'M'))
                            && ((b1 == 'M' && b2 == 'S') || (b1 == 'S' && b2 == 'M'))) {
                        ++ans;
                    }
                }
            }
        }
        return ans;
    }

    private static int part1(char[][] board) {
        int ans = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 'X') {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (isXmas(board, x, y, dx, dy)) {
                                ++ans;
                            }
                        }
                    }
                }
            }
        }
        return ans;
    }

    private static boolean isXmas(char[][] board, int x, int y, int dx, int dy) {
        var w = new char[]{'X', 'M', 'A', 'S'};
        for (int i = 0; i < w.length; i++) {
            int xx = x + dx * i;
            int yy = y + dy * i;
            if (yy == -1 || yy == board.length || xx == -1 || xx == board[0].length) {
                return false;
            }
            if (board[yy][xx] != w[i]) {
                return false;
            }
        }
        return true;
    }
}
