# cumulative

# sample has k = 5
source('helpers.R')
source('vote_based.R')

cumulative_data <- get_data('cumulative.csv')

get_order(cumulative_data)
get_condorcet(cumulative_data)
spoiler_orderings(cumulative_data)
