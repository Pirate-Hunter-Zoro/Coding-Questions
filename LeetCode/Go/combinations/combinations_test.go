package combinations

import (
	"testing"
)

func TestChoose(t *testing.T) {
	v := Choose(2, 2)
	if v != 1 {
		t.Fatalf("Expected 1, but got %d", v)
	}

	v = Choose(5, 0)
	if v != 1 {
		t.Fatalf("Expected 1, but got %d", v)
	}

	v = Choose(10, 3)
	if v != 120 {
		t.Fatalf("Expected 120, but got %d", v)
	}

	v = Choose(4, 5)
	if v != 0 {
		t.Fatalf("Expected 0, but got %d", v)
	}
}