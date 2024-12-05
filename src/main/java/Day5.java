import util.Util;

import java.util.*;

public class Day5 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<String> input) {
        var before = new HashMap<Integer, List<Integer>>();
        var after = new HashMap<Integer, List<Integer>>();
        int index = parseBeforeAndAfter(input, before, after);
        int ans = 0;
        for (++index; index < input.size(); index++) {
            var row = input.get(index);
            var numbers = Arrays.stream(row.split(","))
                    .map(Integer::parseInt)
                    .toList();
            if (!rightOrder(numbers, before, after)) {
                var sortedNumbers = sort(numbers, before, after);
                ans += sortedNumbers.get(numbers.size() / 2);
            }
        }
        return ans;
    }

    private static int part1(List<String> input) {
        var before = new HashMap<Integer, List<Integer>>();
        var after = new HashMap<Integer, List<Integer>>();
        int index = parseBeforeAndAfter(input, before, after);
        int ans = 0;
        for (++index; index < input.size(); index++) {
            var row = input.get(index);
            var numbers = Arrays.stream(row.split(","))
                    .map(Integer::parseInt)
                    .toList();
            if (rightOrder(numbers, before, after)) {
                ans += numbers.get(numbers.size() / 2);
            }
        }
        return ans;
    }

    private static int parseBeforeAndAfter(List<String> input, HashMap<Integer, List<Integer>> before, HashMap<Integer, List<Integer>> after) {
        int index = 0;
        for (; index < input.size(); index++) {
            var row = input.get(index);
            if (row.isEmpty()) {
                break;
            }
            var split = row.split("\\|");
            var a = Integer.parseInt(split[0]);
            var b = Integer.parseInt(split[1]);
            before.putIfAbsent(b, new ArrayList<>());
            after.putIfAbsent(a, new ArrayList<>());
            before.get(b).add(a);
            after.get(a).add(b);
        }
        return index;
    }

    private static List<Integer> sort(List<Integer> numbers,
                                      Map<Integer, List<Integer>> before,
                                      Map<Integer, List<Integer>> after) {
        var sorted = new ArrayList<Integer>();
        for (int num : numbers) {
            var placed = false;
            for (int i = 0; i < sorted.size(); i++) {
                if (okToPlace(num, i, sorted, before, after)) {
                    sorted.add(i, num);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                sorted.add(num);
            }
        }
        return sorted;
    }

    private static boolean okToPlace(int num,
                                     int index,
                                     List<Integer> numbers,
                                     Map<Integer, List<Integer>> before,
                                     Map<Integer, List<Integer>> after) {
        for (int j = 0; j < index; ++j) {
            if (after.getOrDefault(num, Collections.emptyList()).contains(numbers.get(j))) {
                return false;
            }
        }
        for (int j = index; j < numbers.size(); ++j) {
            if (before.getOrDefault(num, Collections.emptyList()).contains(numbers.get(j))) {
                return false;
            }
        }
        return true;
    }

    private static boolean rightOrder(List<Integer> numbers,
                                      Map<Integer, List<Integer>> before,
                                      Map<Integer, List<Integer>> after) {
        for (int i = 0; i < numbers.size(); i++) {
            var num = numbers.get(i);
            for (int j = 0; j < i; ++j) {
                if (after.getOrDefault(num, Collections.emptyList()).contains(numbers.get(j))) {
                    return false;
                }
            }
            for (int j = i+1; j < numbers.size(); ++j) {
                if (before.getOrDefault(num, Collections.emptyList()).contains(numbers.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
