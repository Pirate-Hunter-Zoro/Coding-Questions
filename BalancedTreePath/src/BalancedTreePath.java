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

    static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    static BufferedReader fileReader;

    static File inputFile;

    static int[] nextIntArray() throws IOException {
        if (fileReader != null) {
            return Arrays.stream(fileReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        }
        return Arrays.stream(inputReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int nextInt() throws IOException {
        if (fileReader != null) {
            return Integer.parseInt(fileReader.readLine().trim());
        }
        return Integer.parseInt(inputReader.readLine().trim());
    }

    static String nextLine() throws IOException {
        if (fileReader != null) {
            return fileReader.readLine().strip();
        }
        return inputReader.readLine().strip();
    }

    static int[] nextCharArray() throws IOException {
        return nextLine().chars().toArray();
    }

    static boolean[] closing = new boolean[200];
    static int[] corresponding = new int[200];

    public static void main(String[] args) throws IOException {
        inputFile = null;
        fileReader = null;

        if (args.length > 0) {
            inputFile = new File(args[0]);
            fileReader = new BufferedReader(new FileReader(inputFile));
        }

        int nodes = nextInt();
        int[] annotations = nextCharArray();

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] graph = new ArrayList[nodes+1]; // Just ignore index 0
        for (int i=1; i<graph.length; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i=1; i<nodes; i++) {
            int[] firstSecond = nextIntArray();
            graph[firstSecond[0]].add(firstSecond[1]);
            graph[firstSecond[1]].add(firstSecond[0]);
        }

        closing[(int)'}'] = true;
        closing[(int)']'] = true;
        closing[(int)')'] = true;
        corresponding[(int)'}'] = (int)'{';
        corresponding[(int) ']'] = (int) '[';
        corresponding[(int) ')'] = (int) '(';

        int balancedPaths = 0;
        int[] chars_stack = new int[nodes];
        int idx = -1;
        for (int i=1; i<=nodes; i++) {
            balancedPaths += countBalancedPaths(-1, i, graph, annotations, idx, chars_stack);
        }
    
        System.out.println(balancedPaths);
    }

    /**
     * Helper method to count the number of balanced paths given the current stack
     * of characters and the TreeNode that we just came from
     * 
     * @param cameFrom
     * @param start
     * @param graph
     * @param annotations
     * @param idx
     * @param char_stack
     * @return int
     */
    static int countBalancedPaths(int cameFrom, int start, ArrayList<Integer>[] graph, int[] annotations, int idx, int[] char_stack) {
        int annotation = annotations[start-1];
        if (idx == -1 && closing[annotation]) 
            return 0;
        else if (!closing[annotation]) {
            int paths = 0;
            idx++;
            char_stack[idx] = annotation;
            for (int neighbor : graph[start]) {
                if (neighbor != cameFrom) {
                    paths += countBalancedPaths(start, neighbor, graph, annotations, idx, char_stack);
                }
            }
            idx--;
            return paths;
        } else {
            int prev = char_stack[idx];
            idx--;
            int paths = 0;
            if (corresponding[annotation] == prev) {
                if (idx == -1) {
                    paths++;
                }
                for (int neighbor : graph[start]) {
                    if (neighbor != cameFrom) {
                        paths += countBalancedPaths(start, neighbor, graph, annotations, idx, char_stack);
                    }
                }
            } 
            idx++;
            char_stack[idx] = prev;
            return paths;
        }
    }
}
