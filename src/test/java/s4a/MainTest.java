package s4a;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

class MainTest {
    private String runSolver(String input) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Main.solve(in, out);
        return out.toString().trim();
    }

    private void assertEqual(String input, String expected) throws Exception {
        assertEquals(expected.trim(), runSolver(input));
    }

    @Test
    void testExample1() throws Exception {
        assertEqual(
                "5 7\n" +
                        "1 2 3 2 4\n" +
                        "Q 1 5 2\n" +
                        "Q 2 3 2\n" +
                        "C 2 3\n" +
                        "P 3 5 3\n" +
                        "Q 2 4 4\n" +
                        "A 2 5 6\n" +
                        "Q 1 5 8\n",
                "24\n10\n22\n100"
        );
    }

    @Test
    void testExample2() throws Exception {
        assertEqual(
                "1 7\n" +
                        "2\n" +
                        "Q 1 1 1\n" +
                        "C 1 1\n" +
                        "A 1 6 2\n" +
                        "Q 1 1 3\n" +
                        "Q 1 1 4\n" +
                        "Q 1 1 7\n" +
                        "Q 1 1 8\n",
                "2\n6\n12\n30\n36"
        );
    }

    @Test
    void testSingleRouteNoOperations() throws Exception {
        assertEqual(
                "1 1\n" +
                        "5\n" +
                        "Q 1 1 10\n",
                "50"
        );
    }

    @Test
    void testQueryAtTimeZero() throws Exception {
        assertEqual(
                "3 1\n" +
                        "10 20 30\n" +
                        "Q 1 3 0\n",
                "0"
        );
    }

    @Test
    void testZeroCapacity() throws Exception {
        assertEqual(
                "2 1\n" +
                        "0 0\n" +
                        "Q 1 2 100\n",
                "0"
        );
    }

    @Test
    void testMultipleCapacityChanges() throws Exception {
        assertEqual(
                "1 3\n" +
                        "3\n" +
                        "P 1 7 2\n" +
                        "Q 1 1 5\n" +
                        "Q 1 1 2\n",
                "27\n6"
        );
    }

    @Test
    void testCancelAndReassign() throws Exception {
        assertEqual(
                "1 3\n" +
                        "4\n" +
                        "C 1 3\n" +
                        "A 1 10 5\n" +
                        "Q 1 1 8\n",
                "30"
        );
    }

    @Test
    void testCancelledRouteReturnsZero() throws Exception {
        assertEqual(
                "2 3\n" +
                        "5 5\n" +
                        "C 1 1\n" +
                        "Q 1 2 10\n" +
                        "Q 2 2 10\n",
                "50\n50"
        );
    }

    @Test
    void testSubrangeQuery() throws Exception {
        assertEqual(
                "5 2\n" +
                        "1 2 3 4 5\n" +
                        "Q 2 4 6\n" +
                        "Q 3 3 6\n",
                "54\n18"
        );
    }

    @Test
    void testLargeTimeValues() throws Exception {
        assertEqual(
                "1 1\n" +
                        "1000\n" +
                        "Q 1 1 100000000000\n",
                "100000000000000"
        );
    }

    @Test
    void testAssignAfterCancelNewHistory() throws Exception {
        assertEqual(
                "1 3\n" +
                        "100\n" +
                        "C 1 5\n" +
                        "A 1 1 5\n" +
                        "Q 1 1 10\n",
                "5"
        );
    }

    @Test
    void testInterleavedOperations() throws Exception {
        assertEqual(
                "2 5\n" +
                        "2 3\n" +
                        "Q 1 2 1\n" +
                        "P 1 5 2\n" +
                        "Q 1 2 4\n" +
                        "C 2 4\n" +
                        "Q 1 2 6\n",
                "5\n26\n24"
        );
    }

    @Test
    void testManyRoutesSingleQuery() throws Exception {
        assertEqual(
                "4 1\n" +
                        "1 1 1 1\n" +
                        "Q 1 4 3\n",
                "12"
        );
    }

    @Test
    void testCapacityChangeToZero() throws Exception {
        assertEqual(
                "1 2\n" +
                        "10\n" +
                        "P 1 0 5\n" +
                        "Q 1 1 10\n",
                "50"
        );
    }
}
