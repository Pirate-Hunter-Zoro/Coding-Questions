import java.io.*;
import java.util.*;

public class OnCallTeam {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().trim());
    }

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    public static void main(String[] args) throws IOException {
        int[] nm = nextIntArray();
        int numEngineers = nm[0];
        int numServices = nm[1];

        int[] engineerBitstrings = new int[numEngineers];
        for (int i = 0; i < numEngineers; i++) {
            engineerBitstrings[i] = Integer.parseInt(reader.readLine(), 2);
        }

        int[] numEngineersCoverThisService = new int[numServices];
        int[][] theseEngineersCoverThisService = new int[numServices][numEngineers];
        for (int i=0; i<numEngineers; i++) {
            int engineerCapability = engineerBitstrings[i];
            // Go through each task, and see if this engineer can cover it
            for (int j=0; j<numServices; j++) {
                int serviceBitString = 1 << j;
                if ((engineerCapability & serviceBitString) > 0) {
                    // Then the engineer can cover this task
                    theseEngineersCoverThisService[j][numEngineersCoverThisService[j]] = i+1;
                    numEngineersCoverThisService[j]++;
                }
            }
        }

        int best = numServices;
        int[] maxSelectionEngineerUsedIn = new int[numEngineers];
        for (int serviceSelection=1; serviceSelection<=(int)Math.pow(2, numServices)-1; serviceSelection++) {
            // Iterates through all possible bitstrings of service selections
            // For each service selection, find how many engineers cover at least one of its services
            int numServicesSelected = Integer.bitCount(serviceSelection);
            if (numServicesSelected > best)
                continue;

            int numAssignableEngineers = 0;
            // Compute the size of the union of all the engineers which can be assigned to at least one of the tasks in this selection
            for (int service = 1; service <= numServices; service++) {
                int serviceBitString = 1 << (service-1);
                if ((serviceSelection & serviceBitString) > 0) {
                    // Then we do need this task assigned to someone - grab everybody who can be assigned to it whom we have not already grabbed
                    for (int i=0; i<numEngineersCoverThisService[service-1]; i++) {
                        int engineer = theseEngineersCoverThisService[service-1][i];
                        if (maxSelectionEngineerUsedIn[engineer-1] < serviceSelection) {
                            // We have not yet used this engineer for this service selection
                            maxSelectionEngineerUsedIn[engineer-1] = serviceSelection; // Now we have
                            numAssignableEngineers++;
                            if (numAssignableEngineers >= numServicesSelected)
                                break;
                        }
                    }
                }
                if (numAssignableEngineers >= numServicesSelected)
                    break;
            }

            // Hall's Theorem
            if (numAssignableEngineers < numServicesSelected)
                best = Math.min(best, numServicesSelected-1);
        }
        
        System.out.println(best);
    }

}
