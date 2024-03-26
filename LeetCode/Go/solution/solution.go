package solution

import (
	"leetcode/heap"
	"leetcode/list_node"
	"leetcode/modulo"
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
						current_head = nodes[start]
					}
				} else {
					if end == count-1 {
						nodes[prev_end].Next = nil
						return current_head
					} else {
						start = end + 1
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
https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/description/?envType=daily-question&envId=2024-03-18
*/
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
A super ugly number is a positive integer whose prime factors are in the (sorted) array primes.

Given an integer n and an array of integers primes, return the nth super ugly number.

The nth super ugly number is guaranteed to fit in a 32-bit signed integer.

Link:
https://leetcode.com/problems/super-ugly-number/description/
*/
func NthSuperUglyNumber(n int, primes []int) int {

	// The 'index' of the most recent super ugly number we have reached
	k := 1
	// The value of the most recent super ugly number we have reached
	kth_super_ugly := 1

	prime_heaps := make([]heap.MinHeap, len(primes))
	for i := 0; i < len(primes); i++ {
		prime_heaps[i] = heap.MinHeap{}
		prime_heaps[i].Insert(1)
	}

	for k < n {
		lowest := math.MaxInt
		idx := -1
		// Peek from each heap, and see how small multiplying said value by the heap's prime number would be
		for i := 0; i < len(prime_heaps); i++ {
			v := prime_heaps[i].Peek() * primes[i]
			if v < lowest {
				idx = i
				lowest = v
			}
		}
		// Pop whichever value corresponds to the lowest new value
		// That new value is the next super ugly number
		// Throw it into all your heaps
		k++
		kth_super_ugly = prime_heaps[idx].Extract() * primes[idx]
		for i := idx; i < len(prime_heaps); i++ {
			// Start at 'idx'; not zero, to avoid repeats
			prime_heaps[i].Insert(kth_super_ugly)
		}
	}

	return kth_super_ugly
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
You are given an array of CPU tasks, each represented by letters A to Z, and a cooling time, n. Each cycle or interval allows the completion of one task. Tasks can be completed in any order, but there's a constraint: identical tasks must be separated by at least n intervals due to cooling time.

​Return the minimum number of intervals required to complete all tasks.

Link:
https://leetcode.com/problems/task-scheduler/description/?envType=daily-question&envId=2024-03-19
*/
func LeastInterval(tasks []byte, n int) int {
	cooldown_left := make(map[byte]int)
	counts := make(map[byte]int)

	intervals := 0
	for i := 0; i < len(tasks); i++ {
		v, present := counts[tasks[i]]
		if !present {
			counts[tasks[i]] = 1
		} else {
			counts[tasks[i]] = v + 1
		}
	}
	for task := range counts {
		cooldown_left[task] = 0
	}

	tasks_scheduled := 0
	// At the beginning of this for loop, at least one task will be cooled down
	for tasks_scheduled < len(tasks) {
		intervals++
		schedule_this := byte(0)
		max_count := math.MinInt
		min_nonzero_cooldown := math.MaxInt // will be useful later
		// Find the task with zero cooldown of the highest count
		task_remaining_with_zero_cooldown := false
		for task, cooldown := range cooldown_left {
			if cooldown == 0 {
				if max_count > math.MinInt {
					// This is not the first zero cooldown task we have seen
					task_remaining_with_zero_cooldown = true
				}
				if counts[task] > max_count {
					schedule_this = task
					max_count = counts[task]
				}
			} else {
				cooldown_left[task]--
				if cooldown_left[task] == 0 {
					task_remaining_with_zero_cooldown = true
				} else {
					min_nonzero_cooldown = min(min_nonzero_cooldown, cooldown_left[task])
				}
			}
		}
		tasks_scheduled++
		counts[schedule_this]--
		if counts[schedule_this] == 0 {
			delete(counts, schedule_this)
			delete(cooldown_left, schedule_this)
		} else {
			cooldown_left[schedule_this] = n
			min_nonzero_cooldown = min(min_nonzero_cooldown, n)
		}

		if !task_remaining_with_zero_cooldown && tasks_scheduled < len(tasks) {
			for task := range cooldown_left {
				cooldown_left[task] -= min_nonzero_cooldown
			}
			intervals += min_nonzero_cooldown
		}
	}

	return intervals
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
You are given two linked lists: list1 and list2 of sizes n and m respectively.

Remove list1's nodes from the ath node to the bth node, and put list2 in their place.

Link:
https://leetcode.com/problems/merge-in-between-linked-lists/description/?envType=daily-question&envId=2024-03-20
*/
func MergeInBetween(list1 *list_node.ListNode, a int, b int, list2 *list_node.ListNode) *list_node.ListNode {
	idx := 0
	current := list1
	for idx < a-1 {
		idx++
		current = current.Next
	}
	append := current.Next
	idx = a
	for idx <= b {
		append = append.Next
		idx++
	}

	current.Next = list2
	current = list2
	for current.Next != nil {
		current = current.Next
	}
	current.Next = append

	return list1
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
There is an integer array nums sorted in ascending order (with distinct values).

Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].

Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.

You must write an algorithm with O(log n) runtime complexity.

Link:
https://leetcode.com/problems/search-in-rotated-sorted-array/description/
*/
func Search(nums []int, target int) int {
	return searchPivot(nums, 0, len(nums), target)
}

/*
Helper method to search for an integer in nums, which is sorted except for a pivot
*/
func searchPivot(nums []int, left int, right int, target int) int {
	if right-left < 3 {
		if right-left < 2 {
			if nums[left] == target {
				return left
			} else {
				return -1
			}
		} else {
			if nums[left] == target {
				return left
			} else if nums[left+1] == target {
				return left + 1
			} else {
				return -1
			}
		}
	}

	search_left := left
	search_right := right
	pivot := -1
	// search for the pivot
	for search_left < search_right {
		mid := (search_left + search_right) / 2
		if mid == len(nums)-1 || nums[mid] > nums[mid+1] {
			pivot = mid
			break
		} else if mid > 0 && nums[mid-1] > nums[mid] {
			pivot = mid - 1
			break
		} else if nums[mid] > nums[search_right-1] {
			// pivot is right of mid
			search_left = mid + 1
		} else {
			// pivot is left of mid
			search_right = mid
		}
	}

	if pivot == -1 {
		// binary search  normally
		return binarySearch(nums, left, right, target)
	}

	// Otherwise
	if nums[pivot] == target {
		return pivot
	}

	// binary search from pivot + 1 to the end
	idx := binarySearch(nums, pivot+1, right, target)
	if idx != -1 {
		return idx
	}

	// binary search from start to pivot
	idx = binarySearch(nums, left, pivot, target)
	if idx != -1 {
		return idx
	}

	return -1
}

/*
There is an integer array nums sorted in non-decreasing order (not necessarily with distinct values).

Before being passed to your function, nums is rotated at an unknown pivot index k (0 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,4,4,5,6,6,7] might be rotated at pivot index 5 and become [4,5,6,6,7,0,1,2,4,4].

Given the array nums after the rotation and an integer target, return true if target is in nums, or false if it is not in nums.

You must decrease the overall operation steps as much as possible.

Link:
https://leetcode.com/problems/search-in-rotated-sorted-array-ii/description/
*/
func SearchRepeats(nums []int, target int) bool {
	if nums[0] == nums[len(nums)-1] {
		// then the pivot is in the middle of a bunch of repeats
		if len(nums) < 3 {
			if len(nums) < 2 {
				return nums[0] == target
			} else {
				return nums[0] == target || nums[1] == target
			}
		}

		v := nums[0]
		if v == target {
			return true
		}
		left := 1
		right := len(nums) - 1
		for nums[left] == v && nums[right] == v && left < right {
			left++
			right--
		}
		if left >= right {
			return nums[left] == target || nums[right] == target
		} else {
			return searchPivot(nums, left, right+1, target) != -1
		}
	} else {
		// then nothing changes from the version of this problem where we did not have repeats
		if searchPivot(nums, 0, len(nums), target) != -1 {
			return true
		} else {
			return false
		}
	}
}

/*
Binary search a sorted integer array for a target value from left to right
NOTE - 'right' is exclusive
*/
func binarySearch(nums []int, left int, right int, target int) int {
	// binary search  normally
	for left < right {
		mid := (left + right) / 2
		if nums[mid] == target {
			return mid
		} else if nums[mid] < target {
			left = mid + 1
		} else {
			right = mid
		}
	}
	return -1
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
Given an array of integers nums containing n + 1 integers where each integer is in the range [1, n] inclusive.

There is only one repeated number in nums, return this repeated number.

You must solve the problem without modifying the array nums and uses only constant extra space.

Link:
https://leetcode.com/problems/find-the-duplicate-number/description/?envType=daily-question&envId=2024-03-24

Solution Link:
https://keithschwarz.com/interesting/code/?dir=find-duplicate
*/
func FindDuplicate(nums []int) int {
	// This will form a Ro-shaped sequence - we need to find the beginning of the loop
	n := len(nums) - 1
	slow := n + 1
	fast := n + 1
	slow = nums[slow-1]
	fast = nums[nums[fast-1]-1]

	// Iterate slow and fast such that if slow = x_j, fast = x_{2j}
	for slow != fast {
		slow = nums[slow-1]
		fast = nums[nums[fast-1]-1]
	}

	// Now that slow = fast, slow = x_j, fast = x_{2j}
	// Let length of chain leading up to loop = c
	// Let loop length = l. j is the smallest multiple of l bigger than c
	// Proof: j > c because it must be in the loop
	//		  Also, since x_j=x_{2j}, is j iterations must mean we go around the loop a fixed number of times, so j is a multiple of l
	//		  j is the smallest such multiply of l because any smaller such multiple of l, our above iteration would have hit first

	// Now find the starting point of the loop
	finder := n + 1 // x_0
	// Also recall slow = x_j
	// Further, x_{c+j} is equivalent to iterating up to the start of the loop, and progressing around the loop an integer number of times
	// So you'll end up at the start of the loop after starting at x_0 and going through c+j iterations, and slow has already done j iterations
	// Therefore, after c iterations, finder will be at x_c - the start of the loop - by definition, and so will slow
	for slow != finder {
		finder = nums[finder-1]
		slow = nums[slow-1]
	}

	return finder
}

/*
Given an integer array nums of length n where all the integers of nums are in the range [1, n] and each integer appears once or twice, return an array of all the integers that appears twice.

You must write an algorithm that runs in O(n) time and uses only constant extra space.

Link:
https://leetcode.com/problems/find-all-duplicates-in-an-array/description/?envType=daily-question&envId=2024-03-25
*/
func FindDuplicates(nums []int) []int {
	duplicates := []int{}

	return duplicates
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
Given an integer n, your task is to count how many strings of length n can be formed under the following rules:

Each character is a lower case vowel ('a', 'e', 'i', 'o', 'u')
Each vowel 'a' may only be followed by an 'e'.
Each vowel 'e' may only be followed by an 'a' or an 'i'.
Each vowel 'i' may not be followed by another 'i'.
Each vowel 'o' may only be followed by an 'i' or a 'u'.
Each vowel 'u' may only be followed by an 'a'.
Since the answer may be too large, return it modulo 10^9 + 7.

Link:
https://leetcode.com/problems/count-vowels-permutation/?envType=daily-question&envId=2024-03-24
*/
func CountVowelPermutation(n int) int {
	chars := map[byte]int{
		'a': 0,
		'e': 1,
		'i': 2,
		'o': 3,
		'u': 4,
	}
	can_follow := map[byte][]byte{
		'a': {'e'},
		'e': {'a', 'i'},
		'i': {'a', 'e', 'o', 'u'},
		'o': {'i', 'u'},
		'u': {'a'},
	}
	sols := make([][]int, len(chars))
	for i := 0; i < len(sols); i++ {
		sols[i] = make([]int, n)
		sols[i][0] = 1
	}
	total := 0
	// we could start with any vowel
	for vowel := range chars {
		total = modulo.ModularAdd(total, topDownVowelPermutation(n, vowel, sols, chars, can_follow))
	}
	return total
}

/*
Top down helper method to find the number of vowel combinations we cna make of the given length and the vowel we are starting
*/
func topDownVowelPermutation(n int, vowel byte, sols [][]int, chars map[byte]int, can_follow map[byte][]byte) int {

	if sols[chars[vowel]][n-1] == 0 {
		// Need to solve this problem
		total := 0
		// Consider each character who can follow
		for _, character := range can_follow[vowel] {
			total = modulo.ModularAdd(total, topDownVowelPermutation(n-1, character, sols, chars, can_follow))
		}
		sols[chars[vowel]][n-1] = total
	}

	return sols[chars[vowel]][n-1]
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
There are buckets buckets of liquid, where exactly one of the buckets is poisonous. To figure out which one is poisonous, you feed some number of (poor) pigs the liquid to see whether they will die or not. Unfortunately, you only have minutesToTest minutes to determine which bucket is poisonous.

You can feed the pigs according to these steps:

1) Choose some live pigs to feed.
2) For each pig, choose which buckets to feed it. The pig will consume all the chosen buckets simultaneously and will take no time. Each pig can feed from any number of buckets, and each bucket can be fed from by any number of pigs.
3) Wait for minutesToDie minutes. You may not feed any other pigs during this time.
4) After minutesToDie minutes have passed, any pigs that have been fed the poisonous bucket will die, and all others will survive.
5) Repeat this process until you run out of time.

Given buckets, minutesToDie, and minutesToTest, return the minimum number of pigs needed to figure out which bucket is poisonous within the allotted time.

Link:
https://leetcode.com/problems/poor-pigs/description/?envType=daily-question&envId=2024-03-24
*/
func PoorPigs(buckets int, minutesToDie int, minutesToTest int) int {
	// According to the hints on LeetCode:
	// Say you have X pigs, and time enough for T test rounds.
	// How many different states does that generate?
	// Each pig could die on any of the rounds, or none of them, making T+1 total possibilities for each pig.
	// That's (T+1)^X possible states achieved, each corresponding to a different bucket being poisoned.
	// So pick X such that (T+1)^X >= buckets!
	toDie := float64(minutesToDie)
    toTest := float64(minutesToTest)
    T := math.Floor(toTest/toDie)
    return int(math.Ceil(math.Log2(float64(buckets))/math.Log2(float64(T+1))))
}
