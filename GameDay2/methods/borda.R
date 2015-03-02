# borda
source('helpers.R')
source('vote_based.R')

borda_data <- get_data('borda.csv', rank_flip = T)

get_order(borda_data)
get_condorcet(borda_data)
spoiler_orderings(borda_data)
