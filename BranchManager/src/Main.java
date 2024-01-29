import java.io.*;
import java.util.*;

/**
 * Link to problem:
 * https://open.kattis.com/problems/branchmanager
 */
public class Main {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().strip());
    }

    public static void main(String[] args) throws IOException {
        int[] nm = nextIntArray();
        int cities = nm[0];
        int people = nm[1];

        Node.refresh();

        for (int i=0; i<cities-1; i++) {
            int[] fromTo = nextIntArray();
            Node.add(fromTo[0]);
            Node.add(fromTo[1]);
            Node.addRoad(fromTo[0], fromTo[1]);
        }

        int reached = 0;
        for (int i=0; i<people; i++) {
            int destination = nextInt();
            if (Node.reach(destination))
                reached++;
            else
                break;
        }

        System.out.println(reached);
    }

    static class Node {
        private int id;
        private boolean reachedLastTime;
        private boolean closed;
        private Node parent;
        private List<Node> children;

        public static HashMap<Integer, Node> nodes = new HashMap<>();

        public static void refresh() {
            nodes = new HashMap<>();
        }

        public static void add(int id) {
            if (!nodes.containsKey(id)) {
                nodes.put(id, new Node(id));
            }
        }

        private Node(int id) {
            this.id = id;
            closed = false;
            this.children = new ArrayList<>();
        }

        public static void addRoad(int from, int to) {
            nodes.get(from).children.add(nodes.get(to));
            nodes.get(to).parent = nodes.get(from);
        }

        private void close() {
            if (!closed) {
                closed = true;
                for (Node child : children)
                    child.close();
            }
        }

        public static boolean reach(int id) {
            Node destination = nodes.get(id);
            if (destination.closed)
                return false;
            else if (destination.reachedLastTime)
                return true;

            destination.reachedLastTime = true;
            if (destination.parent != null) {
                for (Node sibling : destination.parent.children) {
                    if (sibling != destination && sibling.id < destination.id) {
                        sibling.close();
                    } else {

                    }
                }
                reach(destination.parent.id);
            } 
            return true;
        }
    }

}
