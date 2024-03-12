package solution

import (
	"math"
	"sort"
	"leetcode/list_node"
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
		for col := row+1; col < count; col++ {
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
