current_q <- 0

read_q <- function(q) {
	filename <- sprintf('q/%04d.csv', q)
	if(!file.exists(filename)) {		
		stop(sprintf("file %s not found", filename))
	} 
	read.csv(filename, header = F)
}

save_q <- function(dat, q) {
	filename <- sprintf('q/%04d.csv', q)
	write.table(dat, file = filename, row.names=F, col.names=F, sep=',')
}

compute_utility <- function(state, next_state, action, reward, alpha, beta) {
	q_mat <- read_q(current_q)
	old_utility <- q_mat[state, action]
	
	# get the best action somehow
	best_utility_for_new_state_lol_naming <- max(q_mat[next_state,])
	
	old_utility * (1 - alpha) + alpha * (reward + beta * best_utility_for_new_state_lol_naming)
}

update_table <- function(state, action, new_util) {
	q_mat <- read_q(current_q)
	q_mat[state, action] <- new_util
	save_q(q_mat, current_q + 1)
}

create_counts <- function() {
	dims <- dim(read_q(current_q))
	mat <- rep(0, prod(dims))
	dim(mat) <- dims
	df <- data.frame(mat)
	df$total <- 0
	df
}

# could be done smarter, but #YOLO
counts <- create_counts()
inc_counts <- function(state, action) {
	counts[state, action] <<- counts[state, action] + 1
	counts$total[state] <<- counts$total[state] + 1
}