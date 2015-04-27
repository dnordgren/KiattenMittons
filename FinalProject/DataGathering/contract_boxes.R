library(readr)
library(data.table)

source('parity.R')

types <- c('-2', '-1', 'Base', '+1', '+2')
parity <- get_median_parity('data/contract.csv')

parity[,type:=types[ceiling(run/50)]]
parity[,type:=factor(type, levels=types)]

g <- ggplot(parity, aes(y=parity, x=type)) + 
	geom_boxplot() + 
	labs(title='Contract Length vs Parity', 
		 y='Parity', 
		 x='Contract Length') #+ stat_smooth(aes(group=1), method='lm')
print(g)
