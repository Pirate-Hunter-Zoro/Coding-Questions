import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
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
    }

    // If we are needing to use Djikstra's Algorithm for the shortest path between
    // two nodes, then a convenient way to store this graph is a connectivity list.
    List<Integer>[] connectivityList;
    Set<Integer>[] canReachMe;
    HashMap<Integer, NodeWithCost> nodes;

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

        nodes = new HashMap<>();
        for (int i=0; i<n; i++) {
            nodes.put(i, new NodeWithCost(i, 0));
        }

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

        if (calculatePath[node1]) { // We have some work to do.
            // Find the shortest path from every node to every other node that it can reach
            PriorityQueue<NodeWithCost> addNext = new PriorityQueue<>(NodeWithCost.NODE_COMPARATOR);
            addNext.add(new NodeWithCost(node1, 0));
            HashSet<Integer> confirmed = new HashSet<>();
            HashSet<Integer> added = new HashSet<>();
            
            while (!addNext.isEmpty()) {
                NodeWithCost next = addNext.poll();
                confirmed.add(next.nodeID);
                costsToConnect[node1][next.nodeID] = next.cost;

                // Now add all of the non-confirmed neighbors
                for (int neighbor : connectivityList[next.nodeID]) {
                    if (!confirmed.contains(neighbor)) {
                        NodeWithCost node = nodes.get(neighbor);
                        if (added.contains(neighbor)) {
                            // then we need to remove it from the heap, modify it's cost, and re-enter it into the heap
                            addNext.remove(node);
                            node.cost = Math.min(node.cost, next.cost + adjacencyMatrix[next.nodeID][neighbor]);
                        } else {
                            node.cost = next.cost + adjacencyMatrix[next.nodeID][neighbor];
                        }
                        addNext.add(node);
                        added.add(neighbor);
                    }
                }
            }
        }
        calculatePath[node1] = false;
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