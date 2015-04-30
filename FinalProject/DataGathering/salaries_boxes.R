library(readr)
library(data.table)

source('parity.R')

real_cap <- 6.3065E7
min_cap <- real_cap * 2 / 3
max_cap <- real_cap * 2
vals <- seq(min_cap, max_cap, length.out = 250)

parity <- get_median_parity('data/salary_cap.csv')

plot_dat <- data.frame(parity=parity$parity, salary=vals)

g <- ggplot(plot_dat, aes(x=vals, y=parity)) + 
	geom_point() + 
	stat_smooth(method='lm') +
	labs(title='Salary Cap vs Parity', 
		 y='Parity', 
		 x='Salary Cap')
print(g)

print(summary(lm(parity~vals, plot_dat)))