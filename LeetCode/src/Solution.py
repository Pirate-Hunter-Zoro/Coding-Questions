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

        sols = {}
        return Solution.top_down_split_array(((0,len(nums)-1),k),sums, maxes, sols)

    @staticmethod
    def top_down_split_array(subproblem, sums, maxes, sols):
        """
        Top-down recursive helper method to optimize splitting the array from a certain start to a certain end a given number of times to minimize the maximal subsequence sum
        """
        if subproblem in sols.keys():
            return sols[subproblem]

        # Explore all possible last splits
        start = subproblem[0][0]
        end = subproblem[0][1]
        subarrays = subproblem[1]
        if subarrays == 1:
            sols[subproblem] = sums[start][end]
        elif subarrays == end - start + 1:
            sols[subproblem] = maxes[start][end]
        else:
            best_result = 10000000000
            for last_split_right_before_here in range(start+1,end):
                max_left_splits = min(last_split_right_before_here-start, subarrays-1)
                for left_subarrays in range(1, max_left_splits+1):
                    right_subarrays = subarrays - left_subarrays
                    left_subproblem = ((start,last_split_right_before_here-1),left_subarrays)
                    right_subproblem = ((last_split_right_before_here, end),right_subarrays)
                    this_result = max(Solution.top_down_split_array(left_subproblem, sums, maxes, sols), Solution.top_down_split_array(right_subproblem, sums, maxes, sols))
                    best_result = min(best_result, this_result)
            sols[subproblem] = best_result

        return sols[subproblem]

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """

nums = [
    5334,
    6299,
    4199,
    9663,
    8945,
    3566,
    9509,
    3124,
    6026,
    6250,
    7475,
    5420,
    9201,
    9501,
    38,
    5897,
    4411,
    6638,
    9845,
    161,
    9563,
    8854,
    3731,
    5564,
    5331,
    4294,
    3275,
    1972,
    1521,
    2377,
    3701,
    6462,
    6778,
    187,
    9778,
    758,
    550,
    7510,
    6225,
    8691,
    3666,
    4622,
    9722,
    8011,
    7247,
    575,
    5431,
    4777,
    4032,
    8682,
    5888,
    8047,
    3562,
    9462,
    6501,
    7855,
    505,
    4675,
    6973,
    493,
    1374,
    3227,
    1244,
    7364,
    2298,
    3244,
    8627,
    5102,
    6375,
    8653,
    1820,
    3857,
    7195,
    7830,
    4461,
    7821,
    5037,
    2918,
    4279,
    2791,
    1500,
    9858,
    6915,
    5156,
    970,
    1471,
    5296,
    1688,
    578,
    7266,
    4182,
    1430,
    4985,
    5730,
    7941,
    3880,
    607,
    8776,
    1348,
    2974,
    1094,
    6733,
    5177,
    4975,
    5421,
    8190,
    8255,
    9112,
    8651,
    2797,
    335,
    8677,
    3754,
    893,
    1818,
    8479,
    5875,
    1695,
    8295,
    7993,
    7037,
    8546,
    7906,
    4102,
    7279,
    1407,
    2462,
    4425,
    2148,
    2925,
    3903,
    5447,
    5893,
    3534,
    3663,
    8307,
    8679,
    8474,
    1202,
    3474,
    2961,
    1149,
    7451,
    4279,
    7875,
    5692,
    6186,
    8109,
    7763,
    7798,
    2250,
    2969,
    7974,
    9781,
    7741,
    4914,
    5446,
    1861,
    8914,
    2544,
    5683,
    8952,
    6745,
    4870,
    1848,
    7887,
    6448,
    7873,
    128,
    3281,
    794,
    1965,
    7036,
    8094,
    1211,
    9450,
    6981,
    4244,
    2418,
    8610,
    8681,
    2402,
    2904,
    7712,
    3252,
    5029,
    3004,
    5526,
    6965,
    8866,
    2764,
    600,
    631,
    9075,
    2631,
    3411,
    2737,
    2328,
    652,
    494,
    6556,
    9391,
    4517,
    8934,
    8892,
    4561,
    9331,
    1386,
    4636,
    9627,
    5435,
    9272,
    110,
    413,
    9706,
    5470,
    5008,
    1706,
    7045,
    9648,
    7505,
    6968,
    7509,
    3120,
    7869,
    6776,
    6434,
    7994,
    5441,
    288,
    492,
    1617,
    3274,
    7019,
    5575,
    6664,
    6056,
    7069,
    1996,
    9581,
    3103,
    9266,
    2554,
    7471,
    4251,
    4320,
    4749,
    649,
    2617,
    3018,
    4332,
    415,
    2243,
    1924,
    69,
    5902,
    3602,
    2925,
    6542,
    345,
    4657,
    9034,
    8977,
    6799,
    8397,
    1187,
    3678,
    4921,
    6518,
    851,
    6941,
    6920,
    259,
    4503,
    2637,
    7438,
    3893,
    5042,
    8552,
    6661,
    5043,
    9555,
    9095,
    4123,
    142,
    1446,
    8047,
    6234,
    1199,
    8848,
    5656,
    1910,
    3430,
    2843,
    8043,
    9156,
    7838,
    2332,
    9634,
    2410,
    2958,
    3431,
    4270,
    1420,
    4227,
    7712,
    6648,
    1607,
    1575,
    3741,
    1493,
    7770,
    3018,
    5398,
    6215,
    8601,
    6244,
    7551,
    2587,
    2254,
    3607,
    1147,
    5184,
    9173,
    8680,
    8610,
    1597,
    1763,
    7914,
    3441,
    7006,
    1318,
    7044,
    7267,
    8206,
    9684,
    4814,
    9748,
    4497,
    2239,
]
k = 9
print(Solution().splitArray(nums, k))
