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
        private TreeSet<Node> children = new TreeSet<>();

        // The following flags are to enable faster path searching
        private boolean collapsed;
        private boolean closed;
        private boolean reachable;

        // When we collapse the graph, we can go from a deep node up a long stretch of
        // single parents.
        // But once we reach a non-single parent, we need to know which Node needs to be
        // picked at an intersection to go on the path that includes THIS node.
        private Node checkThisNodeForClosing;

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
            treeCollapsed = false;
        }

        /**
         * Enable the user to declare their tree complete
         */
        public static void stateGraphComplete() {
            assert (nodeMap.size() > 0);
            collapseAll();
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
        }

        /**
         * Determine if we can reach the node with the given ID
         * 
         * @param id
         * @return
         */
        public static boolean reach(int id) {
            assert (treeCollapsed);
            assert (nodeMap.containsKey(id));
            Node toReach = nodeMap.get(id);
            if (toReach.closed)
                return false;
            else if (toReach.reachable)
                return true;

            // One way or another, we will be reaching this Node
            toReach.setReachable();
 
            // See if we need to close any roads
            while (toReach != null) {
                Node compareWithSiblings = toReach.checkThisNodeForClosing;
                if (compareWithSiblings.parent != null) {
                    TreeSet<Node> siblings = compareWithSiblings.parent.children;

                    Node highestIDSibling = siblings.last();
                    // Keep all children with IDs greater than or equal to compareWithSiblings's ID
                    // (inclusive bounds)
                    TreeSet<Node> keep = (TreeSet<Node>) siblings.subSet(compareWithSiblings, true, highestIDSibling, true);

                    Node lowestIDSibling = siblings.first();
                    if (lowestIDSibling != compareWithSiblings) {
                        // Then we have some nodes to close - do NOT include compareWithSiblings in the
                        // nodes we close
                        TreeSet<Node> toClose = (TreeSet<Node>) siblings.subSet(lowestIDSibling, true, compareWithSiblings,
                                false);
                        for (Node nodeToClose : toClose)
                            nodeToClose.close();
                    }

                    // Simply discard all other siblings - we will never need to check them again
                    compareWithSiblings.parent.children = keep;
                }

                // Now we need to repeat on this.checkThisNodeForClosing's parent (if the parent is null the loop will end)
                toReach = toReach.checkThisNodeForClosing.parent;
            }

            return true;
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
            this.checkThisNodeForClosing = this; // by default
        }

        // Keep track of whether we have collapsed the graph
        private static boolean treeCollapsed;

        /**
         * Method to iterate through all of the leaf Nodes and and collapse them up
         */
        private static void collapseAll() {
            assert (graphComplete);
            for (Node node : nodeMap.values()) {
                if (node.children.size() == 0)
                    node.collapse();
            }
            treeCollapsed = true;
        }

        /**
         * This method may do nothing, but suppose one node is connected to a parent
         * with no other children, and THAT parent is connected to a grandparent with no
         * other children.
         * Then the algorithm for picking a path with the lowest index at a fork in the
         * road only has one choice - there's no need to take the time to make it.
         */
        private void collapse() {
            if (this.collapsed)
                return;

            // Otherwise, can we change this.checkThisNodeForClosing to farther up the tree?
            Node current = this.parent;
            while (current != null) {
                if (current.children.size() > 1)
                    break;
                this.checkThisNodeForClosing = current;
                current = current.parent;
            }
            // Now we progress up the tree to this.checkThisNodeForClosing
            if (this.checkThisNodeForClosing != this) {
                current = this.parent;
                while (current != null && current != this.checkThisNodeForClosing) {
                    current.checkThisNodeForClosing = this.checkThisNodeForClosing;
                    current = current.parent;
                }
                if (this.checkThisNodeForClosing.parent != null) 
                    this.checkThisNodeForClosing.parent.collapse();
            } else { // We had siblings
                if (this.parent != null)
                    this.parent.collapse();
            }
            this.collapsed = true;
        }

        /**
         * Mark this node as reachable if it has not already been maked as reachable
         */
        private void setReachable() {
            if (!this.reachable) {
                this.reachable = true;
                Node current = this.parent;
                while (current != null && !current.reachable) {
                    current.reachable = true;
                    current = current.parent;
                }
            }
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
