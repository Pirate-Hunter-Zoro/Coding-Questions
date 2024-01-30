import java.io.*;
import java.util.*;

/**
 * Link to problem:
 * https://open.kattis.com/problems/branchmanager
 */
public class Main {

    static BufferedReader reader;

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().strip());
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String fileName = args[0];
            File toReadFrom = new File("resources/data/secret/" + fileName);
            reader = new BufferedReader(new FileReader(toReadFrom));
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        int[] nm = nextIntArray();
        int cities = nm[0];
        int people = nm[1];
    }
}
