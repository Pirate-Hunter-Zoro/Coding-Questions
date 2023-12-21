import java.io.*;
import java.util.*;

public class RestaurantOpening {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * It is said that the three most important factors for determining whether or
     * not a business will be successful are location, location, and location. The
     * Incredible Cooks Preparing Cuisine are opening a new restaurant in the
     * International City Promoting Cooking, and they have hired you to find the
     * optimal location for their restaurant.
     * 
     * You decide to model the city as a grid, with each grid square having a
     * specified number of people living in it. The distance between two grid
     * squares (r_1,c_1) and (r_2,c_2) is |r_1 - r_1|+|c_1 - c_2|. In order to visit
     * the restaurant, each potential customer
     * would incur a cost equal to the minimum distance from the grid square in
     * which they live to the grid square containing the proposed location of the
     * restaurant. The total cost for a given restaurant location is defined as the
     * sum of the costs of everyone living in the city to visit the restaurant.
     * 
     * Given the current city layout, compute the minimum total cost if the
     * Incredible Cooks Preparing Cuisine select their next restaurant location
     * optimally.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws IOException {
        // Read in grid length and width
        int[] nm = Arrays.stream(reader.readLine().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        int n = nm[0];
        int m = nm[1];

        // Create the grid
        int[][] populationGrid = new int[n][m];
        for (int i = 0; i < n; i++) {
            populationGrid[i] = Arrays.stream(reader.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        // Structure to solve algorithm
        int[][] sums = new int[n + 1][m + 1];
        // Row sums
        int totalSum = 0;
        for (int r = 1; r <= n; r++) {
            int rowSum = 0;
            for (int c = 1; c <= m; c++) {
                rowSum += populationGrid[r - 1][c - 1];
            }
            sums[r][0] = rowSum;
            totalSum += rowSum;
        }
        sums[0][0] = totalSum;
        // Column sums
        for (int c = 1; c <= m; c++) {
            int colSum = 0;
            for (int r = 1; r <= n; r++) {
                colSum += populationGrid[r - 1][c - 1];
            }
            sums[0][c] = colSum;
        }

        // Calculate the minimum cost via brute force at the top left cell
        int minCost = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                minCost += populationGrid[i][j] * (i + j);
        }
        sums[1][1] = minCost;

        // Compute the solutions row by row
        int topSum = 0;
        for (int r = 1; r <= n; r++) {
            int leftSum = 0;
            // compute the first tile in this row
            if (r != 1) {
                topSum += sums[r-1][0];
                sums[r][1] = sums[r-1][1] - totalSum + 2 * topSum;
                minCost = Math.min(minCost, sums[r][1]);
            }
            // compute the second tile in this row
            for (int c = 2; c <= m; c++) {
                leftSum += sums[0][c-1];
                sums[r][c] = sums[r][c - 1] - totalSum + 2 * leftSum; // columns to the left were subtracted off once, but they
                                                           // should
                                                           // actually increase once
                minCost = Math.min(minCost, sums[r][c]);
            }
        }

        System.out.println(minCost);

    }
}