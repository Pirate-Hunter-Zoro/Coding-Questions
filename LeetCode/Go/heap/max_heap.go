package heap

// MaxHeap struct has a slice that holds the array
type MaxHeap struct {
	array []int
}

// Insert adds an element to the heap
func (h *MaxHeap) Insert(key int) {
	h.array = append(h.array, key)
	h.maxHeapifyUp(len(h.array) - 1)

}

// Helps to fix the heap after inserting a value at the bottom
func (h *MaxHeap) maxHeapifyUp(idx int) {
	// If I am bigger than my parent, I switch places with my parent, and go from there
	if idx == 0 {
		return
	}
	parent_idx := parent(idx)
	for h.array[idx] > h.array[parent_idx] {
		h.swap(idx, parent_idx)
		idx = parent_idx
		parent_idx = parent(idx)
	}
}

// Parent of the given index
func parent(i int) int {
	return (i - 1) / 2
}

// Left child of the given index
func left(i int) int {
	return 2*i + 1
}

// Right child of the given index
func right(i int) int {
	return 2 * (i + 1)
}

// Swap two values in the underlying array
func (h *MaxHeap) swap(i1, i2 int) {
	h.array[i1], h.array[i2] = h.array[i2], h.array[i1]
}

// Extract returns the largest key, and removes it from the heap
func (h *MaxHeap) Extract() int {
	v := h.array[0]
	last := len(h.array) - 1

	h.array[0] = h.array[last]
	h.array = h.array[:last]

	h.maxHeapifyDown(0)

	return v
}

// Helps to fix the heap after replacing a value at the root
func (h *MaxHeap) maxHeapifyDown(idx int) {
	if right(idx) < len(h.array) && (h.array[right(idx)] > h.array[idx] || h.array[left(idx)] > h.array[idx]) {
		if h.array[right(idx)] > h.array[left(idx)] {
			h.swap(right(idx), idx)
			h.maxHeapifyDown(right(idx))
		} else {
			h.swap(left(idx), idx)
			h.maxHeapifyDown(left(idx))
		}
	} else if left(idx) < len(h.array) && (h.array[left(idx)] > h.array[idx]) {
		h.swap(left(idx), idx)
		h.maxHeapifyDown(left(idx))
	}
}

// Peek the highest value on top without removing it
func (h *MaxHeap) Peek() int {
	return h.array[0]
}