# approval
source('helpers.R')
source('vote_based.R')

approval_data <- get_data('approval.csv')

ordered <- get_order(approval_data)
to_excel(3, ordered)
