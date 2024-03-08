import java.io.*;
import java.util.*;

public class OnCallTeamTooSlow {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().trim());
    }

    static int[] nextIntArraySpaces() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int[] nextIntArrayNoSpaces() throws IOException {
        return Arrays.stream(reader.readLine().split("")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * Link:
     * https://open.kattis.com/problems/oncallteam
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws IOException {

        int[] nm = nextIntArraySpaces();
        int numEngineers = nm[0];
        int numServices = nm[1];
        int[][] engineerCapabilities = new int[numEngineers][numServices];
        for (int i = 0; i < numEngineers; i++) {
            engineerCapabilities[i] = nextIntArrayNoSpaces();
        }

        int[] engineerBitstrings = new int[numEngineers];
        for (int i = 0; i < numEngineers; i++) {
            for (int j = 0; j < numServices; j++) {
                engineerBitstrings[i] += engineerCapabilities[i][numServices - 1 - j] << j;
            }
        }

        int[][][] serviceSelections = new int[numServices][numServices + 1][];
        int[][] nChooseK = new int[21][21];
        int best = 1;
        for (int k = 1; k <= numServices; k++) {
            int[] theseServiceSelections = findBinaryStrings(numServices - 1, k, serviceSelections, nChooseK);
            boolean kCovered = true;

            for (int serviceSelection : theseServiceSelections) {
                int current = serviceSelection;
                int numEngineersCoverAService = 0;
                // The code snippet you provided is checking if the current service selection is
                // covering all the services.
                for (int engineerBitString : engineerBitstrings) {
                    numEngineersCoverAService += ((engineerBitString & current) > 0) ? 1 : 0;
                }
                // Hall's Theorem
                if (numEngineersCoverAService < k) {
                    kCovered = false;
                    break;
                }
            }

            if (!kCovered)
                break;
            else
                best = k;
        }

        System.out.println(best);
    }

    /**
     * Find all the binary strings using numOnes ones where the farthest left index
     * allowed to be 1 is highestOneIndex
     * 
     * @param highestOneIndex
     * @param numOnes
     * @param serviceSelections
     * @param nChooseK
     * @return int[]
     */
    static int[] findBinaryStrings(int highestOneIndex, int numOnes, int[][][] serviceSelections, int[][] nChooseK) {
        if (serviceSelections[highestOneIndex][numOnes] == null) {
            // We have some work to do
            int[] binaryStrings = new int[choose(highestOneIndex + 1, numOnes, nChooseK)];

            // Find all such binary strings
            if (numOnes == highestOneIndex + 1) // Base case: choose(n,n) = 1
                binaryStrings[0] = (int) Math.pow(2, numOnes) - 1;
            else if (numOnes == 0) // Base case: choose(n,0) = 1
                binaryStrings[0] = 0;
            else {
                int idx = 0;

                int[] leftMostZero = findBinaryStrings(highestOneIndex - 1, numOnes, serviceSelections, nChooseK);
                for (int prev : leftMostZero) {
                    binaryStrings[idx] = prev;
                    idx++;
                }

                int[] leftMostOne = findBinaryStrings(highestOneIndex - 1, numOnes - 1, serviceSelections, nChooseK);
                int farLeftOne = (int) Math.pow(2, highestOneIndex);
                for (int prev : leftMostOne) {
                    binaryStrings[idx] = farLeftOne + prev;
                    idx++;
                }
            }

            // Store our list of numbers
            serviceSelections[highestOneIndex][numOnes] = binaryStrings;
        }
        return serviceSelections[highestOneIndex][numOnes];
    }

    /**
     * Calculate n choose k
     * 
     * @param n
     * @param k
     * @param nChooseK
     * @return int
     */
    static int choose(int n, int k, int[][] nChooseK) {
        if (nChooseK[n][k] == 0) {
            if (k == 0 || k == n) {
                nChooseK[n][k] = 1;
            } else {
                // we have work to do
                nChooseK[n][k] = choose(n - 1, k, nChooseK) + choose(n - 1, k - 1, nChooseK);
            }
        }
        return nChooseK[n][k];
    }
}
