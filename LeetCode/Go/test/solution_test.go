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

	nums = []int{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	v = solution.NumSubArraysWithSum(nums, 1)
	if v != 10 {
		t.Fatalf("Expected 10 - got %d", v)
	}
}

func TestMinimumDifference(t *testing.T) {
	nums := []int{3, 1, 2}
	v := solution.MinimumDifference(nums)
	if v != int64(-1) {
		t.Fatalf("Expected -1 - got %d", v)
	}

	nums = []int{7, 9, 5, 8, 1, 3}
	v = solution.MinimumDifference(nums)
	if v != int64(1) {
		t.Fatalf("Expected 1 - got %d", v)
	}
}

func TestFindMaxLength(t *testing.T) {
	nums := []int{0, 1}
	v := solution.FindMaxLength(nums)
	if v != 2 {
		t.Fatalf("Expected 2 - got %d", v)
	}

	nums = []int{0, 1, 1, 0, 1, 1, 1, 0}
	v = solution.FindMaxLength(nums)
	if v != 4 {
		t.Fatalf("Expected 4 - got %d", v)
	}

	nums = []int{0, 0, 1, 0, 0, 0, 1, 1}
	v = solution.FindMaxLength(nums)
	if v != 6 {
		t.Fatalf("Expected 6 - got %d", v)
	}

	nums = []int{1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1}
	v = solution.FindMaxLength(nums)
	if v != 670 {
		t.Fatalf("Expected 670 - got %d", v)
	}
}

func TestFindMindArrowShots(t *testing.T) {
	nums := [][]int{{10, 16}, {2, 8}, {1, 6}, {7, 12}}
	v := solution.FindMinArrowShots(nums)
	if v != 2 {
		t.Fatalf("Expected 2 - got %d", v)
	}
}

func TestNthSuperUglyNumber(t *testing.T) {
	primes := []int{2, 7, 13, 19}
	n := 12
	v := solution.NthSuperUglyNumber(n, primes)
	if v != 32 {
		t.Fatalf("Expected 32 - got %d", v)
	}
}

func TestLeastInterval(t *testing.T) {
	tasks := []byte{65, 65, 65, 66, 66, 66}
	n := 2
	v := solution.LeastInterval(tasks, n)
	if v != 8 {
		t.Fatalf("Expected 8 - got %d", v)
	}

	tasks = []byte{65, 67, 65, 66, 68, 66}
	n = 1
	v = solution.LeastInterval(tasks, n)
	if v != 6 {
		t.Fatalf("Expected 6 - got %d", v)
	}

	tasks = []byte{65, 65, 65, 66, 66, 66}
	n = 3
	v = solution.LeastInterval(tasks, n)
	if v != 10 {
		t.Fatalf("Expected 8 - got %d", v)
	}
}

func TestPivotSearch(t *testing.T) {
	nums := []int{1, 3, 5}
	v := solution.Search(nums, 0)
	if v != -1 {
		t.Fatalf("Expected -1, but got %d", v)
	}

	nums = []int{2, 3, 4, 5, 1}
	v = solution.Search(nums, 1)
	if v != 4 {
		t.Fatalf("Expected 4, but got %d", v)
	}

	nums = []int{2, 4, 7, 9, 0}
	v = solution.Search(nums, 9)
	if v != 3 {
		t.Fatalf("Expected 3, but got %d", v)
	}

	nums = []int{5, 7, 8, 0, 3, 4}
	v = solution.Search(nums, 7)
	if v != 1 {
		t.Fatalf("Expected 1, but got %d", v)
	}

	nums = []int{1, 3, 5}
	v = solution.Search(nums, 1)
	if v != 0 {
		t.Fatalf("Expected 0, but got %d", v)
	}

	nums = []int{2, 5, 6, 0, 0, 1, 2}
	found := solution.SearchRepeats(nums, 0)
	if !found {
		t.Fatalf("Expected %t, but was %t", true, false)
	}

	nums = []int{2, 5, 6, 0, 0, 1, 2}
	found = solution.SearchRepeats(nums, 3)
	if found {
		t.Fatalf("Expected %t, but was %t", false, true)
	}

	nums = []int{2, 2, 2, 3, 2, 2, 2}
	found = solution.SearchRepeats(nums, 3)
	if !found {
		t.Fatalf("Expected %t, but was %t", true, false)
	}

	nums = []int{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1}
	found = solution.SearchRepeats(nums, 2)
	if !found {
		t.Fatalf("Expected %t, but was %t", true, false)
	}
}

func TestFindDuplicate(t *testing.T) {
	nums := []int{1, 3, 4, 2, 2}
	v := solution.FindDuplicate(nums)
	if v != 2 {
		t.Fatalf("Expected 2, but was %d", v)
	}

	nums = []int{3, 1, 3, 4, 2}
	v = solution.FindDuplicate(nums)
	if v != 3 {
		t.Fatalf("Expected 3, but was %d", v)
	}

	nums = []int{3,3,3,3,3}
	v = solution.FindDuplicate(nums)
	if v != 3 {
		t.Fatalf("Expected 3, but was %d", v)
	}
}
