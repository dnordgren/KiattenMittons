# approval
source('helpers.R')
source('vote_based.R')

approval_data <- get_data('approval.csv')

get_order(approval_data)
get_condorcet(approval_data)
spoiler_orderings(approval_data)
