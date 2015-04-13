get_order <- function(vote_data) {
	order <- c()
	# using a for loop because dependent on previous iterations
	survivors <- copy(vote_data)
	for(i in 1:(nrow(vote_data) - 1)) {
		
		votes_vec <- apply(survivors[,-1,with=F], 2, which.max)
		votes_table <- table(votes_vec)
		votes_dt <- data.table(id=names(votes_table), votes=votes_table)
		zeroes <- (1:nrow(survivors))[!(1:nrow(survivors) %in% votes_dt$id)]
		
		if(length(zeroes) > 0) {
			votes_dt <- rbind(votes_dt, data.table(id=zeroes, votes=0))
		}
		
		# indices of items up for elimination
		up_for_elim <- as.numeric(votes_dt[which(votes == min(votes)), id])
		elim_names <- survivors$movie[up_for_elim]
		
		# in case of tie, eliminates the last alphabetically
		chosen <- elim_names[order(elim_names, decreasing = T)][1]
		order <- c(chosen, order)
		survivors <- survivors[movie != chosen]
	}
	data.table(movie=c(survivors$movie, order))
}