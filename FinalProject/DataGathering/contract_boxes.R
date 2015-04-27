library(readr)
library(data.table)

source('parity.R')
dirs <- list.dirs('data/experiment2/', full.names = T)[-1]

parity <- rbindlist(lapply(dirs, get_box))
translate <- c('Average', '-1', '-2', '+1', '+2')
names(translate) <- dirs
parity[,type:=translate[raw_type]]
parity[,type:=factor(type, levels = c('-2', '-1', 'Average', '+1', '+2'))]
g <- ggplot(parity, aes(y=parity, x=type)) + 
	geom_boxplot() + 
	labs(title='Contract Length vs Parity', 
		 y='Parity', 
		 x='Contract Length') #+ stat_smooth(aes(group=1), method='lm')
print(g)
