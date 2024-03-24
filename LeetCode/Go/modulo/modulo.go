package modulo

var mod = int64(1000000007)

func ModularAdd(a int, b int) int {
	a_long := int64(a)
	b_long := int64(b)
	return int(((a_long % mod) + (b_long % mod)) % mod)
}

func ModularMultiply(a int, b int) int {
	a_long := int64(a)
	b_long := int64(b)
	return int(((a_long % mod) * (b_long % mod)) % mod)
}

func ModularSubtract(a int, b int) int {
	a_long := int64(a)
	b_long := int64(b)
	a_long = a_long % mod
	b_long = b_long % mod
	if a_long >= b_long {
		return int(a_long - b_long)
	} else {
		return int((a_long + (mod - b_long)) % mod)
	}
}