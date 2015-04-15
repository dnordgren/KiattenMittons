# Script for determining what distribution to use for years to retirement
# in order to maintain the same number of players in the league
og <- dexp(0:19, rate = 1/6)
props <- og
for(i in 1:20) {
	
	props <- c(props[-1], 0)+og*1/6
}
print(sum(props))
print(og/sum(og))