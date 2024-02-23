import java.io.*;
import java.util.*;

public class GardenOfThorns {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * Eddy owns a rectangular garden and has noticed some trespassers stomping
     * through his garden. There are some plants that he wants to protect. He hires
     * an assistant, Zyra, to patrol and protect his garden.
     * 
     * Zyra cannot be bothered to monitor his garden, so she plants a circle of
     * thorns centered at a randomly chosen location within the boundaries of his
     * garden. A plant is considered protected if it is strictly inside the circle
     * of thorns - that is, the distance from the plant to the center of the circle
     * of thorns is less than the circle’s radius. The circle of thorns may extend
     * outside of the boundary of the rectangular garden, though all plants will be
     * inside or on the boundary of the garden.
     * 
     * Given the random nature of the placement of Zyra’s circle of thorns, compute
     * the expected value of the plants that will be protected. Note that Zyra’s
     * circle of thorns does not have to be centered at integer coordinates.
     * 
     * Input
     * 
     * The first line of input contains four integers (), (), and (), where is the
     * number of plants in Eddy’s garden, is the radius of Zyra’s circle of thorns,
     * is the width of Eddy’s garden and is the height of the garden.
     * 
     * Each of the next lines contains three integers (), () and (), where denotes
     * the position of a plant from the lower left corner of Eddy’s garden, and is
     * the value of that plant. No two plants will be at the same position.
     * 
     * Output
     * 
     * Output a single real number, which is the expected value of plants which will
     * be protected by Zyra’s circle of thorns. Any answer within an absolute or
     * relative error of will be accepted.
     * 
     * Link:
     * https://open.kattis.com/problems/gardenofthorns
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        int[] nrwh = nextIntArray();
        int n = nrwh[0];
        int r = nrwh[1];
        int w = nrwh[2];
        int h = nrwh[3];

        double expectedValueProtected = 0.0;

        double thornCircleArea = Math.PI * Math.pow(r, 2);
        double totalCirclePossibleArea = w * h + 2 * w * r + 2 * h * r + thornCircleArea;

        for (int i = 0; i < n; i++) {
            int[] plant = nextIntArray();
            int v = plant[plant.length - 1];
            expectedValueProtected += v * thornCircleArea / totalCirclePossibleArea;
        }

        System.out.println(expectedValueProtected);

    }
}
