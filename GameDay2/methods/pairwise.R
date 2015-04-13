# pairwise

source('helpers.R')
source('vote_based.R')

pairwise_data <- get_data('pairwise.csv', rank_flip = T)

# NOTE: overwrites the normal "get_order" function on source
source('pairwise_helpers.R')

ordered <- get_order(pairwise_data)
to_excel(6, ordered)