import java.io.*;
import java.util.*;

public class CrammingForFinals {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * https://open.kattis.com/problems/crammingforfinals
     * 
     * Help link:
     * https://latenitecoding.github.io/cramming-for-finals/
     * 
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws IOException {

        int[] rcdn = nextIntArray();
        int rows = rcdn[0];
        int cols = rcdn[1];
        int distance = rcdn[2];
        int numStudents = rcdn[3];

        int[][] places = new int[numStudents][2];
        for (int i = 0; i < numStudents; i++) {
            places[i] = nextIntArray();
        }

        long[] studentTableNumbers = new long[numStudents];
        for (int i = 0; i < places.length; i++) {
            studentTableNumbers[i] = findTableNumber(places[i], cols);
        }
        Arrays.sort(studentTableNumbers);
        // top to bottom, left to right

        // go row by row, and for each column, find the noise level, and keep track of the minimum
        // how many places can hear this student?
        // cos(arc,sin(h/d)) * d = b --> anything within 7 sig figs so add 0.0000001
        // 2*b + 1 is full interval length, and we know our column
        // lhs, rhs, that interval is one ADDITIONAL noise level ( + 1)
        // Negative noise level means student is sitting there currently
        // ONLY look at spots in an interval
        // sort intervals by their left hand sides
        // iterate over the intervals, 
        //      each time you hit lhs, noiseLevel++
        //      each time you hit rhs, noiseLevel--
        //      jump from one edge to the next edge (lhs, lhs, rhs, lhs, rhs, rhs)
        //      HAVE we changed positions? Then we can update and check
        //      Use a record/object
        // Keep track of where students are sitting... HOW???
        // 3 arrays? Students, leftHandSides, rightHandSides - sort them all
        // lhs smallest, add 1, rhs smallest, subtract 1, 
        // lhs - 2 2 3 (inclusive - just in interval)
        // rhs - 3 3 4 (exclusive - just after interval ends)
        // students - 2 4
        // noiseLevel: 0,1,2,STUDENT, 3, 2, 1, STUDENT
        // 10 ^ 3, 2*2500, 2*2500 --> 25 * 10 ^ 9 which works for 4 second problem

    }

    /**
     * Helper method to find the table number (1-ordered) of a particular student
     * @param place
     * @param rowLength
     * @return long
     */
    static long findTableNumber(int[] place, int rowLength) {
        long r = place[0];
        long c = place[1];

        return (r - 1l) * rowLength + (c - 1l) + 1l;
    }

    /**
     * Given a table number, return the row number (1-ordered)
     * @param tableNum
     * @param rowLength
     * @return 
     */
    static int getRow(long tableNum, int rowLength) {
        return (int)((tableNum - 1) / rowLength) + 1;
    }

    /**
     * Given table number, return the column number (1-ordered)
     * @param tableNum
     * @param rowLength
     * @return
     */
    static int getCol(long tableNum, int rowLength) {
        return (int)((tableNum - 1) % rowLength) + 1;
    }
}
