# methods for vote based voting methods
library(gtools)

# gets the preference order, ties broken by alphabetical order
get_order <- function(vote_data) {
	vote_data$votes <- rowSums(vote_data[,-1,with=F])
	vote_data[order(votes, -tolower(movie), decreasing = T), list(movie, votes)]
}

get_condorcet <- function(vote_data) {
	
	half_prefer <- function(row_num) {
		others <- (1:nrow(vote_data))[-row_num]
		
		current_row <- vote_data[row_num, -1, with=F]
		prefered_over <- sapply(others, function(other) {
			other_row <- vote_data[other, -1, with=F]
			mean(current_row > other_row) >= .5
		})
		all(prefered_over)
	}
	
	condorcet <- which(sapply(1:nrow(vote_data), half_prefer))
	if(length(condorcet)) {
		return(vote_data$movie[condorcet])
	}
	return("No condorcet winner")
}

spoiler_orderings <- function(vote_data) {
	dat <- data.frame(original=get_order(vote_data)[,movie])
	
	missings <- sapply(1:nrow(vote_data), function(current_row) {
		sub_votes <- vote_data[-current_row]
		vote_cols <- colnames(sub_votes)[-1]
		# bumping up scores for non-eliminated items
		sub_votes[,(vote_cols):=lapply(.SD, function(x) {
			gap <- which(!(0:(nrow(vote_data)-1) %in% x)) - 1
			ifelse(x > gap, x-1, x)
		}), .SDcols=vote_cols]
		new_order <- get_order(sub_votes)
		c(new_order[,movie], NA)
	})
	colnames(missings) <- vote_data$movie
	cbind(dat, missings)
}

pareto_dominators <- function(vote_data) {
	# returns a list of pairs of row numbers, where row[1] pareto dominates row[2]
	all_pairs <- combn(vote_data$movie, 2)
	
	dominates <- apply(all_pairs, 2, function(pair) {
		# 1 pareto dominates 2 if all agents prefer 1 to 2
		all(vote_data[pair[1], -1, with=F] > vote_data[pair[2], -1, with=F])
	})
	
	dom_pairs <- all_pairs[,dominates,drop=F]
	
	if(length(dom_pairs) == 0) {
		return(list())
	}
	
	lapply(1:ncol(dom_pairs), function(ind) {dom_pairs[,ind]})
}

any_orderings <- function(vote_data) {
	source('pairwise_helpers.R', local=T)
	dominators <- pareto_dominators(vote_data)
	all_orders <- permutations(nrow(vote_data), nrow(vote_data))
	
	invisible(apply(all_orders, 1, function(morder) {
		movie_order <- get_order(vote_data, morder)[,movie]
		lapply(dominators, function(pair) {
			interesting <- which(movie_order == pair[1]) > which(movie_order == pair[2])
			if(interesting) {
				cat("Order:\n")
				cat(paste(paste('\t', morder), collapse='\n'))
				cat(sprintf("\nResults in %s finishing behind %s\n", pair[1], pair[2]))
			}
		})
	}))
}