# borda
source('helpers.R')
source('vote_based.R')

borda_data <- get_data('borda.csv', rank_flip = T)

ordered <- get_order(borda_data)
to_excel(4, ordered)
get_condorcet(borda_data)
spoiler_orderings(borda_data)
