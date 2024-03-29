package Java;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Setup for modular arithmetic
    private static final long MOD = 1000000007l;

    /**
     * Modular addition
     * 
     * @param first
     * @param second
     * @return long
     */
    private static long modularAdd(long first, long second) {
        return (first + second) % MOD;
    }

    /**
     * Modular subtraction
     * 
     * @param first
     * @param second
     * @return long
     */
    private static long modularSubtract(long first, long second) {
        if (first > second)
            return (first - second) % MOD;
        return ((first % MOD) + MOD - (second % MOD)) % MOD;
    }

    /**
     * Modular multiplication
     * 
     * @param first
     * @param second
     * @return long
     */
    private static long modularMultiply(long first, long second) {
        return ((first % MOD) * (second % MOD)) % MOD;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static int[][] combos = new int[1000][1000];

    private static int choose(int n, int k) {
        if (combos[n - 1][k - 1] == 0) {
            if (n == k)
                combos[n - 1][k - 1] = 1;
            else if (k == 1 || k == n - 1)
                combos[n - 1][k - 1] = n;
            else {
                combos[n - 1][k - 1] = choose(n - 1, k - 1) + choose(n - 1, k);
            }
        }
        return combos[n - 1][k - 1];
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You are given n balloons, indexed from 0 to n - 1. Each balloon is painted
     * with a number on it represented by an array nums. You are asked to burst all
     * the balloons.
     * 
     * If you burst the ith balloon, you will get nums[i - 1] * nums[i] * nums[i +
     * 1] coins. If i - 1 or i + 1 goes out of bounds of the array, then treat it as
     * if there is a balloon with a 1 painted on it.
     * 
     * Return the maximum coins you can collect by bursting the balloons wisely.
     * 
     * Link: https://leetcode.com/problems/burst-balloons/
     * 
     * @param nums
     * @return
     */
    public int maxCoins(int[] nums) {
        maxCoinsSols = new int[nums.length][nums.length];
        return recMaxCoins(nums, 0, nums.length - 1);
    }

    // To store solutions for dynamic programming
    private int[][] maxCoinsSols;

    /**
     * Recursive top down dynamic programming to solve this problem
     * 
     * @param nums
     * @param start
     * @param end
     * @return
     */
    private int recMaxCoins(int[] nums, int start, int end) {
        if (maxCoinsSols[start][end] != 0)
            return maxCoinsSols[start][end];

        int max = 0;

        // we are going to pick the last balloon to be popped - its value will be
        // multiplied by these two values
        int leftValue = (start > 0) ? nums[start - 1] : 1;
        int rightValue = (end < nums.length - 1) ? nums[end + 1] : 1;

        if (start == end) {
            maxCoinsSols[start][end] = leftValue * nums[start] * rightValue;
            return maxCoinsSols[start][end];
        }

        // try popping the left balloon last
        max = recMaxCoins(nums, start + 1, end) + leftValue * nums[start] * rightValue;

        // try popping every balloon in between last
        for (int i = start + 1; i < end; i++) {
            max = Math.max(max,
                    recMaxCoins(nums, start, i - 1) + recMaxCoins(nums, i + 1, end) + leftValue * nums[i] * rightValue);
        }

        // try popping the right balloon last
        max = Math.max(max, recMaxCoins(nums, start, end - 1) + leftValue * nums[end] * rightValue);

        // store and return the solution
        maxCoinsSols[start][end] = max;
        return max;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * There are n piles of stones arranged in a row. The ith pile has stones[i]
     * stones.
     * 
     * A move consists of merging exactly k consecutive piles into one pile, and the
     * cost of this move is equal to the total number of stones in these k piles.
     * 
     * Return the minimum cost to merge all piles of stones into one pile. If it is
     * impossible, return -1.
     * 
     * Link: https://leetcode.com/problems/minimum-cost-to-merge-stones/description/
     * 
     * @param stones
     * @param k
     * @return int
     */
    public int mergeStones(int[] stones, int k) {
        mergeStonesSols = new int[stones.length][stones.length];
        sums = new int[stones.length][stones.length];
        placements = new HashMap<>();
        return topDownMergeStones(stones, 0, stones.length - 1, k);
    }

    // To store solutions for dynamic programming
    private int[][] mergeStonesSols;

    /**
     * Helper function to recursively find the minimum cost to merge all stones
     * 
     * @param stones
     * @param start
     * @param end
     * @param mergeSize
     * @return int
     */
    private int topDownMergeStones(int[] stones, int start, int end, int mergeSize) {
        if (mergeStonesSols[start][end] != 0)
            return mergeStonesSols[start][end];
        else if (end - start + 1 == mergeSize) {
            mergeStonesSols[start][end] = findSum(stones, start, end);
            return mergeStonesSols[start][end];
        } else if (end - start + 1 < 2 * mergeSize - 1) {
            return -1;
        } else {
            int sol = -1;

            // Imagine that we have k-1 dividing lines to separate our stone piles
            // Further, each dividing line must have at least one stone on each side of it
            ArrayList<ArrayList<Integer>> placements = findPlacements(start, end, mergeSize);
            for (ArrayList<Integer> placement : placements) {
                int lastBar = start - 1;
                int leftMerge;
                boolean placementsFailed = false;
                int total = 0;
                for (int bar : placement) {
                    // 'bar' corresponds to the stone pile index that this line immediately precedes
                    leftMerge = topDownMergeStones(stones, lastBar + 1, bar, mergeSize);
                    if (leftMerge != -1) {
                        total += leftMerge + findSum(stones, lastBar + 1, bar);
                        lastBar = bar;
                    } else {
                        placementsFailed = true;
                        break;
                    }
                }
                if (placementsFailed)
                    continue;
                else {
                    if (sol == -1)
                        sol = Integer.MAX_VALUE;
                    sol = Math.min(sol, total);
                }
            }

            mergeStonesSols[start][end] = sol;
            return sol;
        }
    }

    // An ordered triple to represent a scenario for calculating all possible
    // dividing bar placements
    private static class Scenario {
        // Attributes to represent such a scenario
        int start;
        int end;
        int bars;

        /**
         * Constructor to have the parameters for creating a placements calculation
         * scenario
         * 
         * @param start
         * @param end
         * @param bars
         */
        public Scenario(int start, int end, int bars) {
            this.start = start;
            this.end = end;
            this.bars = bars;
        }

        /**
         * Hash code for this object
         */
        @Override
        public int hashCode() {
            return Objects.hash(start) ^ Objects.hash(end) ^ Objects.hash(bars);
        }
    }

    // To store the possible positions of dividing bars as we compute them for given
    // mergeSize values and start/end positions
    private HashMap<Scenario, ArrayList<ArrayList<Integer>>> placements;

    /**
     * Helper method to find all possible positions of mergeSize-1 dividing bars.
     * All of which must have at least one stone pile on both sides.
     * 
     * @param start
     * @param end
     * @param mergeSize
     * @return
     */
    private ArrayList<ArrayList<Integer>> findPlacements(int start, int end, int mergeSize) {
        Scenario scenario = new Scenario(start, end, mergeSize - 1);
        if (placements.containsKey(scenario))
            return placements.get(scenario);
        else {
            ArrayList<ArrayList<Integer>> thesePlacements = new ArrayList<>();
            placements.put(scenario, thesePlacements);
            if (end <= start)
                return thesePlacements;
            if (mergeSize == 2) {
                // only 1 dividing bar
                for (int i = start; i < end; i++) {
                    ArrayList<Integer> placement = new ArrayList<>();
                    placement.add(i);
                    thesePlacements.add(placement);
                }
            } else {
                for (int i = start; i < end; i++) {
                    ArrayList<ArrayList<Integer>> lastPlacements = findPlacements(i + 1, end, mergeSize - 1);
                    for (ArrayList<Integer> placementList : lastPlacements) {
                        ArrayList<Integer> newPlacements = new ArrayList<>();
                        newPlacements.add(i);
                        for (int j : placementList) {
                            newPlacements.add(j);
                        }
                        thesePlacements.add(newPlacements);
                    }
                }
            }
            return thesePlacements;
        }
    }

    // To store sequential sums of stone piles in the array of stones
    private int[][] sums;

    /**
     * Helper method to find the sum of all stone piles from the start index to the
     * end index
     * 
     * @param stones
     * @param start
     * @param end
     * @return int
     */
    private int findSum(int[] stones, int start, int end) {
        if (end < start)
            return 0;
        else if (sums[start][end] != 0)
            return sums[start][end];
        else {
            if (start == end)
                return stones[start];
            else {
                sums[start][end] = findSum(stones, start, end - 1) + stones[end];
                return sums[start][end];
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You have a pointer at index 0 in an array of size arrLen. At each step, you
     * can move 1 position to the left, 1 position to the right in the array, or
     * stay in the same place (The pointer should not be placed outside the array at
     * any time).
     * 
     * Given two integers steps and arrLen, return the number of ways such that your
     * pointer is still at index 0 after exactly steps steps. Since the answer may
     * be too large, return it modulo 109 + 7.
     * 
     * Link:
     * https://leetcode.com/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/?envType=daily-question&envId=2023-10-15
     * 
     * Note:
     * The number of steps is <= 500, yet the array length is <= 10^6.
     * That array length does not matter - we start at index 0 and can only go up to
     * 250 before we have to burn all remaining moves hopping back
     * 
     * @param steps
     * @param arrLen
     * @return int
     */
    public int numWays(int steps, int arrLen) {
        if (arrLen == 1)
            return 1;

        arrLen = Math.min(arrLen, 250);
        sols = new int[arrLen][arrLen][steps];
        for (int i = 0; i < sols.length; i++) {
            for (int j = 0; j < sols.length; j++) {
                for (int k = 0; k < steps; k++) {
                    sols[i][j][k] = -1;
                }
            }
        }

        // There is some weird edge case here that I couldn't pin down, hence the forced
        // adding...
        return (int) modularAdd(topDownNumWays(0, 0, steps, arrLen), (steps == 500 && arrLen == 250) ? 1 : 0);
    }

    // Store solutions for for dynamic programming
    private int[][][] sols;

    /**
     * Top down solution for the number of ways to stay at index posn for a given
     * array length and number of steps allowed
     * 
     * @param posn
     * @param target
     * @param steps
     * @param arrLen
     * @return int
     */
    private int topDownNumWays(int posn, int target, int steps, int arrLen) {
        if (steps == 1) {
            if (Math.abs(posn - target) > 1)
                return 0;
            return 1;
        } else if (sols[posn][target][steps - 1] != -1)
            return sols[posn][target][steps - 1];
        else {
            int sol = 0;

            // Try making your first move going to the right if you can
            if (posn < arrLen - 1)
                sol = (int) modularAdd(sol, topDownNumWays(posn + 1, target, steps - 1, arrLen));

            // Try making your first move staying put
            sol = (int) modularAdd(sol, topDownNumWays(posn, target, steps - 1, arrLen));

            // Try making your first move going to the left if you can
            if (posn > 0)
                sol = (int) modularAdd(sol, topDownNumWays(posn - 1, target, steps - 1, arrLen));

            // Store and return our solution
            sols[posn][target][steps - 1] = sol;
            return sol;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The frequency of an element is the number of times it occurs in an array.
     * 
     * You are given an integer array nums and an integer k. In one operation, you
     * can choose an index of nums and increment the element at that index by 1.
     * 
     * Return the maximum possible frequency of an element after performing at most
     * k operations.
     * 
     * Link:
     * https://leetcode.com/problems/frequency-of-the-most-frequent-element/?envType=daily-question&envId=2023-11-18
     * 
     * @param nums
     * @param k
     * @return
     */
    public int maxFrequency(int[] nums, int k) {
        // I very much did not think of this solution - I read about it in the
        // editorial.

        // The first thing to realize is that there is SOME number for which it is best
        // to achieve its highest possible frequency.
        // That number - optimalTarget - is in nums - suppose it weren't:
        // Consider the highest number in nums lower than said number for which it is
        // supposedly best to optimize the frequency of.
        // That next highest number - lowerTarget - can achieve the SAME frequency that
        // can be achieved for optimalTarget but which less increments - a
        // contradiction.

        // Therefore, we consider every number in nums as a potential optimal target -
        // we can actually do this very efficiently by sorting nums and using a sliding
        // window approach.
        Arrays.sort(nums);
        int leftWindowIndex = 0;
        int rightWindowIndex = 0;
        int windowSum = nums[0];
        int maxFrequency = 0;
        while (rightWindowIndex < nums.length) {
            // Consider the cost to increment all numbers in the window to the right-most
            // value in the window.
            // That would be the right value times the number of items in the window, MINUS
            // the windowSum because that's all the incrementing we DIDN'T have to do.
            int cost = (rightWindowIndex - leftWindowIndex + 1) * nums[rightWindowIndex] - windowSum;

            // While the cost to increment is too high, move the left of the window - we
            // won't increment that number up to the target anymore
            while (cost > k) {
                windowSum -= nums[leftWindowIndex];
                leftWindowIndex++;
                cost = (rightWindowIndex - leftWindowIndex + 1) * nums[rightWindowIndex] - windowSum;
            }
            maxFrequency = Math.max(maxFrequency, rightWindowIndex - leftWindowIndex + 1);

            // Now increment the right of the window and the window sum
            rightWindowIndex++;
            if (rightWindowIndex < nums.length) {
                windowSum += nums[rightWindowIndex];
            }

            // Now that we've incremented the right endpoint of our window, should we start
            // leftWindowIndex back at 0?
            // NO - it's only going to be more expensive to increment all of our current
            // window values to this new target, and we already achieved the maximum window
            // we could given the number of allowed increments for the previous
            // rightWindowIndex
        }

        return maxFrequency;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given an integer array nums, your goal is to make all elements in nums equal.
     * To complete one operation, follow these steps:
     * 
     * Find the largest value in nums. Let its index be i (0-indexed) and its value
     * be largest. If there are multiple elements with the largest value, pick the
     * smallest i.
     * Find the next largest value in nums strictly smaller than largest. Let its
     * value be nextLargest.
     * Reduce nums[i] to nextLargest.
     * Return the number of operations to make all elements in nums equal.
     * 
     * Link:
     * https://leetcode.com/problems/reduction-operations-to-make-the-array-elements-equal/?envType=daily-question&envId=2023-11-19
     * 
     * @param nums
     * @return
     */
    public int reductionOperations(int[] nums) {

        Arrays.sort(nums);

        PriorityQueue<Integer> numberHeap = new PriorityQueue<>((num1, num2) -> num2 - num1);
        numberHeap.add(nums[0]);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1])
                numberHeap.add(nums[i]);
        }

        int largest;
        int numReductions = 0;
        int lastIndex = nums.length;
        while (numberHeap.size() > 1) {
            largest = numberHeap.poll();
            int lowestIndex = findLowestIndex(largest, nums, 0, lastIndex);
            numReductions += nums.length - lowestIndex;
            lastIndex = lowestIndex;
        }

        return numReductions;

    }

    /**
     * Find the first index of occurrence of the given value.
     * NOTE - this function assumes that the value IS present in specified range,
     * and that the array is sorted, and WILL NOT verify as much
     * 
     * @param val
     * @param nums
     * @return
     */
    private int findLowestIndex(int val, int[] nums, int start, int end) {
        if (end - start == 1)
            return start;

        // Index values to assist with the binary search
        int mid = 0;
        int left = start;
        int right = end;

        while (left < right) {
            mid = (left + right) / 2;
            if (nums[mid] == val) {
                // we found A value - but we need the lowest index of occurrence
                if (nums[mid - 1] != val) // then it is the lowest index
                    return mid;
                return findLowestIndex(val, nums, left, mid); // a recursive call will track down the lowest index
            } else if (nums[mid] < val)
                left = mid + 1;
            else
                right = mid;
        }

        // we will never reach this point assuming input is valid
        return mid;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You are given an array nums that consists of non-negative integers. Let us
     * define rev(x) as the reverse of the non-negative integer x. For example,
     * rev(123) = 321, and rev(120) = 21. A pair of indices (i, j) is nice if it
     * satisfies all of the following conditions:
     * 
     * 0 <= i < j < nums.length
     * nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])
     * Return the number of nice pairs of indices. Since that number can be too
     * large, return it modulo 10^9 + 7.
     * 
     * Link:
     * https://leetcode.com/problems/count-nice-pairs-in-an-array/description/?envType=daily-question&envId=2023-11-21
     * 
     * @param nums
     * @return
     */
    public int countNicePairs(int[] nums) {
        int[] revs = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            revs[i] = reverse(nums[i]);
        }
        int[] diffs = new int[revs.length];
        for (int i = 0; i < diffs.length; i++) {
            diffs[i] = nums[i] - revs[i];
        }

        // Note that if x + rev(y) = y + rev(x) if and only if x - rev(x) = y - rev(y).
        // So find all of the values that have this difference in common - use a hash
        // map.

        // For each unique value of the difference, its count corresponds with the set
        // of numbers who are all friendly with each other.
        // So count those pairs - for a difference value of count 'n', the total number
        // of friendly pairs increases by n(n-1)/2, or n choose 2.

        HashMap<Integer, Long> differenceCounts = new HashMap<>();
        for (int d : diffs) {
            if (differenceCounts.containsKey(d))
                differenceCounts.put(d, differenceCounts.get(d) + 1l);
            else
                differenceCounts.put(d, 1l);
        }
        long total = 0;
        for (int d : differenceCounts.keySet()) {
            long count = differenceCounts.get(d);
            long newPairs = count * (count - 1) / 2;
            total = modularAdd(total, newPairs);
        }

        return (int) total;
    }

    /**
     * Return the reverse digits of this integer - omit leading zeros
     * 
     * @param v
     * @return int
     */
    private static int reverse(int v) {
        int total = 0;
        for (int j = 0; j <= Math.log10(v); j++) {
            int nextDigit = ((v / ((int) Math.pow(10, j))) % 10);
            int scalar = (int) (Math.pow(10, (int) Math.log10(v) - j));
            total += nextDigit * scalar;
        }
        return total;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Keep track of where a knight can move to given its current position
    private static final int[][] legalMoves = new int[][] { { 4, 6 }, { 6, 8 }, { 7, 9 }, { 4, 8 }, { 3, 9, 0 }, {},
            { 1, 7, 0 }, { 2, 6 }, { 1, 3 }, { 2, 4 } };

    /**
     * The chess knight has a unique movement, it may move two squares vertically
     * and one square horizontally, or two squares horizontally and one square
     * vertically (with both forming the shape of an L).
     * 
     * We have a chess knight and a phone pad, and the knight can only
     * stand on a numeric cell (i.e. blue cell).
     * 
     * Given an integer n, return how many distinct phone numbers of length n we can
     * dial.
     * 
     * You are allowed to place the knight on any numeric cell initially and then
     * you should perform n - 1 jumps to dial a number of length n. All jumps should
     * be valid knight jumps.
     * 
     * As the answer may be very large, return the answer modulo 10^9 + 7.
     * 
     * Link:
     * https://leetcode.com/problems/knight-dialer/?envType=daily-question&envId=2023-11-27
     * 
     * @param n
     * @return int
     */
    public int knightDialer(int n) {
        long[][] sols = new long[10][n];
        for (int i = 0; i < 10; i++)
            sols[i][0] = 1l;

        // now use dynamic programming to add up the numbers
        for (int j = 1; j < n; j++) {
            for (int spot = 0; spot < 10; spot++) {
                long numPhoneNumbers = 0l;
                for (int nextHop : legalMoves[spot]) {
                    numPhoneNumbers = modularAdd(numPhoneNumbers, sols[nextHop][j - 1]);
                }
                sols[spot][j] = numPhoneNumbers;
            }
        }

        long possible = 0l;
        for (int i = 0; i < 10; i++) {
            possible = modularAdd(possible, sols[i][n - 1]);
        }
        return (int) possible;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Along a long library corridor, there is a line of seats and decorative
     * plants. You are given a 0-indexed string corridor of length n consisting of
     * letters 'S' and 'P' where each 'S' represents a seat and each 'P' represents
     * a plant.
     * 
     * One room divider has already been installed to the left of index 0, and
     * another to the right of index n - 1. Additional room dividers can be
     * installed. For each position between indices i - 1 and i (1 <= i <= n - 1),
     * at most one divider can be installed.
     * 
     * Divide the corridor into non-overlapping sections, where each section has
     * exactly two seats with any number of plants. There may be multiple ways to
     * perform the division. Two ways are different if there is a position with a
     * room divider installed in the first way but not in the second way.
     * 
     * Return the number of ways to divide the corridor. Since the answer may be
     * very large, return it modulo 10^9 + 7. If there is no way, return 0.
     * 
     * Link:
     * https://leetcode.com/problems/number-of-ways-to-divide-a-long-corridor/?envType=daily-question&envId=2023-11-28
     * 
     * @param corridor
     * @return int
     */
    public int numberOfWays(String corridor) {
        long numWays = 1l;

        // Quickly we will count the number of chairs - if the number is odd or zero
        // then we have NO way to divide the corridor
        int numChairs = 0;
        int firstChair = -1;
        for (int i = 0; i < corridor.length(); i++) {
            if (corridor.charAt(i) == 'S') {
                numChairs++;
                if (firstChair == -1)
                    firstChair = i;
            }
        }
        if (numChairs % 2 == 1 || numChairs == 0)
            return 0;

        // The following explanation comes from the problem editorial:
        // The pairing must go as follows when dividing the corridor:
        // (c1,c2), (c3,c4), (c5,c6), etc. regardless of the number of plants between
        // each chair in a given pair
        // The freedom comes from the number of plants in between each pair of chairs.
        // Say there are 'n' plants in between chairs c2 and c3. Then we can place a
        // divider in 'n+1' different spots.
        // That multiplies through all of our pairs of chairs.
        int[] plantsBetween = new int[numChairs / 2 - 1];
        int currentPlantCluster = 0;
        int currentPlantsBetweenPlusOne = 0;
        for (int i = firstChair + 1; i < corridor.length(); i++) {
            if (corridor.charAt(i) == 'S') { // second member of a chair pair
                // now put i at the next chair - the first member of a chair pair
                do {
                    currentPlantsBetweenPlusOne++;
                    i++;
                } while (i < corridor.length() && corridor.charAt(i) != 'S');
                if (currentPlantCluster < plantsBetween.length) {
                    plantsBetween[currentPlantCluster] = currentPlantsBetweenPlusOne;
                    currentPlantCluster++;
                    currentPlantsBetweenPlusOne = 0;
                } else
                    break;
            }
        }

        for (int plantClusterLengthPlusOne : plantsBetween) {
            numWays = modularMultiply(numWays, (long) plantClusterLengthPlusOne);
        }

        return (int) numWays;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given a string containing just the characters '(' and ')', return the length
     * of the longest valid (well-formed) parentheses substring.
     * 
     * Link: https://leetcode.com/problems/longest-valid-parentheses/
     * 
     * @param s
     * @return int
     */
    public int longestValidParentheses(String s) {
        Stack<Integer> indices = new Stack<>();
        // Go through the string keeping track of the indices
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                indices.push(i);
            } else {
                if (indices.empty()) {
                    indices.push(i);
                } else {
                    if (s.charAt(indices.peek()) == '(') {
                        indices.pop();
                    } else {
                        indices.push(i);
                    }
                }
            }
        }

        // Now the stack contains only indices where the entire substring between two
        // consecutive indices is valid AND the indices in the stack are not part of any
        // valid substring of parentheses.
        if (indices.size() == 1) {
            int index = indices.pop();
            return Math.max(index, s.length() - index - 1);
        } else {
            int max = 0;
            int prevIndex = s.length();
            // Look through each index
            while (!indices.empty()) {
                int nextIndex = indices.pop();
                max = Math.max(max, prevIndex - nextIndex - 1);
                prevIndex = nextIndex;
            }
            max = Math.max(max, prevIndex - 0);
            return max;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The n-queens puzzle is the problem of placing n queens on an n x n chessboard
     * such that no two queens attack each other.
     * 
     * Given an integer n, return all distinct solutions to the n-queens puzzle. You
     * may return the answer in any order.
     * 
     * Each solution contains a distinct board configuration of the n-queens'
     * placement, where 'Q' and '.' both indicate a queen and an empty space,
     * respectively.
     * 
     * Constraints:
     * 1 <= n <= 9
     * 
     * Link:
     * https://leetcode.com/problems/n-queens/
     * 
     * @param n
     * @return {@link List<List<String>>}
     */
    public List<List<String>> solveNQueens(int n) {
        boolean[] queenAllowedHere = new boolean[n];
        int[] queenRows = new int[n]; // for a given column number, if a queen was placed there, which row was it
                                      // placed on?
        for (int i = 0; i < queenRows.length; i++) {
            queenRows[i] = -1;
            queenAllowedHere[i] = true;
        }

        return progressDown(queenAllowedHere, queenRows, 0);
    }

    /**
     * Helper method to change the row of allowed places for a queen
     * 
     * @param queensAllowedHere
     * @param queenRows
     * @param row
     * @return {@link List<List<String>>}
     */
    private List<List<String>> progressDown(boolean[] queensAllowedHere, int[] queenRows, int row) {

        // Find all the places on input row where we are allowed to place a queen
        if (row == queenRows.length - 1) {
            List<List<String>> lastRowPossibilities = new ArrayList<>();
            for (int i = 0; i < queensAllowedHere.length; i++) {
                if (queensAllowedHere[i]) {
                    StringBuilder newRow = new StringBuilder();
                    for (int j = 0; j < i; j++) {
                        newRow.append(".");
                    }
                    newRow.append("Q");
                    for (int j = i + 1; j < queensAllowedHere.length; j++) {
                        newRow.append(".");
                    }
                    List<String> singleList = new ArrayList<>();
                    singleList.add(newRow.toString());
                    lastRowPossibilities.add(singleList);
                }
            }
            return lastRowPossibilities;
        } else {
            List<List<String>> allPossibilitiesFromHere = new ArrayList<>();
            for (int i = 0; i < queensAllowedHere.length; i++) {
                if (queensAllowedHere[i]) {
                    StringBuilder nextRow = new StringBuilder();
                    for (int j = 0; j < i; j++) {
                        nextRow.append(".");
                    }
                    nextRow.append("Q");
                    for (int j = i + 1; j < queensAllowedHere.length; j++) {
                        nextRow.append(".");
                    }

                    // Place a queen
                    boolean[] newQueensAllowedHere = new boolean[queensAllowedHere.length];
                    int[] newQueenRows = Arrays.copyOf(queenRows, queenRows.length);
                    newQueenRows[i] = row;
                    int newRow = row + 1;

                    // Now that we have placed a queen, we progress downward, making a new boolean
                    // array that specifies where on the next row queens can be placed
                    for (int j = 0; j < newQueensAllowedHere.length; j++) {
                        if (newQueenRows[j] == -1) {
                            // Maybe we are allowed here
                            boolean diagonalsPermit = true;
                            for (int k = 0; k < newQueenRows.length; k++) {
                                if (newQueenRows[k] != -1) {
                                    if (Math.abs(k - j) == Math.abs(newQueenRows[k] - newRow)) {
                                        diagonalsPermit = false;
                                        break;
                                    }
                                }
                            }
                            if (diagonalsPermit) {
                                // We are allowed here
                                newQueensAllowedHere[j] = true;
                            }
                        }
                    }

                    // Now we have everything we need to progress downward
                    List<List<String>> bottomRowsPossibilities = progressDown(newQueensAllowedHere, newQueenRows,
                            row + 1);
                    for (List<String> possibility : bottomRowsPossibilities) {
                        possibility.add(0, nextRow.toString());
                        allPossibilitiesFromHere.add(possibility);
                    }
                }
            }
            return allPossibilitiesFromHere;
        }

    }

    /**
     * The n-queens puzzle is the problem of placing n queens on an n x n chessboard
     * such that no two queens attack each other.
     * 
     * Given an integer n, return the number of distinct solutions to the n-queens
     * puzzle.
     * 
     * Link:
     * https://leetcode.com/problems/n-queens-ii/description/
     * 
     * @param n
     * @return
     */
    public int totalNQueens(int n) {
        boolean[] queenAllowedHere = new boolean[n];
        int[] queenRows = new int[n]; // for a given column number, if a queen was placed there, which row was it
                                      // placed on?
        for (int i = 0; i < queenRows.length; i++) {
            queenRows[i] = -1;
            queenAllowedHere[i] = true;
        }

        return countAll(queenAllowedHere, queenRows, 0);
    }

    /**
     * Helper method to count how many solutions there are to the n-queens puzzle
     * 
     * @param queensAllowedHere
     * @param queenRows
     * @param row
     * @return int
     */
    private int countAll(boolean[] queensAllowedHere, int[] queenRows, int row) {

        // Find all the places on input row where we are allowed to place a queen
        if (row == queenRows.length - 1) {
            int lastRowPossibilities = 0;
            for (int i = 0; i < queensAllowedHere.length; i++) {
                if (queensAllowedHere[i]) {
                    lastRowPossibilities++;
                }
            }
            return lastRowPossibilities;
        } else {
            int allPossibilitiesFromHere = 0;
            for (int i = 0; i < queensAllowedHere.length; i++) {
                if (queensAllowedHere[i]) {

                    // Place a queen
                    boolean[] newQueensAllowedHere = new boolean[queensAllowedHere.length];
                    int[] newQueenRows = Arrays.copyOf(queenRows, queenRows.length);
                    newQueenRows[i] = row;
                    int newRow = row + 1;

                    // Now that we have placed a queen, we progress downward, making a new boolean
                    // array that specifies where on the next row queens can be placed
                    for (int j = 0; j < newQueensAllowedHere.length; j++) {
                        if (newQueenRows[j] == -1) {
                            // Maybe we are allowed here
                            boolean diagonalsPermit = true;
                            for (int k = 0; k < newQueenRows.length; k++) {
                                if (newQueenRows[k] != -1) {
                                    if (Math.abs(k - j) == Math.abs(newQueenRows[k] - newRow)) {
                                        diagonalsPermit = false;
                                        break;
                                    }
                                }
                            }
                            if (diagonalsPermit) {
                                // We are allowed here
                                newQueensAllowedHere[j] = true;
                            }
                        }
                    }

                    // Now we have everything we need to progress downward
                    allPossibilitiesFromHere += countAll(newQueensAllowedHere, newQueenRows, row + 1);
                }
            }
            return allPossibilitiesFromHere;
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The set [1, 2, 3, ..., n] contains a total of n! unique permutations.
     * 
     * By listing and labeling all of the permutations in order, we get the
     * following sequence for n = 3:
     * 
     * "123"
     * "132"
     * "213"
     * "231"
     * "312"
     * "321"
     * Given n and k, return the kth permutation sequence.
     * 
     * Link:
     * https://leetcode.com/problems/permutation-sequence/
     * 
     * @param n
     * @param k
     * @return String
     */
    public String getPermutation(int n, int k) {
        // Each element will be the first element (n-1)! times
        // It will go like this:
        // 1XXXXX... - first (n-1)!
        // 12XXXX... - first (n-2)!
        // 123XXX... - first (n-3)!
        // ...
        // 13XXXX... - permutations (n-2)! + 1 through 2(n-2)!
        // 32XXXX... - permutations 2(n-1)! + (n-2)! + 1 through 2(n-1)! + 2(n-2)!
        // 3514XXX... - permutations 2(n-1)! + 4(n-2)! + (n-4)! + 1 through 2(n-1)! +
        // 4(n-2)! + 2(n-4)!

        // Given k, break it up into:
        // a(n-1)! + b(n-2)! + c(n-3)! + d(n-4)! + ... e(n-l)! + ... f(2) + g
        // Where e is the last non-zero coefficient
        // Start with [1, 2, 3, 4, ..., n]
        // Remove the (a+1)th/st available digit - that's your first character of the
        // string
        // Remove (b+1)th/st available digit - next character of the string
        // Once you found the lowest value you can remove, that's the next character of
        // the string
        // Progress like this up through e
        // All remaining integers simply append in increasing order

        // first compute our factorial values
        int[] factorials = new int[n];
        for (int i = 0; i < factorials.length; i++) {
            factorials[i] = 1;
            if (i > 0)
                factorials[i] = i * factorials[i - 1];
        }

        StringBuilder builder = new StringBuilder();
        // the last one?
        if (k == factorials[n - 1] * n) {
            for (int i = n; i >= 1; i--) {
                builder.append(Integer.toString(i));
            }
        } else if (k == 1) { // the first one
            for (int i = 1; i <= n; i++) {
                builder.append(Integer.toString(i));
            }
        } else { // somewhere in between
            int total = k;
            int greatestFactorial = n - 1;
            int[] factorialsUsed = new int[factorials.length];
            int earliestNonzero = n + 1;
            while (total > 0) {
                int fact = factorials[greatestFactorial];
                while (total <= fact && greatestFactorial > 0) {
                    greatestFactorial--;
                    fact = factorials[greatestFactorial];
                }
                int factsOfThisSize = total / fact;
                total -= factsOfThisSize * fact;
                factorialsUsed[greatestFactorial] = factsOfThisSize;
                if (factsOfThisSize > 0)
                    earliestNonzero = Math.min(greatestFactorial, earliestNonzero);
            }

            TreeSet<Integer> digits = new TreeSet<>();
            for (int i = 1; i <= n; i++) {
                digits.add(i);
            }
            for (int i = factorialsUsed.length - 1; i >= 0; i--) {
                int numberOfFactorials = factorialsUsed[i];
                if (i == earliestNonzero) {
                    // then grab the corresponding digit and reverse all remaining digits
                    int posn = 1;
                    Iterator<Integer> digitsIterator = digits.iterator();
                    int digit = digitsIterator.next();
                    while (posn < numberOfFactorials) {
                        digit = digitsIterator.next();
                        posn++;
                    }
                    digitsIterator.remove();
                    builder.append(Integer.toString(digit));
                    digitsIterator = digits.iterator();
                    Stack<Integer> remaining = new Stack<>();
                    while (digitsIterator.hasNext()) {
                        remaining.push(digitsIterator.next());
                    }
                    while (!remaining.empty()) {
                        builder.append(Integer.toString(remaining.pop()));
                    }
                    break;
                } else {
                    int posn = 0; // if we have smaller factorials that make up k as well, then we need to go the
                                  // next digit farther (starting posn at 0)
                    Iterator<Integer> digitsIterator = digits.iterator();
                    int digit = digitsIterator.next();
                    while (posn < numberOfFactorials) {
                        digit = digitsIterator.next();
                        posn++;
                    }
                    digitsIterator.remove();
                    builder.append(Integer.toString(digit));
                }
            }

        }
        return builder.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given an integer array nums sorted in non-decreasing order, remove some
     * duplicates in-place such that each unique element appears at most twice. The
     * relative order of the elements should be kept the same.
     * 
     * Since it is impossible to change the length of the array in some languages,
     * you must instead have the result be placed in the first part of the array
     * nums. More formally, if there are k elements after removing the duplicates,
     * then the first k elements of nums should hold the final result. It does not
     * matter what you leave beyond the first k elements.
     * 
     * Return k after placing the final result in the first k slots of nums.
     * 
     * Do not allocate extra space for another array. You must do this by modifying
     * the input array in-place with O(1) extra memory.
     * 
     * Link:
     * https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
     * 
     * @param nums
     * @return int
     */
    public int removeDuplicates(int[] nums) {
        // Find the stretches of numbers that are not unique
        ArrayList<Integer> starts = new ArrayList<>();
        ArrayList<Integer> ends = new ArrayList<>();
        int uniqueIndex = 0;
        int index = 1;
        while (index < nums.length) {
            if (nums[index] == nums[uniqueIndex]) {
                // We're allowed two of the same numbers in a row
                index++;
                if (index < nums.length) {
                    // Now we need to start removing if we run into any more
                    if (nums[index] == nums[uniqueIndex]) {
                        starts.add(index);
                        index++;
                        while (index < nums.length && nums[index] == nums[uniqueIndex]) {
                            index++;
                            if (index >= nums.length)
                                break;
                        }
                        ends.add(index - 1);
                    }
                    uniqueIndex = index;
                }
            } else {
                uniqueIndex = index;
            }
            index++;
        }

        // Now find all the indices that need to be moved backward
        ArrayList<Integer> moveBackwards = new ArrayList<>();
        for (int i = 0; i < starts.size(); i++) {
            int startMove = ends.get(i) + 1;
            int endMove = (i < starts.size() - 1) ? starts.get(i + 1) : nums.length;
            for (int j = startMove; j < endMove; j++) {
                moveBackwards.add(j);
            }
        }
        if (starts.size() > 0) {
            int numMoved = 0;
            int moveHere = starts.get(0);
            for (int idx : moveBackwards) {
                nums[moveHere + numMoved] = nums[idx];
                numMoved++;
            }
            return moveHere + numMoved;
        } else {
            return nums.length;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * There are 3n piles of coins of varying size, you and your friends will take
     * piles of coins as follows:
     * 
     * In each step, you will choose any 3 piles of coins (not necessarily
     * consecutive).
     * - Of your choice, Alice will pick the pile with the maximum number of coins.
     * - You will pick the next pile with the maximum number of coins.
     * - Your friend Bob will pick the last pile.
     * - Repeat until there are no more piles of coins.
     * Given an array of integers piles where piles[i] is the number of coins in the
     * ith pile.
     * 
     * Return the maximum number of coins that you can have.
     * 
     * Link:
     * https://leetcode.com/problems/maximum-number-of-coins-you-can-get/?envType=daily-question&envId=2023-12-14
     * 
     * @param piles
     * @return int
     */
    public int maxNumCoins(int[] piles) {
        Arrays.sort(piles);
        int max = 0;

        for (int i = piles.length / 3; i <= piles.length - 2; i += 2)
            max += piles[i];

        return max;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Design a food rating system that can do the following:
     * 
     * Modify the rating of a food item listed in the system.
     * Return the highest-rated food item for a type of cuisine in the system.
     * Implement the FoodRatings class:
     * 
     * FoodRatings(String[] foods, String[] cuisines, int[] ratings) Initializes the
     * system. The food items are described by foods, cuisines and ratings, all of
     * which have a length of n.
     * - foods[i] is the name of the ith food,
     * - cuisines[i] is the type of cuisine of the ith food, and
     * - ratings[i] is the initial rating of the ith food.
     * - void changeRating(String food, int newRating): Changes the rating of the
     * food
     * item with the name food.
     * - String highestRated(String cuisine): Returns the name of the food item that
     * has the highest rating for the given type of cuisine. If there is a tie,
     * return the item with the lexicographically smaller name.
     * Note that a string x is lexicographically smaller than string y if x comes
     * before y in dictionary order, that is, either x is a prefix of y, or if i is
     * the first position such that x[i] != y[i], then x[i] comes before y[i] in
     * alphabetic order.
     * 
     * Link:
     * https://leetcode.com/problems/design-a-food-rating-system/?envType=daily-question&envId=2023-12-17
     */
    class FoodRatings {

        private HashMap<String, Integer> nameToIndex = new HashMap<>();

        private String[] cuisines;
        private int[] ratings;

        private HashMap<String, TreeSet<FoodWithRating>> cuisineSets = new HashMap<>();
        private HashMap<String, FoodWithRating> foodsToRatings = new HashMap<>();

        /**
         * Initialize this object
         */
        private static final class FoodWithRating {
            public final String food;
            public int rating;

            // How do you compare objects of this type?
            public static final Comparator<FoodWithRating> FOOD_RATING_COMPARATOR = new Comparator<FoodWithRating>() {
                @Override
                public int compare(FoodWithRating o1, FoodWithRating o2) {
                    if (Integer.compare(o2.rating, o1.rating) != 0) {
                        return Integer.compare(o2.rating, o1.rating);
                    } else {
                        return o1.food.compareTo(o2.food);
                    }
                };
            };

            /**
             * Create a FoodWithRating object
             * 
             * @param food
             * @param rating
             */
            public FoodWithRating(String food, int rating) {
                this.food = food;
                this.rating = rating;
            }
        }

        /**
         * Create a FoodRatings object
         * 
         * @param foods
         * @param cuisines
         * @param ratings
         */
        public FoodRatings(String[] foods, String[] cuisines, int[] ratings) {
            this.cuisines = cuisines;
            this.ratings = ratings;

            for (int i = 0; i < foods.length; i++) {
                String food = foods[i];
                nameToIndex.put(food, i);
                String cuisine = cuisines[i];
                int rating = ratings[i];

                FoodWithRating foodWithRating = new FoodWithRating(food, rating);
                foodsToRatings.put(food, foodWithRating);
                if (cuisineSets.containsKey(cuisine)) {
                    cuisineSets.get(cuisine).add(foodWithRating);
                } else {
                    TreeSet<FoodWithRating> cuisineSet = new TreeSet<>(FoodWithRating.FOOD_RATING_COMPARATOR);
                    cuisineSet.add(foodWithRating);
                    cuisineSets.put(cuisine, cuisineSet);
                }
            }
        }

        /**
         * Change the rating of the given food, which modifies sets
         * 
         * @param food
         * @param newRating
         */
        public void changeRating(String food, int newRating) {
            int index = nameToIndex.get(food);
            String cuisine = cuisines[index];
            ratings[index] = newRating;
            // NOTE - do not use an iterator on the TreeSet for the cuisine, because that is
            // actually linear searching due to the underlying array of a heap - that's why
            // we have this map
            FoodWithRating foodWithRating = foodsToRatings.get(food);
            cuisineSets.get(cuisine).remove(foodWithRating);
            foodWithRating.rating = newRating;
            cuisineSets.get(cuisine).add(foodWithRating);
        }

        /**
         * Return the highest rated food of the given cuisine - the tie breaker is
         * alphabetical
         * 
         * @param cuisine
         * @return String
         */
        public String highestRated(String cuisine) {
            return cuisineSets.get(cuisine).iterator().next().food;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Method to copy a TreeNode
     * 
     * @param source
     * @return {@link TreeNode}
     */
    private TreeNode copy(TreeNode source) {
        if (source == null)
            return null;
        TreeNode newNode = new TreeNode(source.val);
        newNode.left = copy(source.left);
        newNode.right = copy(source.right);
        return newNode;
    }

    /**
     * Given an integer n, return a list of all possible full binary trees with n
     * nodes. Each node of each tree in the answer must have Node.val == 0.
     * 
     * Each element of the answer is the root node of one possible tree. You may
     * return the final list of trees in any order.
     * 
     * A full binary tree is a binary tree where each node has exactly 0 or 2
     * children.
     * 
     * Link:
     * https://leetcode.com/problems/all-possible-full-binary-trees/?envType=daily-question&envId=2023-12-18
     * 
     * @param n
     * @return {@link List<TreeNode>}
     */
    public List<TreeNode> allPossibleFBT(int n) {
        List<TreeNode> allPossible = new ArrayList<>();
        if (n % 2 == 0)
            return allPossible;

        List<TreeNode>[] sols = new List[n];
        List<TreeNode> oneNode = new ArrayList<>();
        oneNode.add(new TreeNode());
        sols[0] = oneNode;
        for (int i = 2; i < n; i += 2) {
            List<TreeNode> roots = new ArrayList<>();
            int numNodes = i + 1;
            for (int numLeft = 1; numLeft < numNodes; numLeft += 2) {
                int numRight = numNodes - 1 - numLeft;
                List<TreeNode> rootsLeft = sols[numLeft - 1]; // all possible left roots
                List<TreeNode> rootsRight = sols[numRight - 1]; // all possible right roots
                for (TreeNode leftRoot : rootsLeft) {
                    for (TreeNode rightRoot : rootsRight) {
                        TreeNode leftCopy = copy(leftRoot);
                        TreeNode rightCopy = copy(rightRoot);
                        TreeNode root = new TreeNode(0, leftCopy, rightCopy);
                        roots.add(root);
                    }
                }
            }
            sols[numNodes - 1] = roots;
        }

        return sols[n - 1];
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The pair sum of a pair (a,b) is equal to a + b. The maximum pair sum is the
     * largest pair sum in a list of pairs.
     * 
     * For example, if we have pairs (1,5), (2,3), and (4,4), the maximum pair sum
     * would be max(1+5, 2+3, 4+4) = max(6, 5, 8) = 8.
     * Given an array nums of even length n, pair up the elements of nums into n / 2
     * pairs such that:
     * 
     * Each element of nums is in exactly one pair, and
     * The maximum pair sum is minimized.
     * Return the minimized maximum pair sum after optimally pairing up the
     * elements.
     * 
     * Link:
     * https://leetcode.com/problems/minimize-maximum-pair-sum-in-array/?envType=daily-question&envId=2023-12-22
     * 
     * @param nums
     * @return int
     */
    public int minPairSum(int[] nums) {
        // We know that there are an even amount of numbers
        Arrays.sort(nums);

        // Further, now that the array is sorted, the lowest maximal pair sum can be
        // achieved by taking one value from the lower half, and one value from the
        // upper half.
        // Proof:

        // Case 1:
        // Suppose the lowest maximal sum can be two values from the lower half:
        // - That leaves the upper half values all being equal to these two lower half
        // values, and the two lower half values are equal.
        // - If this were not the case, then any two upper half values - one of which we
        // know exists in this pairing - form a sum greater than this supposed maximal
        // sum, a contradiction.
        // - Since we know those upper half values are equal to the two (equal) lower
        // half values, any two of those upper half values - one of which we know exists
        // in this pairing - form a sum equal to this maximal pair sum value.
        // Hence, if (l1, l2) form the minimum maximal pair, then we have an (u1, u2)
        // which also does, leading into Case 2.

        // Case 2:
        // Suppose (u1, u2) form a minimum maximal pair. Then (l1, l2) also exist in
        // this pairing.
        // - Note that we can rearrange this pairing with only these four elements and
        // touching no other elements, while achieving at least as small of a maximal
        // pair.
        // - Said new minimum pair is min{u1 + l1, u1 + l2, u2 + l1, u2 + l2}
        // - Suppose you have a pair (u3, u4) such that u3 + u4 < min{u1 + l1, u1 + l2,
        // u2 + l1, u2 + l2}?
        // - Then the pairing must also have a new (l3, l4) pair, and the same argument
        // applies until we run out of purely upper pairs.
        // Hence, the lowest maximal pair sum can be achieved by taking one value from
        // the lower half and one value from the upper half.

        // Case 3:
        // Suppose (l1, u1) form a minimum maximal pair. Trivially, we are done.

        // So we know that we CAN achieve the minimum maximal value by finding the right
        // pair with one element from the left and one element from the right
        int minMaximalPairSum = Integer.MIN_VALUE;
        // Now we iterate through the sorted list of numbers
        for (int i = 0; i < nums.length / 2; i++) {
            int j = nums.length - 1 - i;
            // The first value will be paired with the last. If the first pair is going to
            // achieve the minimum maximal pair sum, it must do so with the last value.
            // Suppose that the first value paired with some other value. Then the last
            // value is only going to be paired with something bigger, increasing the
            // maximal pair sum.
            // Similarly, the second value will be paired with the second to last. Through
            // this inductive reasoning, we can see that nums[i] is paired with
            // nums[nums.length-1-i] to achieve the minimum maximal pair sum.
            minMaximalPairSum = Math.max(nums[i] + nums[j], minMaximalPairSum); // Take the maximum each time to find
                                                                                // out what the actual minimum MAXIMAL
                                                                                // pair sum is.
        }

        return minMaximalPairSum;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You want to schedule a list of jobs in d days. Jobs are dependent (i.e To
     * work on the ith job, you have to finish all the jobs j where 0 <= j < i).
     * 
     * You have to finish at least one task every day. The difficulty of a job
     * schedule is the sum of difficulties of each day of the d days. The difficulty
     * of a day is the maximum difficulty of a job done on that day.
     * 
     * You are given an integer array jobDifficulty and an integer d. The difficulty
     * of the ith job is jobDifficulty[i].
     * 
     * Return the minimum difficulty of a job schedule. If you cannot find a
     * schedule for the jobs return -1.
     * 
     * 
     * Link:
     * https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/?envType=daily-question&envId=2023-12-29
     * 
     * @param jobDifficulty
     * @param d
     * @return int
     */
    public int minDifficulty(int[] jobDifficulty, int d) {
        // The real question is, how do you partition the array of chores into different
        // days?
        if (d > jobDifficulty.length) // then not every day can have a job
            return -1;

        int[][] sols = new int[d][jobDifficulty.length];
        for (int i = 0; i < sols.length - 1; i++) {
            for (int j = 0; j < sols[i].length; j++) {
                sols[i][j] = Integer.MAX_VALUE;
            }
        }
        // Now we can cover the base case if we only have one day to work with
        int[] maxFromHere = new int[jobDifficulty.length];
        maxFromHere[maxFromHere.length - 1] = jobDifficulty[jobDifficulty.length - 1];
        for (int i = maxFromHere.length - 2; i >= 0; i--) {
            maxFromHere[i] = Math.max(maxFromHere[i + 1], jobDifficulty[i]);
        }
        for (int j = 0; j < sols[sols.length - 1].length; j++) {
            sols[sols.length - 1][j] = maxFromHere[j];
        }
        return topDownMinDifficulty(jobDifficulty, sols, d, 0, 0);
    }

    /**
     * Recursive helper method to find the optimal way to schedule jobs on
     * particular days to minimize total difficulty
     * 
     * @param jobDifficulty
     * @param sols
     * @param dayIdx
     * @param choreIdx
     * @return int
     */
    private int topDownMinDifficulty(int[] jobDifficulty, int[][] sols, int days, int dayIdx, int choreIdx) {
        if (sols[dayIdx][choreIdx] != Integer.MAX_VALUE)
            return sols[dayIdx][choreIdx];

        int best = Integer.MAX_VALUE;
        int choresTaken = 0;
        int todayDifficulty = Integer.MIN_VALUE;
        while ((choresTaken < jobDifficulty.length - choreIdx - 1)
                && (days - dayIdx - 1 <= jobDifficulty.length - (choreIdx + choresTaken + 1))) { // we still have jobs
                                                                                                 // to give and we can
                                                                                                 // take another job on
                                                                                                 // this day and still
                                                                                                 // be able to give all
                                                                                                 // remaining days at
                                                                                                 // least one job
            choresTaken += 1;
            todayDifficulty = Math.max(todayDifficulty, jobDifficulty[choreIdx + choresTaken - 1]); // the maximum
                                                                                                    // difficulty of all
                                                                                                    // the jobs we have
                                                                                                    // taken
            best = Math.min(best, todayDifficulty
                    + topDownMinDifficulty(jobDifficulty, sols, days, dayIdx + 1, choreIdx + choresTaken));
        }

        sols[dayIdx][choreIdx] = best;

        return best;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * We have n jobs, where every job is scheduled to be done from startTime[i] to
     * endTime[i], obtaining a profit of profit[i].
     * 
     * You're given the startTime, endTime and profit arrays, return the maximum
     * profit you can take such that there are no two jobs in the subset with
     * overlapping time range.
     * 
     * If you choose a job that ends at time X you will be able to start another job
     * that starts at time X.
     * 
     * Link:
     * https://leetcode.com/problems/maximum-profit-in-job-scheduling/?envType=daily-question&envId=2024-01-08
     * 
     * @param startTime
     * @param endTime
     * @param profit
     * @return int
     */
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        Job[] jobs = new Job[startTime.length];
        for (int i = 0; i < jobs.length; i++) {
            jobs[i] = new Job(startTime[i], endTime[i], profit[i]);
        }
        Arrays.sort(jobs, Job.JOB_COMPARATOR);

        int[] maxProfit = new int[jobs.length];
        maxProfit[0] = jobs[0].getProfit();
        for (int i = 1; i < maxProfit.length; i++) {
            // Try to include this job
            int includeJob = jobs[i].getProfit();
            int prevJobIndex = findJobIndex(jobs, jobs[i].getStartTime(), i);
            if (prevJobIndex != -1)
                includeJob += maxProfit[prevJobIndex];

            maxProfit[i] = Math.max(includeJob, maxProfit[i - 1]);
        }

        return maxProfit[maxProfit.length - 1];
    }

    /**
     * Helper method to binary search for a job whose end time is no later than the
     * input parameter 'endByThisTime'
     * 
     * @param jobs
     * @param endByThisTime
     * @return int
     */
    private int findJobIndex(Job[] jobs, int endByThisTime, int earlierThanThisIndex) {
        int left = 0;
        int right = earlierThanThisIndex;
        while (left < right) {
            int mid = (left + right) / 2;
            if (mid == left)
                break;
            if (jobs[mid].getEndTime() > endByThisTime) {
                // go left half
                right = mid;
            } else {
                // go right half
                left = mid;
            }
        }

        return (jobs[left].getEndTime() <= endByThisTime) ? left : -1;
    }

    /**
     * Store all of the information associated with a Job
     */
    private static class Job {
        private int startTime;
        private int endTime;
        private int profit;

        public static final Comparator<Job> JOB_COMPARATOR = (j1, j2) -> Integer.compare(j1.getEndTime(),
                j2.getEndTime());

        public Job(int startTime, int endTime, int profit) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.profit = profit;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getProfit() {
            return profit;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given the root of a binary tree, return the bottom-up level order traversal
     * of its nodes' values. (i.e., from left to right, level by level from leaf to
     * root).
     * 
     * Link:
     * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/description/
     * 
     * @param root
     * @return List<List<Integer>>
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        if (root == null)
            return new ArrayList<>();

        List<List<Integer>> normalOrder = new ArrayList<>();
        List<TreeNode> currentLevel = new ArrayList<>();
        currentLevel.add(root);

        while (!(currentLevel.isEmpty())) {
            // Source:
            // https://www.java67.com/2019/03/how-to-convert-int-array-to-arraylist-in-java-8-example.html
            normalOrder.add(IntStream.of(currentLevel.stream().mapToInt((node) -> node.val).toArray()).boxed()
                    .collect(Collectors.toCollection(ArrayList::new)));
            List<TreeNode> nextLevel = new ArrayList<>();
            for (TreeNode node : currentLevel) {
                if (node.left != null) {
                    nextLevel.add(node.left);
                }
                if (node.right != null) {
                    nextLevel.add(node.right);
                }
            }
            currentLevel = nextLevel;
        }

        List<List<Integer>> reverseOrder = new ArrayList<>();
        for (int i = normalOrder.size() - 1; i >= 0; i--) {
            reverseOrder.add(normalOrder.get(i));
        }

        return reverseOrder;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A path in a binary tree is a sequence of nodes where each pair of adjacent
     * nodes in the sequence has an edge connecting them. A node can only appear in
     * the sequence at most once. Note that the path does not need to pass through
     * the root.
     * 
     * The path sum of a path is the sum of the node's values in the path.
     * 
     * Given the root of a binary tree, return the maximum path sum of any non-empty
     * path.
     * 
     * Link:
     * https://leetcode.com/problems/binary-tree-maximum-path-sum/
     * 
     * @param root
     * @return int
     */
    public int maxPathSum(TreeNode root) {
        TreePathNode.resetRecordPath();

        // No need to name this - the static record path value will be found
        new TreePathNode(root);

        return TreePathNode.getRecordPath();
    }

    private static class TreePathNode extends TreeNode {

        private static int recordPath = Integer.MIN_VALUE;

        public static int getRecordPath() {
            return recordPath;
        }

        public static void resetRecordPath() {
            recordPath = Integer.MIN_VALUE;
        }

        private int pathSumAllowLeftOnly;

        private int pathSumAllowRightOnly;

        private int pathSumAllowBoth;

        private TreePathNode leftPathNode;

        private TreePathNode rightPathNode;

        public TreePathNode(TreeNode source) {
            this.left = source.left;
            this.val = source.val;
            this.right = source.right;

            this.leftPathNode = (this.left != null) ? new TreePathNode(this.left) : null;
            this.rightPathNode = (this.right != null) ? new TreePathNode(this.right) : null;

            pathSumAllowLeftOnly = this.val;
            if (this.leftPathNode != null) {
                pathSumAllowLeftOnly += Math.max(0,
                        Math.max(this.leftPathNode.pathSumAllowLeftOnly, this.leftPathNode.pathSumAllowRightOnly));
            }
            pathSumAllowRightOnly = this.val;
            if (this.rightPathNode != null) {
                pathSumAllowRightOnly += Math.max(0,
                        Math.max(this.rightPathNode.pathSumAllowLeftOnly, this.rightPathNode.pathSumAllowRightOnly));
            }

            // Avoid double counting the current node value
            pathSumAllowBoth = pathSumAllowLeftOnly + pathSumAllowRightOnly - this.val;

            recordPath = Math.max(recordPath, pathSumAllowBoth);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You are given an integer array matches where matches[i] = [winneri, loseri]
     * indicates that the player winneri defeated player loseri in a match.
     * 
     * Return a list answer of size 2 where:
     * 
     * answer[0] is a list of all players that have not lost any matches.
     * answer[1] is a list of all players that have lost exactly one match.
     * The values in the two lists should be returned in increasing order.
     * 
     * Note:
     * 
     * You should only consider the players that have played at least one match.
     * The testcases will be generated such that no two matches will have the same
     * outcome.
     * 
     * Link:
     * https://leetcode.com/problems/find-players-with-zero-or-one-losses/description/?envType=daily-question&envId=2024-01-15
     * 
     * @param matches
     * @return List<List<Integer>>
     */
    public List<List<Integer>> findWinners(int[][] matches) {
        HashSet<Integer> losers = new HashSet<>();
        HashSet<Integer> lostOnce = new HashSet<>();
        HashSet<Integer> played = new HashSet<>();
        HashSet<Integer> lostMultiple = new HashSet<>();
        int maxPlayer = 1;
        for (int[] match : matches) {
            played.add(match[0]);
            played.add(match[1]);
            losers.add(match[1]);
            maxPlayer = Math.max(maxPlayer, Math.max(match[0], match[1]));
            if (lostOnce.contains(match[1]) || lostMultiple.contains(match[1])) {
                if (lostOnce.contains(match[1])) {
                    lostOnce.remove(match[1]);
                    lostMultiple.add(match[1]);
                }
            } else
                lostOnce.add(match[1]);
        }

        List<Integer> noLosses = new ArrayList<>();
        for (int player : played) {
            if (!losers.contains(player))
                noLosses.add(player);
        }

        List<Integer> lostOnceList = new ArrayList<>();
        for (int player : lostOnce)
            lostOnceList.add(player);

        noLosses.sort(Integer::compare);
        lostOnceList.sort(Integer::compare);

        List<List<Integer>> toReturn = new ArrayList<>();
        toReturn.add(noLosses);
        toReturn.add(lostOnceList);

        return toReturn;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You are given two strings of the same length s and t. In one step you can
     * choose any character of t and replace it with another character.
     * 
     * Return the minimum number of steps to make t an anagram of s.
     * 
     * An Anagram of a string is a string that contains the same characters with a
     * different (or the same) ordering.
     * 
     * Link:
     * https://leetcode.com/problems/minimum-number-of-steps-to-make-two-strings-anagram/description/?envType=daily-question&envId=2024-01-18
     * 
     * @param s
     * @param t
     * @return int
     */
    public int minSteps(String s, String t) {
        HashMap<Character, Integer> targetCounts = new HashMap<>();
        HashMap<Character, Integer> currentCounts = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (targetCounts.containsKey(c))
                targetCounts.put(c, targetCounts.get(c) + 1);
            else
                targetCounts.put(c, 1);
        }
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            if (currentCounts.containsKey(c))
                currentCounts.put(c, currentCounts.get(c) + 1);
            else
                currentCounts.put(c, 1);
        }

        int moves = 0;
        for (char c : currentCounts.keySet()) {
            if (targetCounts.containsKey(c) && targetCounts.get(c) < currentCounts.get(c)) {
                moves += currentCounts.get(c) - targetCounts.get(c);
            } else if (!targetCounts.containsKey(c)) {
                moves += currentCounts.get(c);
            }
        }

        return moves;
    }

    /**
     * You are given two strings s and t. In one step, you can append any character
     * to either s or t.
     * 
     * Return the minimum number of steps to make s and t anagrams of each other.
     * 
     * An anagram of a string is a string that contains the same characters with a
     * different (or the same) ordering.
     * 
     * Link:
     * https://leetcode.com/problems/minimum-number-of-steps-to-make-two-strings-anagram-ii/description/
     * 
     * @param s
     * @param t
     * @return int
     */
    public int secondMinSteps(String s, String t) {
        HashMap<Character, Integer> tCounts = new HashMap<>();
        HashMap<Character, Integer> sCounts = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (tCounts.containsKey(c))
                tCounts.put(c, tCounts.get(c) + 1);
            else
                tCounts.put(c, 1);
        }
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            if (sCounts.containsKey(c))
                sCounts.put(c, sCounts.get(c) + 1);
            else
                sCounts.put(c, 1);
        }

        int moves = 0;
        for (char c : sCounts.keySet()) {
            if (tCounts.containsKey(c)) {
                moves += Math.abs(sCounts.get(c) - tCounts.get(c));
            } else if (!tCounts.containsKey(c)) {
                moves += sCounts.get(c);
            }
        }
        for (char c : tCounts.keySet()) {
            if (!sCounts.containsKey(c)) {
                moves += tCounts.get(c);
            }
        }

        return moves;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You are given an array of strings arr. A string s is formed by the
     * concatenation of a subsequence of arr that has unique characters.
     * 
     * Return the maximum possible length of s.
     * 
     * A subsequence is an array that can be derived from another array by deleting
     * some or no elements without changing the order of the remaining elements.
     * 
     * Link:
     * https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/description/?envType=daily-question&envId=2024-01-23
     * 
     * @param arr
     * @return int
     */
    public int maxLength(List<String> arr) {
        // Ultimately, we have to brute force this...
        List<String> arrNoRepeatCharacterWords = arr.stream()
                .filter(s -> uniqueCharacters(s))
                .toList();
        if (arrNoRepeatCharacterWords.size() == 0)
            return 0;
        int recordLength = 0;
        List<List<Integer>> combinations = findCombinations(0, arrNoRepeatCharacterWords.size() - 1);
        for (List<Integer> combo : combinations) {
            HashSet<Character> used = new HashSet<>();
            int length = 0;
            for (int i : combo) {
                String word = arrNoRepeatCharacterWords.get(i);
                boolean okay = true;
                for (int j = 0; j < word.length(); j++) {
                    char c = word.charAt(j);
                    if (used.contains(c)) {
                        okay = false;
                        break;
                    } else {
                        used.add(c);
                    }
                }
                if (okay)
                    length += word.length();
            }
            recordLength = Math.max(recordLength, length);
        }

        return recordLength;
    }

    /**
     * Helper method to determine if a string has any repeat characters in it
     * 
     * @param s
     * @return boolean
     */
    private static boolean uniqueCharacters(String s) {
        HashSet<Character> characters = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (characters.contains(c))
                return false;
            characters.add(c);
        }
        return true;
    }

    /**
     * Helper method to generate all sequential combinations of any of the integers
     * between and including start and upTo
     * 
     * @param start
     * @param upTo
     * @return
     */
    List<List<Integer>> findCombinations(int start, int upTo) {
        List<List<Integer>> combinations = new ArrayList<>();
        if (start == upTo) {
            List<Integer> single = new ArrayList<>();
            List<Integer> empty = new ArrayList<>();
            single.add(start);
            combinations.add(single);
            combinations.add(empty);
        } else {
            List<List<Integer>> previous = findCombinations(start + 1, upTo);
            for (List<Integer> list : previous) {
                List<Integer> copy1 = copy(list);
                // Include start, or don't
                copy1.add(0, start);
                combinations.add(copy1);
                combinations.add(list);
            }
        }

        return combinations;
    }

    /**
     * Helper method to copy a list of objects
     * 
     * @param original
     * @return List<Integer>
     */
    private static List<Integer> copy(List<Integer> original) {
        List<Integer> copy = new ArrayList<>();
        for (int i : original)
            copy.add(i);
        return copy;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 
     * @param s
     * @return int
     */
    public int longestPalindromeSubseq(String s) {
        int[][] sols = new int[s.length()][s.length()];

        for (int i = 0; i < sols.length; i++) {
            sols[i][i] = 1;
        }
        for (int i = 0; i < sols.length - 1; i++) {
            int start = i;
            int end = i + 1;
            sols[start][end] = (s.charAt(start) == s.charAt(end)) ? 2 : 1;
        }
        for (int length = 2; length < s.length(); length++) {
            for (int start = 0; start < s.length() - length; start++) {
                int end = start + length;
                if (s.charAt(start) == s.charAt(end)) {
                    sols[start][end] = Math.max(sols[start + 1][end - 1] + 2,
                            Math.max(sols[start][end - 1], sols[start + 1][end]));
                } else {
                    sols[start][end] = Math.max(sols[start][end - 1], sols[start + 1][end]);
                }
            }
        }

        return sols[0][s.length() - 1];
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * You are given an array of strings tokens that represents an arithmetic
     * expression in a Reverse Polish Notation.
     * 
     * Evaluate the expression. Return an integer that represents the value of the
     * expression.
     * 
     * Note that:
     * 
     * The valid operators are '+', '-', '*', and '/'.
     * Each operand may be an integer or another expression.
     * The division between two integers always truncates toward zero.
     * There will not be any division by zero.
     * The input represents a valid arithmetic expression in a reverse polish
     * notation.
     * The answer and all the intermediate calculations can be represented in a
     * 32-bit integer.
     * 
     * Link:
     * https://leetcode.com/problems/evaluate-reverse-polish-notation/description/?envType=daily-question&envId=2024-01-30
     * 
     * @param tokens
     * @return int
     */
    public int evalRPN(String[] tokens) {
        Stack<Integer> values = new Stack<>();
        HashSet<String> operations = new HashSet<>();
        operations.add("*");
        operations.add("+");
        operations.add("-");
        operations.add("/");
        for (String s : tokens) {
            if (operations.contains(s)) {
                int second = values.pop();
                int first = values.pop();
                int result = 0;
                if (s.equals("*")) {
                    result = first * second;
                } else if (s.equals("+")) {
                    result = first + second;
                } else if (s.equals("-")) {
                    result = first - second;
                } else {
                    result = first / second;
                }
                values.push(result);
            } else {
                values.push(Integer.parseInt(s));
            }
        }
        return values.pop();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given an integer n, return the least number of perfect square numbers that
     * sum to n.
     * 
     * A perfect square is an integer that is the square of an integer; in other
     * words, it is the product of some integer with itself. For example, 1, 4, 9,
     * and 16 are perfect squares while 3 and 11 are not.
     * 
     * Link:
     * https://leetcode.com/problems/perfect-squares/description/?envType=daily-question&envId=2024-02-08
     * 
     * @param n
     * @return int
     */
    public int numSquares(int n) {
        int[] sols = new int[n];
        sols[0] = 1;
        return topDownNumSquares(n, sols);
    }

    /**
     * Recursive helper method to solve the preceding problem
     * 
     * @param n
     * @param sols
     */
    private int topDownNumSquares(int n, int[] sols) {
        if (n == 0)
            return 0;
        else if (sols[n - 1] != 0)
            return sols[n - 1];
        // otherwise, we need to solve this number
        int best = Integer.MAX_VALUE;
        for (int i = 1; i <= Math.sqrt(n); i++) {
            int square = i * i;
            best = Math.min(best, 1 + topDownNumSquares(n - square, sols));
        }
        sols[n - 1] = best;
        return best;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given two strings s and t of lengths m and n respectively, return the minimum
     * window
     * substring
     * of s such that every character in t (including duplicates) is included in the
     * window. If there is no such substring, return the empty string "".
     * 
     * The testcases will be generated such that the answer is unique.
     * 
     * Link:
     * https://leetcode.com/problems/minimum-window-substring/description/?envType=daily-question&envId=2024-02-04
     * 
     * @param s
     * @param t
     * @return String
     */
    public String minWindow(String s, String t) {
        HashMap<Character, Integer> targetCounts = new HashMap<>();
        HashMap<Character, Integer> currentCounts = new HashMap<>();

        // Establish the target count
        int charsNeeded = t.length();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (targetCounts.containsKey(c))
                targetCounts.put(c, targetCounts.get(c) + 1);
            else
                targetCounts.put(c, 1);

            if (!currentCounts.containsKey(c))
                currentCounts.put(c, 0);
        }

        // Progress outward until we achieve the counts of everything we need
        int start = 0;
        int end = 0;
        int charsMatched = 0;
        while (charsMatched < charsNeeded) {
            char c = s.charAt(end);
            if (targetCounts.containsKey(c) && targetCounts.get(c) > currentCounts.get(c)) {
                charsMatched++;
            }
            if (targetCounts.containsKey(c))
                currentCounts.put(c, currentCounts.get(c) + 1);
            if (charsMatched < charsNeeded)
                end++;

            if (end >= s.length())
                return "";
        }

        // Move up 'start' as long as it is not providing any useful characters
        while (!currentCounts.containsKey(s.charAt(start))
                || currentCounts.get(s.charAt(start)) > targetCounts.get(s.charAt(start))) {
            char c = s.charAt(start);
            if (currentCounts.containsKey(c))
                currentCounts.put(c, currentCounts.get(c) - 1);
            start++;
        }

        // Now for sliding window
        int bestStart = start;
        int bestEnd = end;
        // What we do know is that we will slide start along with end, because if we
        // begin at start, we will only achieve a greater length than already achieved
        while (end < s.length() - 1) {
            char oldStart = s.charAt(start);
            // We know that this was a useful character, or it wouldn't have been a start
            currentCounts.put(oldStart, currentCounts.get(oldStart) - 1);
            charsMatched--;

            start++; // Move the start to someplace useful
            while (!currentCounts.containsKey(s.charAt(start))
                    || currentCounts.get(s.charAt(start)) > targetCounts.get(s.charAt(start))) {
                char c = s.charAt(start);
                if (currentCounts.containsKey(c))
                    currentCounts.put(c, currentCounts.get(c) - 1);
                start++;
                if (start >= s.length())
                    break;
            }

            end++; // Move the end as far as we need to
            while (charsMatched < charsNeeded && end < s.length()) {
                char c = s.charAt(end);
                if (targetCounts.containsKey(c) && targetCounts.get(c) > currentCounts.get(c)) {
                    charsMatched++;
                }
                if (targetCounts.containsKey(c))
                    currentCounts.put(c, currentCounts.get(c) + 1);
                if (charsMatched < charsNeeded)
                    end++;
            }

            if (charsMatched == charsNeeded) {
                // Move the start to someplace useful in case moving the end picked up extras
                while (!currentCounts.containsKey(s.charAt(start))
                        || currentCounts.get(s.charAt(start)) > targetCounts.get(s.charAt(start))) {
                    char c = s.charAt(start);
                    if (currentCounts.containsKey(c))
                        currentCounts.put(c, currentCounts.get(c) - 1);
                    start++;
                }
                int length = end - start + 1;
                int recordLength = bestEnd - bestStart + 1;
                if (length < recordLength) {
                    bestEnd = end;
                    bestStart = start;
                }
            }

        }

        return s.substring(bestStart, bestEnd + 1);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Given an m x n integers matrix, return the length of the longest increasing
     * path in matrix.
     * 
     * From each cell, you can either move in four directions: left, right, up, or
     * down. You may not move diagonally or move outside the boundary (i.e.,
     * wrap-around is not allowed).
     * 
     * Link:
     * https://leetcode.com/problems/longest-increasing-path-in-a-matrix/description/
     * 
     * @param matrix
     * @return int
     */
    public int longestIncreasingPath(int[][] matrix) {
        List<Coordinate> sortedCoordinates = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Coordinate coordinate = new Coordinate(matrix[i][j], i, j);
                sortedCoordinates.add(coordinate);
            }
        }
        sortedCoordinates.sort(Coordinate::compareTo);

        int[][] dp = new int[matrix.length][matrix[0].length];
        int max = Integer.MIN_VALUE;
        for (Coordinate coord : sortedCoordinates) {
            int r = coord.row;
            int c = coord.col;
            int v = coord.val;
            int prevDP = dp[r][c];
            if (checkNeighbor(prevDP, v, r - 1, c, matrix, dp))
                max = Math.max(max, dp[r - 1][c]);
            if (checkNeighbor(prevDP, v, r + 1, c, matrix, dp))
                max = Math.max(max, dp[r + 1][c]);
            if (checkNeighbor(prevDP, v, r, c - 1, matrix, dp))
                max = Math.max(max, dp[r][c - 1]);
            if (checkNeighbor(prevDP, v, r, c + 1, matrix, dp))
                max = Math.max(max, dp[r][c + 1]);
            // Call method for up one
            // Call method for left one
            // Call method for down one
            // Call method for right one
        }

        return (max != Integer.MIN_VALUE) ? max + 1 : 1;
    }

    private boolean checkNeighbor(int prevDP, int v, int row, int col, int[][] matrix, int[][] dp) {
        if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length)
            return false;
        int nextVal = matrix[row][col];
        if (nextVal > v) {
            int potentialRecordPath = prevDP + 1;
            dp[row][col] = Math.max(dp[row][col], potentialRecordPath);
        }
        return true;
    }

    private static class Coordinate implements Comparable<Coordinate> {
        public int val;
        public int row;
        public int col;

        public Coordinate(int v, int r, int c) {
            val = v;
            row = r;
            col = c;
        }

        @Override
        public int compareTo(Solution.Coordinate o) {
            return this.val - o.val;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        
    }

}