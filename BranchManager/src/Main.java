import java.io.*;
import java.util.*;

public class Main {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().strip());
    }

    public static void main(String[] args) throws Exception {
        int[] nm = nextIntArray();
        int cities = nm[0];
        int people = nm[1];

        HashMap<Integer, Node> nodes = new HashMap<>();
        
    }

    static class Node {
        private int id;
        private boolean reachedLastTime;
        private Node parent;
        private List<Node> children;

        public Node(int id, boolean closed) {
            
        }
    }

}
