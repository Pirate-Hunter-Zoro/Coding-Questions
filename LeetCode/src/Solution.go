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
		sum = int(math.Max(float64(sum), float64(nodes[left].Val) + float64(nodes[right].Val)))
		left++
		right--
	}

	return sum
}