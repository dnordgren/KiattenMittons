# plurality
source('helpers.R')
source('vote_based.R')

plurality_data <- get_data('plurality_condorcet.csv')

get_order(plurality_data)
get_condorcet(plurality_data)
spoiler_orderings(plurality_data)
