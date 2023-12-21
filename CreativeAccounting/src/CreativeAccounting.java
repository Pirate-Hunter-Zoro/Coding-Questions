import java.io.*;
import java.util.*;

public class CreativeAccounting {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * When accounting for the profit of a business, we can divide consecutive days
     * into fixed-sized segments and calculate each segment’s profit as the sum of
     * all its daily profits. For example, we could choose seven-day segments to do
     * our accounting in terms of weekly profit. We also have the flexibility of
     * choosing a segment’s starting day. For example, for weekly profit we can
     * start a week on a Sunday, Monday, or even Wednesday. Choosing different
     * segment starting days may sometimes change how the profit looks on the books,
     * making it more (or less) attractive to investors.
     * 
     * As an example, we can divide ten consecutive days of profit (or loss, which
     * we denote as negative profit) into three-day segments as such:
     * 3,2,-7|5,4,1|3,0,-3|5
     * 
     * This gives us four segments with profit -2,10,0,5. For the purpose of this
     * division, partial segments with fewer than the
     * fixed segment size are allowed at the beginning and at the end. We say a
     * segment is profitable if it has a strictly positive profit. In the above
     * example, only two out of the four segments are profitable.
     * 
     * If we try a different starting day, we can obtain:
     * 3,2|-7,5,4|1,3,0|-3,5
     * 
     * This gives us four segments with profit 5,2,4,2. All four segments are
     * profitable, which makes our business look much more
     * consistent.
     * 
     * You’re given a list of consecutive days of profit, as well as an integer
     * range. If we can choose any segment size within that range and any starting
     * day for our accounting, what is the minimum and maximum number of profitable
     * segments that we can have?
     * 
     * Link:
     * https://open.kattis.com/problems/creativeaccounting
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int[] nlh = Arrays.stream(reader.readLine().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        int n = nlh[0];
        int l = nlh[1];
        int h = nlh[2];

        int[] leftSums = new int[n];
        int[] rightSums = new int[n];
        for (int i=0; i<n; i++) {
            leftSums[i] = Integer.MIN_VALUE;
            rightSums[i] = Integer.MIN_VALUE;
        }

        int[] days = new int[n];
        for (int i = 0; i < n; i++) {
            days[i] = Integer.parseInt(reader.readLine());
        }

        // Create a table of sums
        // The row index (plus 1) corresponds with the segment length
        // The column index indicates the starting index for the segment sum the cell
        // corresponds to
        int[][] buildUpSums = new int[2][n]; // working our way up to the window sizes of interest
        buildUpSums[0] = days;
        int[][] keepTheseSums = new int[h - l + 1][n];
        if (l == 1) // then we'll never touch buildUpSums[1]
            keepTheseSums[0] = buildUpSums[0];
        // Calculate sums
        for (int segSize = 2; segSize <= h; segSize++) {
            if (segSize > l) { // not using the build-up anymore - we're using what we're gonna need later
                for (int start = 0; start < n - segSize + 1; start++) // take the segSize-1 sum value and add the next day into it for the segSize sum value
                    keepTheseSums[segSize - l][start] = keepTheseSums[segSize - l - 1][start] + days[start + (segSize - 1)];
            } else {
                // we're still using the build-up
                for (int start = 0; start < n - segSize + 1; start++) // still same concept
                    buildUpSums[1][start] = buildUpSums[0][start] + days[start + (segSize-1)];
                buildUpSums[0] = buildUpSums[1]; // increment for the next round
                if (segSize == l) // we just reached the minimum window size so keep track of that - we will not be using our pair of arrays anymore
                    keepTheseSums[0] = buildUpSums[1];
            }
        }

        // Was buildUpSums even touch at all?
        if ((h - l) < 1) {
            for (int start = 0; start < n - h + 1; start++) // still same concept
                buildUpSums[1][start] = buildUpSums[0][start] + days[start + (h-1)];
            keepTheseSums = buildUpSums;
        }

        int maxProfitableSegments = Integer.MIN_VALUE;
        int minProfitableSegments = Integer.MAX_VALUE;
        for (int segSize = l; segSize <= h; segSize++) {
            // there are segSize possible starting points for the first fully-sized interval
            // of days
            for (int start = 0; start < segSize; start++) {
                int numProfitableSegments = 0;

                if (start > 0) { // first (non-full) window
                    if (computeSumFromLeft(start-1, leftSums, days) > 0) // linear complexity
                        numProfitableSegments++;
                } 

                int lastWindowSize = (n - start) % segSize; // last (non-full) window
                if (lastWindowSize > 0) {
                    if (computeSumFromHere(days.length - lastWindowSize, rightSums, days) > 0) // linear complexity
                        numProfitableSegments++;
                }

                // now for the full windows
                for (int i = start; i < n - lastWindowSize; i += segSize) {
                    if (keepTheseSums[segSize-l][i] > 0)
                        numProfitableSegments++;
                }

                maxProfitableSegments = Math.max(maxProfitableSegments, numProfitableSegments);
                minProfitableSegments = Math.min(minProfitableSegments, numProfitableSegments);
            }
        }

        System.out.println(minProfitableSegments + " " + maxProfitableSegments);

    }

    /**
     * Helper method to compute the sum of values from the start to end in days
     * @param start
     * @param rightSums
     * @param days
     * @return int
     */
    private static int computeSumFromHere(int start, int[] rightSums, int[] days) {
        if (rightSums[start] != Integer.MIN_VALUE)
            return rightSums[start];

        if (start == days.length-1)
            rightSums[start] = days[start];
        else
            rightSums[start] = days[start] + computeSumFromHere(start+1, rightSums, days);
        
        return rightSums[start];
    }

    /**
     * Helper method to compute the sum of values from 0 to the end in days
     * @param end
     * @param leftSums
     * @param days
     * @return int
     */
    private static int computeSumFromLeft(int end, int[] leftSums, int[] days) {
        if (leftSums[end] != Integer.MIN_VALUE)
            return leftSums[end];

        if (end == 0)
            leftSums[end] = days[end];
        else
            leftSums[end] = days[end] + computeSumFromLeft(end-1, leftSums, days);
        
        return leftSums[end];
    }

}
