# according to http://en.wikipedia.org/wiki/Q-learning#Initial_conditions_.28.29
# try setting value to the first reward
generate_q0 <- function(num_actions, num_states=20, value=1) {
	dat <- matrix(value, nrow=num_states, ncol=num_actions)
	write.table(dat, file = 'q/0000.csv', row.names=F, col.names=F, sep=',')
}