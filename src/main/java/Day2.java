import util.Util;

import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<String> input) {
        int ans = 0;
        for (String line : input) {
            String[] split = line.split(" +");
            for (int i = 0; i < split.length; i++) {
                String[] s = new String[split.length - 1];
                int index = 0;
                for (int j = 0; j < split.length; j++) {
                    if (j != i) {
                        s[index] = split[j];
                        ++index;
                    }
                }
                if (isSafe(s)) {
                    ++ans;
                    break;
                }
            }
        }
        return ans;
    }

    private static int part1(List<String> input) {
        int ans = 0;
        for (String line : input) {
            String[] split = line.split(" +");
            boolean safe = isSafe(split);
            if (safe) {
                ++ans;
            }
        }
        return ans;
    }

    private static boolean isSafe(String[] split) {
        int last = 0;
        boolean safe = true;
        boolean inc = true;
        for (int i = 0; i < split.length; i++) {
            var v = Integer.parseInt(split[i]);
            if (i == 1) {
                inc = v > last;
            }
            if (i != 0) {
                int diff = Math.abs(v - last);
                if (diff == 0 || diff > 3) {
                    safe = false;
                    break;
                }
                boolean incc = v > last;
                if (inc != incc) {
                    safe = false;
                    break;
                }
            }
            last = v;
        }
        return safe;
    }
}
