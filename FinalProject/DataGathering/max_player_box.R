library(ggplot2)
source('parity.R')

types <- c('$17 mil', '$19.5 mil', '$20.6 mil', '$22.5 mil', '$25 mil')
parity <- get_median_parity('data/max_player.csv')

parity[,type:=types[ceiling(run/50)]]

g <- ggplot(parity, aes(y=parity, x=type)) + 
	geom_boxplot() + 
	labs(title='Player Cap vs Parity', 
		 y='Parity', 
		 x='Player Cap') #+ stat_smooth(aes(group=1), method='lm')
print(g)
