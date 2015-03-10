library(ggplot2)
library(reshape2)

plot_heat <- function(q) {
	d <- read_q(q)
	scaled <- prop.table(as.matrix(d), 1)
	colnames(scaled) <- 1:ncol(scaled)
	dat <- melt(scaled)
	colnames(dat) <- c('state', 'action', 'value')
	g <- ggplot(dat, aes(action, state)) + 
		geom_tile(aes(fill = value), colour = "white") + 
		scale_fill_gradient(low = "white", high = "steelblue") + 
		labs(x='action', y='state') +
		scale_y_reverse() +
		ggtitle(sprintf('Q%d', q))
	g
}
