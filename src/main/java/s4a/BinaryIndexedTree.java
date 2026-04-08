package s4a;

final class BinaryIndexedTree {
    private final long[] tree;

    BinaryIndexedTree(int size) {
        this.tree = new long[size + 1];
    }

    void add(int index, long delta) {
        while (index < tree.length) {
            tree[index] += delta;
            index += index & -index;
        }
    }

    long prefixSum(int index) {
        long sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= index & -index;
        }
        return sum;
    }

    long rangeSum(int from, int to) {
        return prefixSum(to) - prefixSum(from - 1);
    }

    void buildFrom(short[] values, int size) {
        for (int i = 1; i <= size; i++) {
            tree[i] = values[i];
        }

        for (int i = 1; i <= size; i++) {
            int parent = i + (i & -i);
            if (parent <= size) {
                tree[parent] += tree[i];
            }
        }
    }
}
