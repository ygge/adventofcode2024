import util.Util;

import java.util.*;

public class Day23 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static String part2(List<String> input) {
        var conn = parseConnections(input);
        List<String> list = new ArrayList<>();
        for (String comp : conn.keySet()) {
            var lan = calcConnected(comp, conn);
            if (lan.size() > list.size()) {
                list = lan;
            }
        }
        var sorted = list.stream().sorted().toList();
        return String.join(",", sorted);
    }

    private static long part1(List<String> input) {
        var conn = parseConnections(input);
        var seen = new HashSet<Set<String>>();
        for (Map.Entry<String, Set<String>> entry : conn.entrySet()) {
            if (entry.getKey().startsWith("t")) {
                var list = new ArrayList<>(entry.getValue());
                for (int i = 0; i < list.size(); i++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        if (conn.get(list.get(i)).contains(list.get(j))) {
                            seen.add(new HashSet<>(Arrays.asList(entry.getKey(), list.get(i), list.get(j))));
                        }
                    }
                }
            }
        }
        return seen.size();
    }

    private static List<String> calcConnected(String comp, Map<String, Set<String>> conn) {
        var best = new HashSet<String>();
        for (String other : conn.get(comp)) {
            var seen = new HashSet<String>();
            seen.add(comp);
            seen.add(other);
            for (String o2 : conn.get(comp)) {
                if (!seen.contains(o2)) {
                    if (conn.get(o2).containsAll(seen)) {
                        seen.add(o2);
                    }
                }
            }
            if (seen.size() > best.size()) {
                best = seen;
            }
        }
        return best.stream().toList();
    }

    private static HashMap<String, Set<String>> parseConnections(List<String> input) {
        var conn = new HashMap<String, Set<String>>();
        for (String row : input) {
            var s = row.split("-");
            conn.putIfAbsent(s[0], new HashSet<>());
            conn.putIfAbsent(s[1], new HashSet<>());
            conn.get(s[0]).add(s[1]);
            conn.get(s[1]).add(s[0]);
        }
        return conn;
    }
}
