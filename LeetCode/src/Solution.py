from collections import defaultdict


class Solution(object):

    def numberOfArithmeticSlices(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        sequences = 0
        sols = [defaultdict(int) for _ in range(len(nums))]