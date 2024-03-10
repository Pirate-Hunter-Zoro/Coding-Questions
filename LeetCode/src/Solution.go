package src

import (
	"math"
)

type ListNode struct {
	Val  int
	Next *ListNode
}

/*
Given the head of a singly linked list, return the middle node of the linked list.
If there are two middle nodes, return the second middle node.

Link:
https://leetcode.com/problems/middle-of-the-linked-list/description/?envType=daily-question&envId=2024-03-07
*/
func middleNode(head *ListNode) *ListNode {
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

/*
In a linked list of size n, where n is even, the ith node (0-indexed) of the linked list is known as the twin of the (n-1-i)th node, if 0 <= i <= (n / 2) - 1.

For example, if n = 4, then node 0 is the twin of node 3, and node 1 is the twin of node 2. These are the only nodes with twins for n = 4.
The twin sum is defined as the sum of a node and its twin.

Given the head of a linked list with even length, return the maximum twin sum of the linked list.

Link:
https://leetcode.com/problems/maximum-twin-sum-of-a-linked-list/description/
*/
func pairSum(head *ListNode) int {
	nodes := make(map[int]*ListNode)
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

func generateMatrix(n int) [][]int {

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
