import java.io.*;
import java.util.*;

/**
 * Vi is a coach for her university’s ICPC organization and is working on
 * creating teams for their upcoming regional contest. They recently competed in
 * the North America Qualifier and Vi is using the results as well as each
 * person’s preferences to create as many teams of three as possible to send to
 * regionals.
 * 
 * More specifically, people from Vi’s university competed in the North America
 * Qualifier (NAQ), and each person got a unique rank from to . The person at
 * rank has two parameters, and , where , indicating that their two teammates
 * must have a rank between and , inclusive. Teams must have exactly three
 * people.
 * 
 * Due to the collaborative environment, Vi notes that for every pair of
 * individuals at ranks and , if , then and .
 * 
 * Compute the maximum number of teams that Vi can send to regionals
 * 
 * Input
 * 
 * The first line of input contains a single integer (), which is the number of
 * competitors in the local contest.
 * 
 * Each of the next lines contains two integers and (), where is the
 * competitor’s rank. These are the limits of the ranks of the competitors that
 * can be teamed with this competitor. The competitors are given in rank order,
 * from to . If , then and .
 * 
 * Output
 * 
 * Output a single integer, which is the maximum number of teams Vi can send to
 * the regional contest.
 * 
 * Link:
 * https://open.kattis.com/problems/icpcteamgeneration
 */
public class ICPCTeamGeneration {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().trim());
    }

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    public static void main(String[] args) throws IOException {
        int n = nextInt();

        int[][] intervals = new int[n][2];

        for (int i=0; i<n; i++) {
            int[] interval = nextIntArray();
            interval[0] = interval[0] - 1;
            interval[1] = interval[1] - 1;
            // Keep our ranking/indexing consistent
            intervals[i] = interval;
        }

        int teams = 0;
        for (int i=intervals.length-1; i>=0; i--) {
            int rank = i;
            int lowestAcceptance = intervals[i][0];

            int highestCompatibleTeam = lowestAcceptance;
            while (intervals[highestCompatibleTeam][1]<rank)
                highestCompatibleTeam++;
            if (highestCompatibleTeam <= rank-2) { // Then we can fit three people in this team
                // Greedily take from the top
                teams++;
                i=rank-2; // Then i will be subtracted to rank-3, and we will repeat on rank-3
            }
        }

        System.out.println(teams);
    }
}
