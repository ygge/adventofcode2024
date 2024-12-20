import util.Util;

import java.util.Arrays;
import java.util.List;

public class Day19 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        var patterns = Arrays.stream(input.get(0).split(","))
                .map(String::trim)
                .toList();
        long count = 0;
        for (int i = 2; i < input.size(); i++) {
            count += count(patterns, input.get(i));
        }
        return count;
    }

    private static long part1(List<String> input) {
        var patterns = Arrays.stream(input.get(0).split(","))
                .map(String::trim)
                .toList();
        int count = 0;
        for (int i = 2; i < input.size(); i++) {
            if (canMake(patterns, input.get(i))) {
                ++count;
            }
        }
        return count;
    }

    private static long count(List<String> patterns, String design) {
        long[] count = new long[design.length() + 1];
        count[0] = 1;
        for (int i = 0; i < count.length; ++i) {
            if (count[i] > 0) {
                for (String pattern : patterns) {
                    if (pattern.length() + i <= design.length() && design.startsWith(pattern, i)) {
                        count[i + pattern.length()] += count[i];
                    }
                }
            }
        }
        return count[design.length()];
    }

    private static boolean canMake(List<String> patterns, String design) {
        boolean[] seen = new boolean[design.length() + 1];
        seen[0] = true;
        for (int i = 0; i < seen.length; ++i) {
            if (seen[i]) {
                for (String pattern : patterns) {
                    if (pattern.length() + i <= design.length() && design.startsWith(pattern, i)) {
                        seen[i + pattern.length()] = true;
                    }
                }
            }
        }
        return seen[design.length()];
    }
}
