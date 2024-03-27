package combinations

var sols = make([][]int, 1000)

func Choose(n int, k int) int {
	if k > n {
		return 0
	} else {
		if len(sols[n]) < 1000 {
			sols[n] = make([]int, 1000)
		}
		if sols[n][k] == 0 {
			if k == n || k == 0 {
				sols[n][k] = 1
			} else {
				sols[n][k] = Choose(n-1, k-1) + Choose(n-1, k)
			}
		}
		return sols[n][k]
	}
}