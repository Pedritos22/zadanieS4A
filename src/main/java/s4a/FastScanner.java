package s4a;

import java.io.IOException;
import java.io.InputStream;

final class FastScanner {
    private static final int BUFFER_SIZE = 1 << 16;

    private final InputStream input;
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private int pointer = 0;
    private int bytesRead = 0;

    FastScanner(InputStream input) {
        this.input = input;
    }

    private int read() throws IOException {
        if (pointer >= bytesRead) {
            bytesRead = input.read(buffer);
            pointer = 0;
            if (bytesRead <= 0) {
                return -1;
            }
        }
        return buffer[pointer++];
    }

    private int skipWhitespace() throws IOException {
        int c;
        do {
            c = read();
        } while (c <= ' ' && c != -1);
        return c;
    }

    int nextInt() throws IOException {
        int c = skipWhitespace();
        int sign = 1;

        if (c == '-') {
            sign = -1;
            c = read();
        }

        int value = 0;
        while (c > ' ') {
            value = value * 10 + (c - '0');
            c = read();
        }
        return value * sign;
    }

    long nextLong() throws IOException {
        int c = skipWhitespace();
        long sign = 1;

        if (c == '-') {
            sign = -1;
            c = read();
        }

        long value = 0;
        while (c > ' ') {
            value = value * 10L + (c - '0');
            c = read();
        }
        return value * sign;
    }

    char nextCommand() throws IOException {
        return (char) skipWhitespace();
    }
}
