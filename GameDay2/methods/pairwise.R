# pairwise

source('helpers.R')
source('vote_based.R')

pairwise_data <- get_data('pairwise.csv', rank_flip = T)

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

get_winner <- function(vote_data) {
	# test ordering
	order <- 1:nrow(vote_data)
	remaining_order <- order
	for(i in 1:(length(order) - 1)) {
		winner <- mano_a_mano(vote_data, remaining_order[1], remaining_order[2])
		remaining_order <- c(winner, remaining_order[c(-1, -2)])
	}
	vote_data$movie[remaining_order]
}

get_condorcet(pairwise_data)
