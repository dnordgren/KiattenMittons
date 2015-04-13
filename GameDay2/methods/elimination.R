# plurality with elimination
# Hey, this one's actually interesting to code

source('helpers.R')
source('vote_based.R')

elimination_data <- get_data('elimination.csv', rank_flip = T)

source('elimination_helper.R')

get_condorcet(elimination_data)
get_order(elimination_data)
spoiler_orderings(elimination_data)
