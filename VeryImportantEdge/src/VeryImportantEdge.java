import java.io.*;
import java.util.*;

// Link:
// https://naq23.kattis.com/contests/naq23-spring/problems/naq23.veryimportantedge
public class VeryImportantEdge {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    public static void main(String[] args) throws Exception {

        int[] nm = nextIntArray();
        int vertices = nm[0];
        int edges = nm[1];

        List<Edge>[] graph = new List[vertices];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < edges; i++) {
            int[] edge = nextIntArray();
            int node1 = edge[0];
            int node2 = edge[1];
            int weight = edge[2];
            Edge e = new Edge(node1, node2, weight);
            graph[node1 - 1].add(e);
            graph[node2 - 1].add(e);
        }

        // Do Prim's Algorithm
        long sum = 0l, weightDiff = 0l;

        Node[] visited = new Node[vertices]; // If null, then not part of the spanning tree yet
        PriorityQueue<Edge> primaryHeap = new PriorityQueue<>();

        visited[0] = new Node(1, null);
        for (Edge e : graph[0]) {
            // start at node 1
            primaryHeap.add(e);
        }

        while (!primaryHeap.isEmpty()) {
            Edge nextEdge = primaryHeap.poll();

            if (visited[nextEdge.getLeft() - 1] != null && visited[nextEdge.getRight() - 1] != null) {
                // find the least common ancestor to get our hands on the cycle this creates
                Optional<Edge> e = cycleDetection(visited, nextEdge.getLeft(), nextEdge.getRight());
                if (e.isEmpty()) {
                    continue;
                }
                weightDiff = Math.max(weightDiff, nextEdge.weight - e.get().weight);
                e.get().black = true;
                continue;
            }

            sum += nextEdge.getWeight();

            int nextVert = (visited[nextEdge.getLeft() - 1] != null) ? nextEdge.getRight() : nextEdge.getLeft();
            visited[nextVert] = new Node(nextVert, nextEdge);

            for (Edge e : graph[nextVert - 1]) {
                if (visited[e.getLeft() - 1] != null && visited[e.getRight() - 1] != null) {
                    continue;
                }
                primaryHeap.add(e);
            }
        }

        System.out.println(sum + weightDiff);

        // Now that our minimum spanning tree is created, we go back through our
        // secondary heap

    }

    /**
     * Find the least common ancestor of the two nodes
     * @param visited
     * @param left
     * @param right
     * @return {@link Optional<Edge>}
     */
    static Optional<Edge> cycleDetection(Node[] visited, int left, int right) {
        HashSet<Integer> seen = new HashSet<>();
        PriorityQueue<Edge> cycle = new PriorityQueue<>();

        // Traverse from left up to the root
        Node current = visited[left];
        seen.add(current.label);
        ArrayList<Integer> firstPath = new ArrayList<>();
        while (current.label != 1) {
            firstPath.add(current.getParentNodeID());
            current = visited[current.getParentNodeID()];
            seen.add(current.getParentNodeID());
        }
        current = visited[right];
        ArrayList<Integer> secondPath = new ArrayList<>();
        int lca = -1;
        while (current.label != 1) {
            secondPath.add(current.getParentNodeID());
            current = visited[current.getParentNodeID()];
            if (seen.contains((current.getParentNodeID()))) {
                lca = current.getParentNodeID();
                break;
            }
        }

        // Now we know the least common ancestor - it is the last element in the secondPath
        int idx = 0;
        int currentNodeId = firstPath.get(idx);
        while (currentNodeId != lca) {
            Edge edgeInPath = visited[currentNodeId].parent;
            if (!edgeInPath.black)
                cycle.add(edgeInPath);
            idx++;
            currentNodeId = firstPath.get(idx);
        }
        // Now go from the right to complete the edges in the cycle we get to pick from
        idx = 0;
        currentNodeId = secondPath.get(idx);
        while (currentNodeId != lca) {
            Edge edgeInPath = visited[currentNodeId].parent;
            if (!edgeInPath.black)
                cycle.add(edgeInPath);
            idx++;
            currentNodeId = secondPath.get(idx);
        }

        return (cycle.isEmpty()) ? Optional.empty() : Optional.of(cycle.poll());
    }

    /**
     * A node for a graph
     */
    static class Node {
        int label;
        Edge parent;

        public Node(int label, Edge parent) {
            this.label = label;
            this.parent = parent;
        }

        public int getParentNodeID() {
            return (parent.left == label) ? parent.right : parent.left;
        }
    }

    /**
     * An edge for a graph
     */
    static class Edge implements Comparable<Edge> {
        int left;
        int right;
        int weight;
        boolean black;

        public Edge(int left, int right, int weight) {
            this.left = left;
            this.right = right;
            this.weight = weight;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public int compareTo(VeryImportantEdge.Edge o) {
            return this.weight - o.weight;
        }
    }
}
