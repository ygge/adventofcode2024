import util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<String> input) {
        var left = new ArrayList<Integer>();
        var right = new HashMap<Integer, Integer>();
        for (String row : input) {
            var r = row.split(" +");
            left.add(Integer.parseInt(r[0]));
            int v = Integer.parseInt(r[1]);
            right.put(v, right.getOrDefault(v, 0) + 1);
        }
        int ans = 0;
        for (int i = 0; i < left.size(); i++) {
            ans += left.get(i) * right.getOrDefault(left.get(i), 0);
        }
        return ans;
    }

    private static int part1(List<String> input) {
        var left = new ArrayList<Integer>();
        var right = new ArrayList<Integer>();
        for (String row : input) {
            var r = row.split(" +");
            left.add(Integer.parseInt(r[0]));
            right.add(Integer.parseInt(r[1]));
        }
        Collections.sort(left);
        Collections.sort(right);
        int ans = 0;
        for (int i = 0; i < left.size(); i++) {
            ans += Math.abs(left.get(i) - right.get(i));
        }
        return ans;
    }
}
