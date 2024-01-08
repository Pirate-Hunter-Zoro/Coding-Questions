import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * There is a directed weighted graph that consists of n nodes numbered from 0
 * to n - 1. The edges of the graph are initially represented by the given array
 * edges where edges[i] = [from_i, to_i, edgeCost_i] meaning that there is an
 * edge
 * from from_i to to_i with the cost edgeCost_i.
 * 
 * Implement the Graph class:
 * 
 * Graph(int n, int[][] edges) initializes the object with n nodes and the given
 * edges.
 * addEdge(int[] edge) adds an edge to the list of edges where edge = [from, to,
 * edgeCost]. It is guaranteed that there is no edge between the two nodes
 * before adding this one.
 * int shortestPath(int node1, int node2) returns the minimum cost of a path
 * from node1 to node2. If no path exists, return -1. The cost of a path is the
 * sum of the costs of the edges in the path.
 * 
 * Link:
 * https://leetcode.com/problems/design-graph-with-shortest-path-calculator/?envType=daily-question&envId=2023-12-23
 */
public class Graph {

    public static void main(String[] args) {
        Graph graph = new Graph(13,
                new int[][] { { 7, 2, 131570 }, { 9, 4, 622890 }, { 9, 1, 812365 }, { 1, 3, 399349 }, { 10, 2, 407736 },
                        { 6, 7, 880509 }, { 1, 4, 289656 }, { 8, 0, 802664 }, { 6, 4, 826732 }, { 10, 3, 567982 },
                        { 5, 6, 434340 }, { 4, 7, 833968 }, { 12, 1, 578047 }, { 8, 5, 739814 }, { 10, 9, 648073 },
                        { 1, 6, 679167 }, { 3, 6, 933017 }, { 0, 10, 399226 }, { 1, 11, 915959 }, { 0, 12, 393037 },
                        { 11, 5, 811057 }, { 6, 2, 100832 }, { 5, 1, 731872 }, { 3, 8, 741455 }, { 2, 9, 835397 },
                        { 7, 0, 516610 }, { 11, 8, 680504 }, { 3, 11, 455056 }, { 1, 0, 252721 } });
        int len = graph.shortestPath(9, 3);
        graph.addEdge(new int[] { 11, 1, 873094 });
        len = graph.shortestPath(3, 10); // outputs 1393401, but should output 1943345
        // Everything that is wrong with the second LeetCode test case is wrong because it is too small - likely meaning that I forgot to add something when calculating the cost in the algorithm.
        System.out.println(len);
    }

    // If we are needing to use Djikstra's Algorithm for the shortest path between
    // two nodes, then a convenient way to store this graph is a connectivity list.
    List<Integer>[] connectivityList;
    Set<Integer>[] canReachMe;

    // However, to store edge weights, let us also use an adjacency matrix
    int[][] adjacencyMatrix;

    // Further, we will use a matrix to store the cost from getting to any node to
    // any other node
    int[][] costsToConnect;

    // For each node, keep track of whether we need to recalculate its shortest path
    boolean[] calculatePath;

    /**
     * Initialize the graph with its adjacency lists and matrices
     * 
     * @param n
     * @param edges
     */
    public Graph(int n, int[][] edges) {
        connectivityList = new ArrayList[n];
        canReachMe = new HashSet[n];
        adjacencyMatrix = new int[n][n];
        costsToConnect = new int[n][n];
        calculatePath = new boolean[n];
        for (int i = 0; i < calculatePath.length; i++)
            calculatePath[i] = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    adjacencyMatrix[i][j] = -1;
                    costsToConnect[i][j] = -1;
                } else {
                    adjacencyMatrix[i][j] = 0;
                    costsToConnect[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            connectivityList[i] = new ArrayList<>();
            adjacencyMatrix[i][i] = 0;
            canReachMe[i] = new HashSet<>();
        }
        for (int[] edge : edges) {
            this.addEdge(edge);
        }
    }

    /**
     * Add an edge to the graph - which could change the shortest paths to and from
     * each node
     * 
     * @param edge
     */
    public void addEdge(int[] edge) {
        int fromNode = edge[0];
        int toNode = edge[1];
        int cost = edge[2];
        connectivityList[fromNode].add(toNode);
        adjacencyMatrix[fromNode][toNode] = cost;
        canReachMe[toNode].add(fromNode); // That's a set - so elements will not be repeated from adding them again

        // Every node who can reach fromNode can now reach toNode
        for (int node : canReachMe[fromNode]) {
            canReachMe[toNode].add(node);
        }

        // Every node who can reach toNode may have just had their shortest paths
        // change
        for (int node : canReachMe[toNode]) {
            calculatePath[node] = true;
        }
    }

    /**
     * Find the shortest path between two nodes
     * 
     * @param node1
     * @param node2
     * @return int
     */
    public int shortestPath(int node1, int node2) {
        // TODO - correct this algorithm

        if (calculatePath[node1]) { // We have some work to do.
            // Find the shortest path from every node to every other node that it can reach
            TreeSet<NodeWithCost> addNext = new TreeSet<>(NodeWithCost.NODE_COMPARATOR);
            addNext.add(new NodeWithCost(node1, 0));
            HashMap<Integer, NodeWithCost> nodeMap = new HashMap<>(); // To avoid recreating NodeWithCost objects
            HashSet<Integer> confirmed = new HashSet<>();

            int next;
            while (!addNext.isEmpty()) {
                next = addNext.pollFirst().nodeID;
                confirmed.add(next);
                for (int connected : connectivityList[next]) {
                    if (confirmed.contains(connected)) // not interested
                        continue;

                    // First, find a new node this node can directly reach - and update everyone's
                    // shortest paths to that node accordingly
                    int weightOfConnection = adjacencyMatrix[next][connected];
                    if (costsToConnect[next][connected] == -1) {
                        costsToConnect[next][connected] = weightOfConnection;
                    } else { // One may think that since it's only one edge, it's the best connection, but
                             // not if it's a super weighty edge.
                        costsToConnect[next][connected] = Math.min(weightOfConnection,
                                costsToConnect[next][connected]);
                    }
                    canReachMe[connected].add(next); // No duplicates will occur - it is a set
                    // Everything that can reach 'next' can now reach 'connected'. If they already
                    // could, see if this new way through 'next' is shorter.
                    for (int canReachNext : canReachMe[next]) {
                        canReachMe[connected].add(canReachNext);
                        int newPathWeight = costsToConnect[canReachNext][next] + costsToConnect[next][connected];
                        if (costsToConnect[canReachNext][connected] == -1) {
                            costsToConnect[canReachNext][connected] = newPathWeight;
                        } else {
                            costsToConnect[canReachNext][connected] = Math
                                    .min(costsToConnect[canReachNext][connected], newPathWeight);
                        }
                    }

                    // Now we need to add this connection to our heap
                    if (nodeMap.containsKey(connected)) {
                        NodeWithCost connection = nodeMap.get(connected);
                        if (connection.cost > costsToConnect[node1][connected]) {
                            addNext.remove(connection);
                            connection.setCost(costsToConnect[node1][connected]);
                            addNext.add(connection);
                        }
                    } else {
                        NodeWithCost connection = new NodeWithCost(connected, costsToConnect[node1][connected]);
                        nodeMap.put(connected, connection);
                        addNext.add(connection);
                    }

                }
            }

            for (int exploredNode : nodeMap.keySet()) {
                calculatePath[exploredNode] = false;
            }
        }
        return costsToConnect[node1][node2];
    }

    private static class NodeWithCost {

        // Attributes for this object - which node are we talking about and what is its
        // cost?
        private int nodeID;
        private int cost;

        public NodeWithCost(int nodeID, int cost) {
            this.nodeID = nodeID;
            this.cost = cost;
        }

        public static final Comparator<NodeWithCost> NODE_COMPARATOR = new Comparator<NodeWithCost>() {
            /**
             * How to compare two nodes with two given costs
             * 
             * @param o1
             * @param o2
             * @return int
             */
            @Override
            public int compare(NodeWithCost o1, NodeWithCost o2) {
                return Integer.compare(o1.cost, o2.cost);
            };
        };

        public void setCost(int cost) {
            this.cost = cost;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof NodeWithCost))
                return false;
            NodeWithCost other = (NodeWithCost) obj;
            return this.nodeID == other.nodeID;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.nodeID);
        }
    }

}