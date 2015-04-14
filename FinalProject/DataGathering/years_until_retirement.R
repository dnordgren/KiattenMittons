# Script for determining what distribution to use for years to retirement
# in order to maintain the same number of players in the league
og <- dexp(0:19, rate = 1/6)
props <- floor(og * 300)
for(i in 1:200) {
	
	props <- c(props[-1], 0)+floor(og*90)
}
print(sum(props))
