library(readr)
library(data.table)

source('parity.R')
dirs <- list.dirs('data/experiment1/', full.names = T)[-1]

parity <- rbindlist(lapply(dirs, get_box))
translate <- c('$42 mil', '$95 mil', '$125 mil', '$63 mil')
names(translate) <- dirs
parity[,type:=translate[raw_type]]
parity[,type:=factor(type, levels = c('$42 mil', '$63 mil', '$95 mil', '$125 mil'))]
g <- ggplot(parity, aes(y=parity, x=type)) + 
	geom_boxplot() + 
	labs(title='Team Salary Cap vs Parity', 
		 y='Parity', 
		 x='Salary Cap') #+ stat_smooth(aes(group=1), method='lm')
print(g)
