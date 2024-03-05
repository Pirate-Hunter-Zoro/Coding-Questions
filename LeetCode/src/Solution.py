from collections import defaultdict
import copy
import math
from TreeNode import TreeNode


class Solution(object):
    factorial_sols = [defaultdict(int) for _ in range(1001)]

    @staticmethod
    def choose(n, k):
        if k > n:
            return 0

        if Solution.factorial_sols[n][k] == 0:
            if k == 0 or k == n:
                Solution.factorial_sols[n][k] = 1
            else:
                Solution.factorial_sols[n][k] = Solution.choose(
                    n - 1, k - 1
                ) + Solution.choose(n - 1, k)

        return Solution.factorial_sols[n][k]

    mod = 1000000007

    @staticmethod
    def modularAdd(n1, n2):
        return ((n1 % Solution.mod) + (n2 % Solution.mod)) % Solution.mod

    @staticmethod
    def modularSubtract(n1, n2):
        first = n1 % Solution.mod
        second = n2 % Solution.mod
        if first >= second:
            return (first - second) % Solution.mod
        else:
            return (
                (first % Solution.mod) + (Solution.mod - (second % Solution.mod))
            ) % Solution.mod

    @staticmethod
    def modularMultiply(n1, n2):
        return ((n1 % Solution.mod) * (n2 % Solution.mod)) % Solution.mod

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def numberOfArithmeticSlices(self, nums):
        """
        Given an integer array nums, return the number of all the arithmetic subsequences of nums.

        A sequence of numbers is called arithmetic if it consists of at least three elements and if the difference between any two consecutive elements is the same.

        For example, [1, 3, 5, 7, 9], [7, 7, 7, 7], and [3, -1, -5, -9] are arithmetic sequences.
        For example, [1, 1, 2, 5, 7] is not an arithmetic sequence.
        A subsequence of an array is a sequence that can be formed by removing some elements (possibly none) of the array.

        For example, [2,5,10] is a subsequence of [1,2,1,2,4,1,5,10].
        The test cases are generated so that the answer fits in 32-bit integer.

        Link:
        https://leetcode.com/problems/arithmetic-slices-ii-subsequence/description/?envType=daily-question&envId=2024-01-08

        Inspiration:
        https://www.youtube.com/watch?v=YIMwwT9JdIE&t=1103s
        """
        """
        :type nums: List[int]
        :rtype: int
        """
        sequences = 0
        sols = [defaultdict(int) for _ in range(len(nums))]

        for i in range(len(nums)):
            differences = set([])
            for j in range(i):
                diff = nums[i] - nums[j]
                sols[i][diff] += 1 + sols[j][diff]
                differences.add(diff)
            for diff in differences:
                sequences += sols[i][diff]

        return sequences - Solution.choose(len(nums), 2)

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def countPalindromicSubsequences(self, s):
        """
        Given a string s, return the number of different non-empty palindromic subsequences in s. Since the answer may be very large, return it modulo 109 + 7.

        A subsequence of a string is obtained by deleting zero or more characters from the string.

        A sequence is palindromic if it is equal to the sequence reversed.

        Two sequences a1, a2, ... and b1, b2, ... are different if there is some i for which ai != bi.

        The string s only contains the characters, 'a','b','c', and 'd'

        Link:
        https://leetcode.com/problems/count-different-palindromic-subsequences/description/
        """
        """
        :type s: str
        :rtype: int
        """
        dp = []
        outermost_pairs = {"a": [], "b": [], "c": [], "d": []}
        for i in range(len(s)):
            row = []
            for j in range(len(s)):
                row.append(0)
            dp.append(row)

            for char in outermost_pairs:
                row = []
                for j in range(len(s)):
                    row.append((-1, -1))
                outermost_pairs[char].append(row)

        unique_chars = copy.deepcopy(dp)

        Solution.top_down_count_palindromes(
            0, len(s) - 1, s, dp, unique_chars, outermost_pairs
        )

        # Do not count the empty sequence
        return Solution.modularSubtract(dp[0][len(s) - 1], 1)

    @staticmethod
    def top_down_count_palindromes(start, end, s, dp, unique_chars, outermost_pairs):
        """
        Static top-down recursive method that uses dynamic programming to count the number of palindromatic subsequences within a string given a particular range
        """
        if dp[start][end] == 0:  # Then we have not solved this (sub)problem yet
            if start == end:
                # 'a' or empty
                dp[start][end] = 2
            elif start == end - 1:
                # 'a', 'aa', or empty
                dp[start][end] = 3
            else:
                # First count the number of unique characters
                total = Solution.num_unique_chars(s, start, end, unique_chars)

                # Simply find all nested, outermost occuring pairs - inner_start, inner_end
                inner_palindromes = 0
                for char in ["a", "b", "c", "d"]:
                    inner_start, inner_end = Solution.outermost_pair(
                        s, start, end, char, outermost_pairs
                    )
                    # All inner palindromes are of the form 'a*a' (or 'b*b', etc.), as long as we have two different 'a' indices then approach that subproblem in between to yield all possible '*'
                    if inner_end > inner_start:
                        inner_palindromes = Solution.modularAdd(
                            inner_palindromes,
                            Solution.top_down_count_palindromes(
                                inner_start + 1,
                                inner_end - 1,
                                s,
                                dp,
                                unique_chars,
                                outermost_pairs,
                            ),
                        )

                total = Solution.modularAdd(total, inner_palindromes)

                # Also count the empty sequence
                total = Solution.modularAdd(total, 1)

                # Store our solution
                dp[start][end] = total

        return dp[start][end]

    @staticmethod
    def num_unique_chars(s, start, end, unique_chars):
        """
        Count the number of unique characters between start and end
        """
        if unique_chars[start][end] == 0:
            uniques = set()
            for i in range(start, end + 1):
                uniques.add(s[i])
            unique_chars[start][end] = len(uniques)

        return unique_chars[start][end]

    @staticmethod
    def outermost_pair(s, start, end, char, outermost_pairs):
        """
        Static top-down recursive helper method to determine outermost range in [start,end] - call it k,l - where s[k] == char and s[l] == char
        """
        if outermost_pairs[char][start][end][0] == -1:
            # Then we have not solved this (sub)problem yet
            if start < end:
                if s[start] == char and s[end] == char:
                    outermost_pairs[char][start][end] = (start, end)
                elif s[start] == char:  # No point in moving up start
                    outermost_pairs[char][start][end] = Solution.outermost_pair(
                        s, start, end - 1, char, outermost_pairs
                    )
                elif s[end] == char:  # No point in moving down end
                    outermost_pairs[char][start][end] = Solution.outermost_pair(
                        s, start + 1, end, char, outermost_pairs
                    )
                else:  # Need to move up both
                    outermost_pairs[char][start][end] = Solution.outermost_pair(
                        s, start + 1, end - 1, char, outermost_pairs
                    )
            else:
                outermost_pairs[char][start][end] = (1, 0)

        return outermost_pairs[char][start][end]

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def splitArray(self, nums, k):
        """
        Given an integer array nums and an integer k, split nums into k non-empty subarrays such that the largest sum of any subarray is minimized.

        Return the minimized largest sum of the split.

        A subarray is a contiguous part of the array.

        Link:
        https://leetcode.com/problems/split-array-largest-sum/description/
        """
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """

        # Calculate the sums of all possible starts and ends in nums
        sums = [[0 for i in range(len(nums))] for j in range(len(nums))]
        for i in range(len(nums)):
            sums[i][i] = nums[i]
        maxes = copy.deepcopy(sums)
        for start in range(len(nums)):
            for end in range(start + 1, len(nums)):
                sums[start][end] = sums[start][end - 1] + nums[end]
                maxes[start][end] = max(maxes[start][end - 1], nums[end])

        # Inspiration for the following idea:
        # https://leetcode.com/problems/split-array-largest-sum/solutions/1899947/c-simple-code-easy-to-understand-tc-o-n-log-sum-nums-sc-o-1/
        # From user:
        # https://leetcode.com/manish_rawt/
        # Also user:
        # https://leetcode.com/anant_0059/
        min_possible = maxes[0][len(maxes[0]) - 1]
        if k == len(nums):  # Base case
            return min_possible
        max_possible = sums[0][len(sums[0]) - 1]
        if k == 1:  # Base case
            return max_possible
        best = max_possible
        while min_possible <= max_possible:
            sum_goal = int((min_possible + max_possible) / 2)
            if Solution.can_achieve_less_than_or_equal(sum_goal, nums, k):
                # try to do better
                best = sum_goal
                max_possible = sum_goal - 1
            else:
                # we'll have to settle for worse
                min_possible = sum_goal + 1

        return best

    @staticmethod
    def can_achieve_less_than_or_equal(sum_goal, nums, k):
        """
        Helper method to determine if a maximal sum less than or equal to beat_this_sum is achievable given the start, end, and number of subarrays we must create
        """
        intervals = 0
        index = 0
        while index < len(nums):
            current_interval_sum = 0
            while current_interval_sum <= sum_goal and index < len(nums):
                if sum_goal - nums[index] >= current_interval_sum:
                    current_interval_sum += nums[index]
                    index += 1
                else:
                    break
            intervals += 1

        return intervals <= k

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def isEvenOddTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        """
        A binary tree is named Even-Odd if it meets the following conditions:
        - The root of the binary tree is at level index 0, its children are at level index 1, their children are at level index 2, etc.
        - For every even-indexed level, all nodes at the level have odd integer values in strictly increasing order (from left to right).
        - For every odd-indexed level, all nodes at the level have even integer values in strictly decreasing order (from left to right).
        - Given the root of a binary tree, return true if the binary tree is Even-Odd, otherwise return false.
        
        Link:
        https://leetcode.com/problems/even-odd-tree/description/?envType=daily-question&envId=2024-02-29
        """
        children = []
        if root.left != None:
            children.append(root.left)
        if root.right != None:
            children.append(root.right)
        if root.val % 2 == 0:
            return False
        odd = True
        while len(children) > 0:
            current = children[0]
            if odd:
                if current.val % 2 == 1:
                    return False
            else:
                if current.val % 2 == 0:
                    return False
            index = 1
            while index < len(children):
                next = children[index]
                if odd:
                    if next.val % 2 == 1:  # must be even
                        return False
                    if (
                        next.val >= current.val
                    ):  # need to be in strictly decreasing order
                        return False
                    current = next
                    index += 1
                else:
                    if next.val % 2 == 0:  # must be odd
                        return False
                    if (
                        next.val <= current.val
                    ):  # need to be in strictly increasing order
                        return False
                    current = next
                    index += 1
            odd = not odd
            next_children = []
            for n in children:
                if n.left != None:
                    next_children.append(n.left)
                if n.right != None:
                    next_children.append(n.right)
            children = next_children

        return True

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def findCheapestPrice(self, n, flights, src, dst, k):
        """
        :type n: int
        :type flights: List[List[int]]
        :type src: int
        :type dst: int
        :type k: int
        :rtype: int
        """
        """
        There are n cities connected by some number of flights. You are given an array flights where flights[i] = [fromi, toi, pricei] indicates that there is a flight from city fromi to city toi with cost pricei.
        You are also given three integers src, dst, and k, return the cheapest price from src to dst with at most k stops. If there is no such route, return -1.
        
        Link:
        https://leetcode.com/problems/cheapest-flights-within-k-stops/description/?envType=daily-question&envId=2024-03-03
        """
        # At most n^3 = 10^6
        optimal_path_given_length = [
            [[-1 for i in range(n)] for i in range(n)] for i in range(k + 1)
        ]
        # first index is length, second index is start node, third index is end node

        for flight in flights:  # record the price of flights with 0 stops - base case
            optimal_path_given_length[0][flight[0]][flight[1]] = flight[2]

        for stops in range(1, k + 1):
            for start in range(n):
                for end in range(n):
                    if start != end:
                        # first guess
                        optimal_path_given_length[stops][start][end] = (
                            optimal_path_given_length[stops - 1][start][end]
                        )
                        # let's see if we can do better
                        for intermediate in range(n):
                            if intermediate != start and intermediate != end:
                                # start -> intermediate -> end
                                intermediate_stops = stops - 1
                                if (
                                    optimal_path_given_length[intermediate_stops][
                                        start
                                    ][intermediate]
                                    != -1
                                ):
                                    if (
                                        optimal_path_given_length[0][intermediate][end]
                                        != -1
                                    ):
                                        if (
                                            optimal_path_given_length[stops][start][end]
                                            == -1
                                        ):
                                            optimal_path_given_length[stops][start][
                                                end
                                            ] = (
                                                optimal_path_given_length[
                                                    intermediate_stops
                                                ][start][intermediate]
                                                + optimal_path_given_length[0][
                                                    intermediate
                                                ][end]
                                            )
                                        else:
                                            optimal_path_given_length[stops][start][
                                                end
                                            ] = min(
                                                optimal_path_given_length[stops][start][
                                                    end
                                                ],
                                                optimal_path_given_length[
                                                    intermediate_stops
                                                ][start][intermediate]
                                                + optimal_path_given_length[0][
                                                    intermediate
                                                ][end],
                                            )

        return optimal_path_given_length[k][src][dst]

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def bagOfTokensScore(self, tokens, power):
        """
        :type tokens: List[int]
        :type power: int
        :rtype: int
        """
        """
        You start with an initial power of power, an initial score of 0, and a bag of tokens given as an integer array tokens, where each tokens[i] donates the value of token[i].
        Your goal is to maximize the total score by strategically playing these tokens. In one move, you can play an unplayed token in one of the two ways (but not both for the same token):
            Face-up: If your current power is at least tokens[i], you may play tokeni, losing tokens[i] power and gaining 1 score.
            Face-down: If your current score is at least 1, you may play token[i], gaining tokens[i] power and losing 1 score.
        Return the maximum possible score you can achieve after playing any number of tokens.
        
        Link:
        https://leetcode.com/problems/bag-of-tokens/description/?envType=daily-question&envId=2024-03-04
        """
        if (len(tokens) == 0):
            return 0

        tokens.sort()
        if power < tokens[0]:
            return 0
        else:
            max_score = 1
            score = 1
            power -= tokens[0]
            left_index = 1
            right_index = len(tokens)-1
            while (right_index >= left_index):
                if power >= tokens[left_index]:
                    power -= tokens[left_index]
                    score += 1
                    left_index += 1
                    max_score = max(max_score, score)
                else:
                    if score >= 1:
                        score -= 1
                        power += tokens[right_index]
                        right_index -= 1
                    else:
                        break
            return max_score
    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

    def maxJumps(self, arr, d):
        """
        :type arr: List[int]
        :type d: int
        :rtype: int
        """
        """
        Given an array of integers arr and an integer d. In one step you can jump from index i to index:
            i + x where: i + x < arr.length and  0 < x <= d.
            i - x where: i - x >= 0 and  0 < x <= d.
        In addition, you can only jump from index i to index j if arr[i] > arr[j] and arr[i] > arr[k] for all indices k between i and j (More formally min(i, j) < k < max(i, j)).
        You can choose any index of the array and start jumping. Return the maximum number of indices you can visit.
        Notice that you can not jump outside of the array at any time.
        
        Link:
        https://leetcode.com/problems/jump-game-v/description/
        """
        

flights = [[0, 1, 100], [1, 2, 100], [2, 0, 100], [1, 3, 600], [2, 3, 200]]
print(Solution().findCheapestPrice(4, flights, 0, 3, 1))
