# plurality
source('helpers.R')
plurality_data <- get_data('plurality.csv')

# gets the preference order, ties broken by alphabetical order
get_order <- function() {
	plurality_data$votes <- rowSums(plurality_data[,-1,with=F])
	plurality_data[order(votes, -tolower(movie), decreasing = T), list(movie, votes)]
}

get_condorcet <- function() {
	
	half_prefer <- function(row_num) {
		others <- (1:nrow(plurality_data))[-row_num]
		
		current_row <- plurality_data[row_num, -1, with=F]
		prefered_over <- sapply(others, function(other) {
			other_row <- plurality_data[other, -1, with=F]
			mean(current_row > other_row) >= .5
		})
		all(prefered_over)
	}
	
	condorcet <- which(sapply(1:nrow(plurality_data), half_prefer))
	if(length(condorcet)) {
		return(plurality_data$movie[condorcet])
	}
	return("No condorcet winner")
}