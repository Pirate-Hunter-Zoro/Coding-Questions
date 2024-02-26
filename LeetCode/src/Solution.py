from collections import defaultdict
import copy


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
            for end in range(start+1, len(nums)):
                sums[start][end] = sums[start][end-1] + nums[end]
                maxes[start][end] = max(maxes[start][end-1], nums[end])

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
                if sum_goal-nums[index] >= current_interval_sum:
                    current_interval_sum += nums[index]
                    index += 1
                else:
                    break
            intervals += 1

        return intervals <= k

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

nums = [5, 2, 4, 1, 3, 6, 0]
k = 4
print(Solution().splitArray(nums, k))
