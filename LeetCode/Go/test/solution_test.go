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

func TestCountSubarraysWithSum(t *testing.T) {
	nums := []int{1, 0, 1, 0, 1}

	v := solution.NumSubArraysWithSum(nums, 2)
	if v != 4 {
		t.Fatalf("Expected 4 - got %d", v)
	}

	nums = []int{0, 0, 0, 0, 0}
	v = solution.NumSubArraysWithSum(nums, 0)
	if v != 15 {
		t.Fatalf("Expected 15 - got %d", v)
	}

	nums = []int{0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0}
	v = solution.NumSubArraysWithSum(nums, 5)
	if v != 10 {
		t.Fatalf("Expected 10 - got %d", v)
	}

	nums = []int{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0}
	v = solution.NumSubArraysWithSum(nums, 3)
	if v != 48 {
		t.Fatalf("Expected 48 - got %d", v)
	}

	nums = []int{1,0,0,0,0,0,0,0,0,0}
	v = solution.NumSubArraysWithSum(nums, 1)
	if v != 10 {
		t.Fatalf("Expected 10 - got %d", v)
	}
}
