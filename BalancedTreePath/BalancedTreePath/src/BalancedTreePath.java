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

    }

    static class TreeNode {
        private int id;
        private char annotation;
        private int numBalancedPathsStartingHere = -1;
    }
}
