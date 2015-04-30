library(readr)
library(data.table)

source('parity.R')

vals <- rep(seq(0, 1, length.out = 30), each=10)

parity <- get_median_parity('data/over.csv')

plot_dat <- data.frame(parity=parity$parity, salary=vals)

g <- ggplot(plot_dat, aes(x=vals, y=parity)) + 
	geom_point() + 
	stat_smooth(method='loess') +
	labs(title='% Over vs Parity', 
		 y='Parity', 
		 x='% of Teams Over Salary Cap')
print(g)

print(summary(lm(parity~vals, plot_dat)))
