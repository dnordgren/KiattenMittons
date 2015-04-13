# cumulative

source('helpers.R')
source('vote_based.R')

cumulative_data <- get_data('cumulative.csv')

ordered <- get_order(cumulative_data)
to_excel(2, ordered)
