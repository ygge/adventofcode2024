import util.Util;

import java.util.*;

public class Day11 {

    public static void main(String[] args) {
        var input = Util.readString();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(String input) {
        var list = parseList(input);
        Map<Long, Long> counts = new HashMap<>();
        for (Long num : list) {
            counts.put(num, counts.getOrDefault(num, 0L) + 1);
        }
        for (int i = 0; i < 75; ++i) {
            counts = blink(counts);
        }
        return counts.values().stream()
                .reduce(0L, Long::sum);
    }

    private static long part1(String input) {
        var list = parseList(input);
        for (int i = 0; i < 25; ++i) {
            list = blink(list);
        }
        return list.size();
    }

    private static Map<Long, Long> blink(Map<Long, Long> counts) {
        var newCounts = new HashMap<Long, Long>();
        for (Map.Entry<Long, Long> entry : counts.entrySet()) {
            if (entry.getKey() == 0) {
                newCounts.put(1L, newCounts.getOrDefault(1L, 0L) + entry.getValue());
            } else if (entry.getKey().toString().length() % 2 == 0) {
                var s = entry.getKey().toString();
                long key1 = Long.parseLong(s.substring(0, s.length() / 2));
                newCounts.put(key1, newCounts.getOrDefault(key1, 0L) + entry.getValue());
                long key2 = Long.parseLong(s.substring(s.length()/2));
                newCounts.put(key2, newCounts.getOrDefault(key2, 0L) + entry.getValue());
            } else {
                var key = entry.getKey() * 2024;
                newCounts.put(key, newCounts.getOrDefault(key, 0L) + entry.getValue());
            }
        }
        return newCounts;
    }

    private static List<Long> parseList(String input) {
        return Arrays.stream(input.split(" "))
                .map(Long::parseLong)
                .toList();
    }

    private static List<Long> blink(List<Long> list) {
        var newList = new ArrayList<Long>();
        for (Long i : list) {
            if (i == 0) {
                newList.add(1L);
            } else if (i.toString().length() % 2 == 0) {
                var s = i.toString();
                newList.add(Long.parseLong(s.substring(0, s.length()/2)));
                newList.add(Long.parseLong(s.substring(s.length()/2)));
            } else {
                newList.add(i * 2024);
            }
        }
        return newList;
    }
}
