from collections import defaultdict


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
            return ((first % Solution.mod) + (Solution.mod - second)) % Solution.mod

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

        Link:
        https://leetcode.com/problems/count-palindromic-subsequences/description/
        """
        """
        :type s: str
        :rtype: int
        """
        

    """
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    """
