# plurality
source('helpers.R')
source('vote_based.R')

plurality_data <- get_data('Voting.csv')

get_order(plurality_data)
get_condorcet(plurality_data)
spoiler_orderings(plurality_data)
