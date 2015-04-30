library(readr)
library(data.table)

source('parity.R')

vals <- seq(0, 1, length.out = 201)

parity <- get_median_parity('data/tpf.csv')

plot_dat <- data.frame(parity=parity$parity, salary=vals)

g <- ggplot(plot_dat, aes(x=vals, y=parity)) + 
	geom_point() + 
	stat_smooth(method='lm') +
	labs(title='TPF vs Parity', 
		 y='Parity', 
		 x='Team Preference Factor (TPF)')
print(g)

print(summary(lm(parity~vals, plot_dat)))
