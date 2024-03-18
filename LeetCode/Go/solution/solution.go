package solution

import (
	"leetcode/heap"
	"leetcode/list_node"
	"math"
	"sort"
)

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
Given the head of a singly linked list, return the middle node of the linked list.
If there are two middle nodes, return the second middle node.

Link:
https://leetcode.com/problems/middle-of-the-linked-list/description/?envType=daily-question&envId=2024-03-07
*/
func MiddleNode(head *list_node.ListNode) *list_node.ListNode {
	count := 0.0
	current := head
	for current != nil {
		count += 1.0
		current = current.Next
	}
	count_left := math.Floor(count / 2.0)
	current_count_left := 0
	current = head
	for current_count_left < int(count_left) {
		current = current.Next
		current_count_left += 1
	}
	return current
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
In a linked list of size n, where n is even, the ith node (0-indexed) of the linked list is known as the twin of the (n-1-i)th node, if 0 <= i <= (n / 2) - 1.

For example, if n = 4, then node 0 is the twin of node 3, and node 1 is the twin of node 2. These are the only nodes with twins for n = 4.
The twin sum is defined as the sum of a node and its twin.

Given the head of a linked list with even length, return the maximum twin sum of the linked list.

Link:
https://leetcode.com/problems/maximum-twin-sum-of-a-linked-list/description/
*/
func PairSum(head *list_node.ListNode) int {
	nodes := make(map[int]*list_node.ListNode)
	current := head
	idx := 0
	for current != nil {
		nodes[idx] = current
		current = current.Next
		idx++
	}
	sum := 0
	left, right := 0, idx-1
	for left < right {
		sum = int(math.Max(float64(sum), float64(nodes[left].Val)+float64(nodes[right].Val)))
		left++
		right--
	}

	return sum
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
*
Given a positive integer n, generate an n x n matrix filled with elements from 1 to n^2 in spiral order.

Link:
https://leetcode.com/problems/spiral-matrix-ii/description/
*/
func GenerateMatrix(n int) [][]int {

	to_return := make([][]int, n)
	for i := 0; i < n; i++ {
		to_return[i] = make([]int, n)
	}
	to_return[0][0] = 1
	num_cells := n * n

	row := 0
	col := 1
	row_min := 0
	row_max := n - 1
	col_min := 0
	col_max := n - 1
	for i := 1; i < num_cells; i++ {
		if row == row_min && col == col_min { // start a new spiral
			row++
			col++
			to_return[row][col] = i + 1
			col++
			row_min++
			col_min++
			row_max--
			col_max--
		} else {
			to_return[row][col] = i + 1
			if col == col_max { // right side
				if row == row_max { // at bottom, so go left
					col--
				} else { // going down
					row++
				}
			} else if row == row_max {
				if col == col_min { // at left, so go up
					row--
				} else { // going left
					col--
				}
			} else if row == row_min { // at top, going right
				col++
			} else if col == col_min { // at left, going up
				row--
			}

		}
	}

	return to_return

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
You are given two strings order and s. All the characters of order are unique and were sorted in some custom order previously.

Permute the characters of s so that they match the order that order was sorted. More specifically, if a character x occurs before a character y in order, then x should occur before y in the permuted string.

Return any permutation of s that satisfies this property.

Link:
https://leetcode.com/problems/custom-sort-string/description/?envType=daily-question&envId=2024-03-11
*/
func CustomSorting(order string, s string) string {
	orderings := make(map[byte]int)
	for i := 0; i < len(order); i++ {
		orderings[order[i]] = i
	}

	s_chars := make([]byte, len(s))
	for i := 0; i < len(s); i++ {
		s_chars[i] = s[i]
	}

	sort.SliceStable(s_chars, func(idx_1, idx_2 int) bool {
		return orderings[s_chars[idx_1]] < orderings[s_chars[idx_2]]
	})

	return string(s_chars)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
*
Given the head of a linked list, we repeatedly delete consecutive sequences of nodes that sum to 0 until there are no such sequences.

After doing so, return the head of the final linked list.  You may return any such answer.

Link:
https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/description/?envType=daily-question&envId=2024-03-12
*/
func RemoveZeroSumSublists(head *list_node.ListNode) *list_node.ListNode {
	if head.Next == nil {
		if head.Val == 0 {
			return nil
		}
		return head
	}

	// Get instant access for each node value
	count := 0
	current := head
	for current != nil {
		count++
		current = current.Next
	}
	vals := make([]int, count)
	nodes := make([]*list_node.ListNode, count)
	count = 0
	current = head
	for current != nil {
		nodes[count] = current
		vals[count] = current.Val
		count++
		current = current.Next
	}

	// Otherwise we have some work to do
	sums := make([][]int, count)
	for i := 0; i < count; i++ {
		sums[i] = make([]int, count)
	}

	for i := 0; i < count; i++ {
		sums[i][i] = vals[i]
	}
	for row := 0; row < count; row++ {
		for col := row + 1; col < count; col++ {
			sums[row][col] = sums[row][col-1] + vals[col]
		}
	}

	// WLOG, we can find the greatest length 0 sequence starting at index 0, find where it ends, and repeat for whatever precedes the ending index, etc.
	prev_end := -1
	start := 0
	current_head := head
	for start < count {
		found_zero_sum := false
		end := count - 1
		for end >= start {
			if sums[start][end] == 0 {
				found_zero_sum = true
				if nodes[start] == current_head {
					if end == count-1 {
						return nil
					} else {
						start = end + 1
						end = count - 1
						current_head = nodes[start]
					}
				} else {
					if end == count-1 {
						nodes[prev_end].Next = nil
						return current_head
					} else {
						start = end + 1
						end = count - 1
						nodes[prev_end].Next = nodes[start]
					}
				}
				break
			}
			end--
		}
		if !found_zero_sum {
			prev_end = start
			start++
		}
	}

	return current_head
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
Given a binary array nums and an integer goal, return the number of non-empty subarrays with a sum goal.

A subarray is a contiguous part of the array.

Link:
https://leetcode.com/problems/binary-subarrays-with-sum/description/?envType=daily-question&envId=2024-03-14
*/
func NumSubArraysWithSum(nums []int, goal int) int {
	// Handle zero separately
	if goal == 0 {
		count := 0
		left := 0
		for left < len(nums) && nums[left] == 1 {
			left++
		}
		right := left
		for right < len(nums) {
			if nums[right] == 0 {
				count += right - left + 1
				right++
			} else {
				for right < len(nums) && nums[right] == 1 {
					right++
				}
				left = right
			}
		}

		return count
	}

	// we KNOW the sum goal is greater than 0
	left := 0
	right := 0
	sum := nums[0]
	count := 0
	// progress until we hit our goal, and move up left until it hits a 1
	for right < len(nums) && sum < goal {
		right++
		if right >= len(nums) {
			break
		}
		sum += nums[right]
	}
	if sum < goal { // not possible to reach goal
		return 0
	}
	// otherwise, it was possible to reach the goal - move up left until it hits a 1
	for nums[left] == 0 {
		left++
	}

	// at each restart of the following loop, nums[left] == 1 AND nums[right] == 1, and we need to count how many consecutive zeros lie left, and how many consecutive zeros lie right
	for right < len(nums) {
		zeros_left := 0
		left_scanner := left - 1
		for left_scanner >= 0 && nums[left_scanner] == 0 {
			zeros_left++
			left_scanner--
		}

		zeros_right := 0
		right_scanner := right + 1
		for right_scanner < len(nums) && nums[right_scanner] == 0 {
			zeros_right++
			right_scanner++
		}

		count += (zeros_left + 1) * (zeros_right + 1)

		left++
		for left < len(nums) && nums[left] == 0 {
			left++
		}
		right++
		for right < len(nums) && nums[right] == 0 {
			right++
		}
	}

	return count
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].

The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.

You must write an algorithm that runs in O(n) time and without using the division operation.

Link:
https://leetcode.com/problems/product-of-array-except-self/description/?envType=daily-question&envId=2024-03-15
*/
func ProductExceptSelf(nums []int) []int {
	sols := make([]int, len(nums))
	prod := 1
	num_zeros := 0
	for i := 0; i < len(nums); i++ {
		if nums[i] != 0 {
			prod *= nums[i]
		} else {
			num_zeros++
			if num_zeros > 1 {
				return sols
			}
		}
	}
	for i := 0; i < len(nums); i++ {
		if nums[i] != 0 {
			if num_zeros == 0 {
				sols[i] = prod / nums[i]
			}
		} else {
			sols[i] = prod
		}
	}

	return sols
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
*
You are given a 0-indexed integer array nums consisting of 3 * n elements.

You are allowed to remove any subsequence of elements of size exactly n from nums. The remaining 2 * n elements will be divided into two equal parts:

The first n elements belonging to the first part and their sum is sumfirst.
The next n elements belonging to the second part and their sum is sumsecond.
The difference in sums of the two parts is denoted as sumfirst - sumsecond.

For example, if sumfirst = 3 and sumsecond = 2, their difference is 1.
Similarly, if sumfirst = 2 and sumsecond = 3, their difference is -1.
Return the minimum difference possible between the sums of the two parts after the removal of n elements.

Link:
https://leetcode.com/problems/minimum-difference-in-sums-after-removal-of-elements/description/

Inspiration:
https://leetcode.com/problems/minimum-difference-in-sums-after-removal-of-elements/solutions/1747029/python-explanation-with-pictures-priority-queue/

User Who Gave Inspiration:
https://leetcode.com/Bakerston/
*/
func MinimumDifference(nums []int) int64 {
	n := len(nums) / 3

	min_sum_left := int64(0)
	left_heap := heap.MaxHeap{}
	for i := 0; i < n; i++ {
		left_heap.Insert(nums[i])
		min_sum_left += int64(nums[i])
	}

	max_sum_right := int64(0)
	right_heap := heap.MinHeap{}
	for i := 2 * n; i < 3*n; i++ {
		right_heap.Insert(nums[i])
		max_sum_right += int64(nums[i])
	}

	left_sums := make([]int64, n+1) // left_sums[i] is the minimum sum of n numbers achievable in nums[0:n+i-1]
	left_sums[0] = min_sum_left
	right_sums := make([]int64, n+1) // right_sums[i] is the maximum sum of n numbers achievable in nums[n+i:3n-1]
	right_sums[n] = max_sum_right
	for i := 1; i <= n; i++ {
		// handle left
		next_left := nums[n+i-1]
		if left_heap.Peek() > next_left {
			v := left_heap.Extract()
			left_heap.Insert(next_left)
			min_sum_left -= int64(v)
			min_sum_left += int64(next_left)
		}
		left_sums[i] = min_sum_left

		// handle right
		next_right := nums[2*n-i]
		if right_heap.Peek() < next_right {
			v := right_heap.Extract()
			right_heap.Insert(next_right)
			max_sum_right -= int64(v)
			max_sum_right += int64(next_right)
		}
		right_sums[n-i] = max_sum_right
	}

	record := left_sums[0] - right_sums[0]
	for i := 1; i < len(left_sums); i++ {
		record = min(record, left_sums[i]-right_sums[i])
	}

	return record

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
*
Given a binary array nums, return the maximum length of a contiguous subarray with an equal number of 0 and 1.

Link:
https://leetcode.com/problems/contiguous-array/description/?envType=daily-question&envId=2024-03-16

Editorial:
https://leetcode.com/problems/contiguous-array/editorial/?envType=daily-question&envId=2024-03-16
*/
func FindMaxLength(nums []int) int {
	total := 0
	counts := make(map[int]int)
	counts[0] = -1
	record := 0
	for i := 0; i < len(nums); i++ {
		if nums[i] == 0 {
			total--
		} else {
			total++
		}
		earliest_occurence, key_present := counts[total]
		if key_present {
			length := i - earliest_occurence
			record = max(record, length)
		} else {
			counts[total] = i
		}
	}

	return record
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
There are some spherical balloons taped onto a flat wall that represents the XY-plane. The balloons are represented as a 2D integer array points where points[i] = [xstart, xend] denotes a balloon whose horizontal diameter stretches between xstart and xend. You do not know the exact y-coordinates of the balloons.

Arrows can be shot up directly vertically (in the positive y-direction) from different points along the x-axis. A balloon with xstart and xend is burst by an arrow shot at x if xstart <= x <= xend. There is no limit to the number of arrows that can be shot. A shot arrow keeps traveling up infinitely, bursting any balloons in its path.

Given the array points, return the minimum number of arrows that must be shot to burst all balloons.

Link:
https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/description/?envType=daily-question&envId=2024-03-18*/
func FindMinArrowShots(points [][]int) int {
	sort.SliceStable(points, func(idx_1, idx_2 int) bool {
		return points[idx_1][1] < points[idx_2][1]
	})
	// Essentially, shoot an arrow at the ends of all these balloons

	arrows := 0
	prev_end := math.MinInt
	i := 0
	for i < len(points) {
		for i < len(points) && points[i][0] <= prev_end {
			i++
		}
		if i < len(points) {
			arrows++
			prev_end = points[i][1]
		}
	}

	return arrows
}
