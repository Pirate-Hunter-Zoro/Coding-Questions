package test

import (
	"leetcode/list_node"
	"leetcode/solution"
	"testing"
)

func TestRemoveZeroSum(t *testing.T) {
	head := &list_node.ListNode{
		Val: -1,
		Next: &list_node.ListNode{
			Val: -2,
			Next: &list_node.ListNode{
				Val: 2,
				Next: &list_node.ListNode{
					Val: -1,
					Next: &list_node.ListNode{
						Val: 0,
					},
				},
			},
		},
	}

	new_head := solution.RemoveZeroSumSublists(head)
	current := new_head
	expected := make([]int, 2)
	expected[0] = -1
	expected[1] = -1
	for i := 0; i < len(expected); i++ {
		if current == nil {
			t.Fatalf("Expected node with index %d to exist, but was null...",
				i)
		}
		if current.Val != expected[i] {
			t.Fatalf("Expected node with index %d to have value %d, but saw %d",
				i, expected[i], current.Val)
		}
		current = current.Next
	}

	if current != nil {
		t.Fatalf("List is longer than expected - found node with value %d at index %d",
			current.Val, len(expected))
	}
}
