import util.Util;

import java.util.Arrays;
import java.util.List;

public class Day7 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        long ans = 0;
        for (String row : input) {
            var s = row.split(": ");
            long goal = Long.parseLong(s[0]);
            var num = Arrays.stream(s[1].split(" "))
                    .map(Long::parseLong)
                    .toList();
            if (canMake(goal, num, num.get(0), 1, true)) {
                ans += goal;
            }
        }
        return ans;
    }

    private static long part1(List<String> input) {
        long ans = 0;
        for (String row : input) {
            var s = row.split(": ");
            long goal = Long.parseLong(s[0]);
            var num = Arrays.stream(s[1].split(" "))
                    .map(Long::parseLong)
                    .toList();
            if (canMake(goal, num, num.get(0), 1, false)) {
                ans += goal;
            }
        }
        return ans;
    }

    private static boolean canMake(long goal, List<Long> num, long soFar, int index, boolean concat) {
        if (index == num.size()) {
            return soFar == goal;
        }
        if (soFar > goal) {
            return false;
        }
        return canMake(goal, num, soFar + num.get(index), index + 1, concat)
                || canMake(goal, num, soFar * num.get(index), index + 1, concat)
                || (concat && canMake(goal, num, concat(soFar, num.get(index)), index + 1, concat));
    }

    private static long concat(long a, long b) {
        int len = Long.toString(b).length();
        while (len > 0) {
            a *= 10;
            --len;
        }
        return a + b;
    }
}
