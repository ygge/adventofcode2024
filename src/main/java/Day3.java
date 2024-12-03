import util.Util;

import java.util.List;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<String> input) {
        int ans = 0;
        var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");
        var enabled = true;
        for (String row : input) {
            var matcher = pattern.matcher(row);
            while (matcher.find()) {
                var value = matcher.group();
                if (value.equals("do()")) {
                    enabled = true;
                } else if (value.equals("don't()")) {
                    enabled = false;
                } else if (enabled) {
                    var val1 = Integer.parseInt(matcher.group(1));
                    var val2 = Integer.parseInt(matcher.group(2));
                    ans += val1 * val2;
                }
            }
        }
        return ans;
    }

    private static int part1(List<String> input) {
        int ans = 0;
        var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        for (String row : input) {
            var matcher = pattern.matcher(row);
            while (matcher.find()) {
                var val1 = Integer.parseInt(matcher.group(1));
                var val2 = Integer.parseInt(matcher.group(2));
                ans += val1 * val2;
            }
        }
        return ans;
    }
}
