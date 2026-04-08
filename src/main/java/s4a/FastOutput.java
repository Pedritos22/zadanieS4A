package s4a;

import java.io.IOException;
import java.io.OutputStream;

final class FastOutput {
    private static final int BUFFER_SIZE = 1 << 16;

    private final OutputStream output;
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private final byte[] digits = new byte[20];
    private int pointer = 0;

    FastOutput(OutputStream output) {
        this.output = output;
    }

    private void writeByte(int value) throws IOException {
        if (pointer == BUFFER_SIZE) {
            flush();
        }
        buffer[pointer++] = (byte) value;
    }

    void writeLong(long value) throws IOException {
        if (value == 0) {
            writeByte('0');
            return;
        }

        if (value < 0) {
            writeByte('-');
            value = -value;
        }

        int length = 0;
        while (value > 0) {
            digits[length++] = (byte) ('0' + (value % 10));
            value /= 10;
        }

        for (int i = length - 1; i >= 0; i--) {
            writeByte(digits[i]);
        }
    }

    void writeNewLine() throws IOException {
        writeByte('\n');
    }

    void flush() throws IOException {
        output.write(buffer, 0, pointer);
        output.flush();
        pointer = 0;
    }
}
