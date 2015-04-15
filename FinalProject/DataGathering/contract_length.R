# Script to determine what the resulting proportions of contract lengths would be
# if new contracts were 2, 3, or 4 years (equally)
amt <- c(0, 120, 120, 120)
total <- sum(amt)

for(i in 1:200) {
	amt <- c(amt[-1], 0)
	temp_sum <- sum(amt)
	amt <- amt + (total-temp_sum)*c(0, 1/3, 1/3, 1/3)
}
print(amt)