import util.Util;

import java.util.ArrayList;

public class Day9 {

    public static void main(String[] args) {
        var input = Util.readString();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(String input) {
        var mem = new ArrayList<Memory>();
        for (int i = 0; i < input.length(); i++) {
            mem.add(new Memory(input.charAt(i) - '0', i%2 == 1, i/2));
        }
        for (int i = mem.size() - 1; i > 0; i--) {
            var block = mem.get(i);
            for (int j = 0; j < i; j++) {
                if (mem.get(j).free && mem.get(j).size >= block.size) {
                    var free = mem.get(j);
                    mem.remove(i);
                    mem.add(i, new Memory(block.size, true, block.id));
                    mem.remove(j);
                    mem.add(j, block);
                    if (free.size > block.size) {
                        mem.add(j + 1, new Memory(free.size - block.size, true, free.id));
                        ++i;
                    }
                    boolean compacted = true;
                    while (compacted) {
                        compacted = false;
                        for (int k = 1; k < mem.size(); ++k) {
                            if (mem.get(k).free && mem.get(k-1).free) {
                                mem.add(k-1, new Memory(mem.get(k-1).size + mem.get(k).size, true, mem.get(k).id));
                                mem.remove(k + 1);
                                mem.remove(k);
                                if (k < i) {
                                    --i;
                                }
                                compacted = true;
                            }
                        }
                    }
                    break;
                }
            }
        }
        long ans = 0;
        long index = 0;
        for (Memory memory : mem) {
            if (memory.free) {
                index += memory.size;
            } else {
                for (int i = 0; i < memory.size; i++) {
                    ans += (index++) * memory.id;
                }
            }
        }
        return ans;
    }

    private static long part1(String input) {
        long ans = 0;
        long startId = 0;
        long endId = input.length() / 2 + 1;
        int dataIndex = -1;
        var startIndex = 0;
        int endIndex = input.length() + 1;
        int left = 0;
        while (startIndex < endIndex) {
            var c = input.charAt(startIndex);
            for (int i = 0; i < (c - '0'); ++i) {
                ans += (++dataIndex) * startId;
                System.out.println(dataIndex + " " + startId + " " + ans);
            }
            ++startId;
            c = input.charAt(++startIndex);
            for (int i = 0; i < (c - '0'); ++i) {
                if (left == 0) {
                    endIndex -= 2;
                    if (endIndex <= startIndex) {
                        break;
                    }
                    --endId;
                    left = input.charAt(endIndex) - '0';
                    --i;
                    continue;
                }
                ans += (++dataIndex) * endId;
                System.out.println(dataIndex + " " + endId + " " + ans);
                --left;
            }
            ++startIndex;
        }
        while (left > 0) {
            ans += (++dataIndex) * endId;
            --left;
        }
        return ans;
    }

    private record Memory(int size, boolean free, int id) {}
}
