mano_a_mano <- function(vote_data, r1, r2) {
	row1 <- vote_data[r1, -1, with=F]
	row2 <- vote_data[r2, -1, with=F]
	
	# can't have ties when comparing, so I believe this inequality is correct
	pct_for1 <- mean(row1 > row2)
	
	if(pct_for1 > .5) {
		return(r1)
	} else if (pct_for1 < .5) {
		return(r2)
	}
	
	# tiebreaker
	# first alphabetically wins
	if(vote_data$movie[r1] < vote_data$movie[r2]) {
		return(r1)
	}
	r2
}

get_order <- function(vote_data, base_order=NULL) {
	# test ordering
	out_order <- c()
	if(is.null(base_order)) {
		base_order <- 1:nrow(vote_data)
	}
	remaining_order <- base_order
	while(length(remaining_order)>1) {
		winner <- mano_a_mano(vote_data, remaining_order[1], remaining_order[2])
		loser <- ifelse(winner==remaining_order[1], remaining_order[2], remaining_order[1])
		out_order <- c(vote_data$movie[loser], out_order)
		remaining_order <- c(remaining_order[c(-1, -2)], winner)
	}
	out_order <- c(vote_data$movie[remaining_order], out_order)
	data.table(movie=out_order)
}