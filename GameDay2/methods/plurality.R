# plurality
source('helpers.R')
plurality_data <- get_data('plurality.csv')

# gets the preference order, ties broken by alphabetical order
get_order <- function() {
	plurality_data$votes <- rowSums(plurality_data[,-1,with=F])
	plurality_data[order(votes, -tolower(movie), decreasing = T), list(movie, votes)]
}