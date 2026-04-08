package s4a;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main {

    public static void solve(InputStream inputStream, OutputStream outputStream) throws IOException {
        FastScanner scanner = new FastScanner(inputStream);
        FastOutput output = new FastOutput(outputStream);

        int numberOfRoutes = scanner.nextInt();
        int queryCount = scanner.nextInt();

        short[] routeCapacity = new short[numberOfRoutes + 1];
        long[] routeOffset = new long[numberOfRoutes + 1];

        for (int i = 1; i <= numberOfRoutes; i++) {
            routeCapacity[i] = (short) scanner.nextInt();
        }

        BinaryIndexedTree offsetTree = new BinaryIndexedTree(numberOfRoutes);
        BinaryIndexedTree capacityTree = new BinaryIndexedTree(numberOfRoutes);
        capacityTree.buildFrom(routeCapacity, numberOfRoutes);

        for (int qi = 0; qi < queryCount; qi++) {
            char command = scanner.nextCommand();

            switch (command) {
                case 'P' -> {
                    int route = scanner.nextInt();
                    short newCapacity = (short) scanner.nextInt();
                    long day = scanner.nextLong();

                    long deltaOffset = ((long) routeCapacity[route] - newCapacity) * day;
                    offsetTree.add(route, deltaOffset);
                    capacityTree.add(route, newCapacity - routeCapacity[route]);

                    routeOffset[route] += deltaOffset;
                    routeCapacity[route] = newCapacity;
                }

                case 'C' -> {
                    int route = scanner.nextInt();
                    // Drugi parametr w operacji 'C' jest czescia formatu wejscia,
                    // ale nie jest potrzebny do aktualizacji stanu trasy.
                    scanner.nextLong();

                    offsetTree.add(route, -routeOffset[route]);
                    capacityTree.add(route, -routeCapacity[route]);

                    routeOffset[route] = 0;
                    routeCapacity[route] = 0;
                }

                case 'A' -> {
                    int route = scanner.nextInt();
                    short newCapacity = (short) scanner.nextInt();
                    long day = scanner.nextLong();

                    long newOffset = -(long) newCapacity * day;
                    offsetTree.add(route, newOffset - routeOffset[route]);
                    capacityTree.add(route, newCapacity - routeCapacity[route]);

                    routeOffset[route] = newOffset;
                    routeCapacity[route] = newCapacity;
                }

                case 'Q' -> {
                    int from = scanner.nextInt();
                    int to = scanner.nextInt();
                    long day = scanner.nextLong();

                    long answer = offsetTree.rangeSum(from, to) + day * capacityTree.rangeSum(from, to);
                    output.writeLong(answer);
                    output.writeNewLine();
                }

                default -> throw new IOException("Unknown command: " + command);
            }
        }

        output.flush();
    }

    public static void main(String[] args) throws IOException {
        solve(System.in, System.out);
    }
}
