import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 {

    public static void main(String[] args) {
        var input = Util.readLongs();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<Long> input) {
        var total = new HashMap<String, Integer>();
        for (Long num : input) {
            var prices = new HashMap<String, Integer>();
            String diffString = "";
            var n = num;
            int p = (int)(num%10);
            for (int i = 0; i < 2000; ++i) {
                var x = ((n*64) ^ n) % 16777216;
                x = ((x/32) ^ x) % 16777216;
                n = ((x*2048) ^ x) % 16777216;
                int pp = (int)(n%10);
                int diff = pp-p;
                if (i > 0) {
                    diffString += ",";
                }
                diffString += diff;
                if (i >= 3) {
                    if (i > 3) {
                        diffString = diffString.substring(diffString.indexOf(",") + 1);
                    }
                    prices.putIfAbsent(diffString, pp);
                }
                p = pp;
            }
            for (Map.Entry<String, Integer> entry : prices.entrySet()) {
                total.put(entry.getKey(), total.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        int max = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : total.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        return total.values().stream()
                .mapToInt(i -> i)
                .max()
                .orElseThrow();
    }

    private static long part1(List<Long> input) {
        long sum = 0;
        for (Long num : input) {
            var n = num;
            for (int i = 0; i < 2000; ++i) {
                var x = ((n*64) ^ n) % 16777216;
                x = ((x/32) ^ x) % 16777216;
                n = ((x*2048) ^ x) % 16777216;
            }
            sum += n;
        }
        return sum;
    }
}
