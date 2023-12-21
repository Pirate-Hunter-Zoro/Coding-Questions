import java.util.ArrayList;

public class Solution {

    public static void main(String[] args) {
        int[][] points = { { 2, -3 }, { -17, -8 }, { 13, 8 }, { -17, -15 } };
        System.out.println(new Solution().minCostConnectPoints(points));
    }

    private static ArrayList<ArrayList<DisjointSetNode>> recGenerateCombos(ArrayList<DisjointSetNode> nodeList,
            int startIndex, int size) {
        if (size == 0 || nodeList.size() - startIndex < size)
            return new ArrayList<>();
        else if (size == 1) {
            ArrayList<ArrayList<DisjointSetNode>> combos = new ArrayList<>();
            for (int i = startIndex; i < nodeList.size(); i++) {
                ArrayList<DisjointSetNode> single = new ArrayList<>();
                single.add(nodeList.get(i));
                combos.add(single);
            }
            return combos;
        }

        ArrayList<ArrayList<DisjointSetNode>> combinations = new ArrayList<>();

        for (int i = startIndex; i < nodeList.size(); i++) {
            ArrayList<ArrayList<DisjointSetNode>> combos = recGenerateCombos(nodeList, i + 1, size - 1);
            for (ArrayList<DisjointSetNode> combo : combos) {
                combo.add(nodeList.get(i));
                combinations.add(combo);
            }
        }

        return combinations;
    }

    // geneerate all combinations of the given size from the list of nodes
    private static ArrayList<ArrayList<DisjointSetNode>> generateCombos(ArrayList<DisjointSetNode> nodeList, int size) {
        return recGenerateCombos(nodeList, 0, size);
    }

    /**
     * You are given an array points representing integer coordinates of some points
     * on a 2D-plane, where points[i] = [xi, yi].
     * 
     * The cost of connecting two points [xi, yi] and [xj, yj] is the manhattan
     * distance between them: |xi - xj| + |yi - yj|, where |val| denotes the
     * absolute value of val.
     * 
     * Return the minimum cost to make all points connected. All points are
     * connected if there is exactly one simple path between any two points.
     * 
     * Link: https://leetcode.com/problems/min-cost-to-connect-all-points/
     * 
     * @param points
     * @return
     */
    public int minCostConnectPoints(int[][] points) {
        int totalCost = 0;

        ArrayList<DisjointSetNode> nodeList = new ArrayList<>();

        for (int[] point : points) {
            nodeList.add(new DisjointSetNode(point[0], point[1]));
        }

        EdgeQueue queue = new EdgeQueue();

        ArrayList<ArrayList<DisjointSetNode>> allPairs = generateCombos(nodeList, 2);

        for (ArrayList<DisjointSetNode> nodePair : allPairs) {
            queue.enqueue(new Edge(nodePair.get(0), nodePair.get(1)));
        }

        int numEdges = 0;
        while (numEdges < points.length - 1) {
            Edge nextEdge = queue.dequeue();
            if (!(nextEdge.getFirst().sameSet(nextEdge.getSecond()))) {
                totalCost += nextEdge.getDistance();
                nextEdge.getFirst().join(nextEdge.getSecond());
                numEdges++;
            }
        }

        return totalCost;
    }
}