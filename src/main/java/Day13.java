import util.Util;

import java.util.List;

public class Day13 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        long ans = 0;
        for (int i = 0; i < input.size(); ++i) {
            var a = input.get(i++);
            var b = input.get(i++);
            var p = input.get(i++);
            var spiltA = a.substring(10).split(", ");
            int ax = Integer.parseInt(spiltA[0].substring(2));
            int ay = Integer.parseInt(spiltA[1].substring(2));
            var spiltB = b.substring(10).split(", ");
            int bx = Integer.parseInt(spiltB[0].substring(2));
            int by = Integer.parseInt(spiltB[1].substring(2));
            var splitP = p.substring(7).split(", ");
            var px = Integer.parseInt(splitP[0].substring(2));
            var py = Integer.parseInt(splitP[1].substring(2));
            return cost(ax, ay, bx, by, 10000000000000L + px, 10000000000000L + py);
        }
        return ans;
    }

    private static long part1(List<String> input) {
        long ans = 0;
        for (int i = 0; i < input.size(); ++i) {
            var a = input.get(i++);
            var b = input.get(i++);
            var p = input.get(i++);
            var spiltA = a.substring(10).split(", ");
            int ax = Integer.parseInt(spiltA[0].substring(2));
            int ay = Integer.parseInt(spiltA[1].substring(2));
            var spiltB = b.substring(10).split(", ");
            int bx = Integer.parseInt(spiltB[0].substring(2));
            int by = Integer.parseInt(spiltB[1].substring(2));
            var splitP = p.substring(7).split(", ");
            var px = Integer.parseInt(splitP[0].substring(2));
            var py = Integer.parseInt(splitP[1].substring(2));
            return cost(ax, ay, bx, by, px, py);
        }
        return ans;
    }

    private static long cost(long x1, long y1, long x2, long y2, long px, long py) {
        long b = (py * x1 - px * y1) / (x1 * y2 - x2 * y1);
        long a = (px - b * x2) / x1;
        if (a * x1 + b * x2 == px && a * y1 + b * y2 == py) {
            return a * 3 + b;
        }
        return 0;
    }
}
