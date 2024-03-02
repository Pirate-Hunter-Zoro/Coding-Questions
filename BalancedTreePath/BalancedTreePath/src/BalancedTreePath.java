import java.io.*;
import java.util.*;

/**
 * You are given a tree where each node is annotated with a character from
 * ()[]{}. A path is a sequence of one or more nodes where no node is repeated
 * and every pair of adjacent nodes is connected with an edge. A path is
 * balanced if the characters at each node, when concatenated, form a balanced
 * string. A string is balanced if it satisfies the following definition:
 * 
 * An empty string is balanced.
 * 
 * If 'a' is a balanced string, then ('a'), ['a'], and {'a'} are balanced
 * strings.
 * 
 * If 'a' and 'b' are balanced strings, then 'a' concatenated with 'b' is a
 * balanced string.
 * 
 * Compute the number of balanced paths over the entire tree.
 * 
 * Input:
 * 
 * The first line of input contains a single integer n.
 * 
 * The next line contains a string of characters, where each character is one of
 * ()[]{}.
 * 
 * Each of the next n-1 lines contains two integers, u and v, indicating that
 * nodes and are connected with an edge. It is guaranteed the graph is a tree.
 * 
 * Output:
 * 
 * Output a single integer, which is the number of balanced paths over the
 * entire tree.
 * 
 * Link:
 * https://open.kattis.com/problems/balancedtreepath
 */
public class BalancedTreePath {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().trim());
    }

    static String nextLine() throws IOException {
        return reader.readLine().strip();
    }

    public static void main(String[] args) throws IOException {

        HashSet<TreeNode> nodeOpenings = new HashSet<>();
        HashMap<Integer, TreeNode> nodes = new HashMap<>();
        int n = nextInt();
        String chars = nextLine();
        for (int i = 0; i < n; i++) {
            int nodeId = i + 1;
            char c = chars.charAt(i);
            nodes.put(nodeId, new TreeNode(nodeId, c));
            if (c == '(' || c == '[' || c == '{') {
                nodeOpenings.add(nodes.get(nodeId));
            }
        }

        for (int i = 0; i < n-1; i++) {
            int[] connection = nextIntArray();
            int first = connection[0];
            int second = connection[1];
            nodes.get(first).connections.add(nodes.get(second));
            nodes.get(second).connections.add(nodes.get(first));
        }

        int numPaths = 0;
        for (TreeNode start : nodeOpenings) {
            numPaths += start.findNumBalancedStartingHere();
        }

        System.out.println(numPaths);

    }

    static class TreeNode {
        final int id;
        final char annotation;
        int numBalancedPathsStartingHere = -1;

        static final HashSet<Character> opening = new HashSet<Character>(Arrays.asList('(','{','['));
        static final HashSet<Character> closing = new HashSet<Character>(Arrays.asList(')', '}', ']'));

        static final HashMap<Character,Character> corresponding = new HashMap<Character, Character>() {
            {
                put(')', '(');
                put('}', '{');
                put(']','[');
            }
        };

        ArrayList<TreeNode> connections = new ArrayList<>();

        TreeNode(int id, char annotation) {
            this.id = id;
            this.annotation = annotation;
            if (closing.contains(this.annotation))
                this.numBalancedPathsStartingHere = 0;
        }

        public void setNumBalancedPathsStartingHere(int numBalancedPathsStartingHere) {
            this.numBalancedPathsStartingHere = numBalancedPathsStartingHere;
        }

        public int findNumBalancedStartingHere() {
            if (this.numBalancedPathsStartingHere == -1) {
                this.numBalancedPathsStartingHere = 0;
                for (TreeNode next : this.connections) {
                    Stack<Character> chars = new Stack<>();
                    chars.push(this.annotation);
                    this.numBalancedPathsStartingHere += next.countPathsJustCameFrom(this, chars);
                }
            }
            return this.numBalancedPathsStartingHere;
        }

        private int countPathsJustCameFrom(TreeNode cameFrom, Stack<Character> chars) {
            char prevChar = chars.peek();
            if (opening.contains(this.annotation)) {
                chars.push(this.annotation);
                int paths = 0;
                for (TreeNode next : this.connections) {
                    if (!next.equals(cameFrom)) {
                        paths += next.countPathsJustCameFrom(this, chars); 
                    }
                }
                return paths;
            } else {
                if (corresponding.get(this.annotation) != prevChar) {
                    // we just ran into a closing character that does NOT close the most recent opening character - dead end
                    return 0;
                } else {
                    chars.pop();
                    // we did just close the most recent non-closed opening character
                    // did that empty our stack?
                    if (chars.empty()) {
                        int paths = 1;
                        for (TreeNode neighbor : this.connections) {
                            if (!neighbor.equals(cameFrom)) {
                                paths += neighbor.findNumBalancedStartingHere();
                            }
                        }
                        return paths;
                    } else {
                        // we did not empty the stack, so progress from here
                        int paths = 0;
                        for (TreeNode neighbor : this.connections) {
                            if (!neighbor.equals(cameFrom)) {
                                paths += neighbor.countPathsJustCameFrom(this, chars);
                            }
                        }
                        return paths;
                    }
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof TreeNode))
                return false;
            TreeNode other = (TreeNode) obj;
            return other.id == this.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.id);
        }
    }
}
