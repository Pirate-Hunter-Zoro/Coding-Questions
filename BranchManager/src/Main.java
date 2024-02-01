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

        Node.refresh();
        for (int i=0; i<cities-1; i++) {
            int[] parentChild = nextIntArray();
            int parent = parentChild[0];
            int child = parentChild[1];
            Node.addChild(parent, child);
        }
        Node.stateGraphComplete();

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

    private static class Node implements Comparable<Node> {
        // Identify this node
        private int id;
        private Node parent;
        private ArrayList<Node> children = new ArrayList<>();

        // The following flags are to enable faster path searching
        private boolean closed;
        private boolean reachable;
        private int lowestOpenChild;

        // Keep track of which Nodes have been created
        private static HashMap<Integer, Node> nodeMap = new HashMap<>();

        // Make sure the user knows that the graph is complete
        private static boolean graphComplete;

        /**
         * Static method to clear out all Nodes and enable the creation of new ones with
         * the same ID
         */
        public static void refresh() {
            nodeMap.clear();
            graphComplete = false;
        }

        /**
         * Enable the user to declare their tree complete
         */
        public static void stateGraphComplete() {
            assert (nodeMap.size() > 0);
            graphComplete = true;
            for (Node node : nodeMap.values())
                node.children.sort((n1, n2) -> n1.compareTo(n2));
        }

        /**
         * Method to make another Node a child of this Node
         * 
         * @param child
         */
        public static void addChild(int parent, int child) {
            assert (!graphComplete);
            assert (nodeMap.containsKey(parent) && nodeMap.containsKey(child) && parent != child);
            createNode(parent);
            createNode(child);
            Node parentNode = nodeMap.get(parent);
            Node childNode = nodeMap.get(child);
            parentNode.children.add(childNode);
            childNode.parent = parentNode;
            parentNode.lowestOpenChild = 0;
        }

        /**
         * Determine if we can reach the node with the given ID
         * 
         * @param id
         * @return
         */
        public static boolean reach(int id) {
            assert (nodeMap.containsKey(id));
            assert (graphComplete);
            Node toReach = nodeMap.get(id);
            if (toReach.closed)
                return false;
            else if (toReach.reachable)
                return true;
 
            // Progress up to the highest node which is known to be reachable (up to the root), closing roads as we need to
            Node current = toReach;
            while (current != null && !current.reachable) {
                current.reachable = true;
                if (current.parent != null && current.parent.children.size() > 1) {
                    int oldLowestIndex = current.parent.lowestOpenChild;
                    current.parent.setNewIndex(current.id);
                    for (int i=oldLowestIndex; i<current.parent.lowestOpenChild; i++) {
                        current.parent.children.get(i).close();
                    }
                }

                // Now we need to repeat on this.checkThisNodeForClosing's parent (if the parent is null the loop will end)
                current = current.parent;
            }

            return true;
        }

        /**
         * Modify the Node's lowestOpenChild index to be whatever is needed to make the inputted ID the lowest valid index
         * @param lowestValidID
         */
        private void setNewIndex(int lowestValidID) {
            int left = this.lowestOpenChild; // inclusive
            int right = this.children.size(); // exclusive
            while (left < right) {
                int mid = (left + right) / 2;
                if (this.children.get(mid).id == lowestValidID) {
                    this.lowestOpenChild = mid;
                    break;
                } else if (this.children.get(mid).id < lowestValidID) {
                    left = mid;
                } else {
                    right = mid;
                }
            }
        }

        /**
         * Create a Node with the given ID if said Node has not already been created
         * 
         * @param id
         * @return {@link Node}
         */
        private static void createNode(int id) {
            assert (!graphComplete);
            if (!nodeMap.containsKey(id))
                nodeMap.put(id, new Node(id));
        }

        /**
         * Private constructor for a Node
         * 
         * @param id
         */
        private Node(int id) {
            this.id = id;
            this.lowestOpenChild = -1;
        }

        /**
         * Method to close the immediate road to this city (and every subsequent road)
         */
        private void close() {
            if (!this.closed) {
                this.closed = true;
                for (Node child : this.children)
                    child.close();
            }
        }

        /**
         * Define how two nodes are compared to each other (just compare by ID)
         */
        @Override
        public int compareTo(Main.Node o) {
            return this.id - o.id;
        }
    }
}
