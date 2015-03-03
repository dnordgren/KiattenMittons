# plurality
source('helpers.R')
source('vote_based.R')

plurality_data <- get_data('Voting.csv')

ordered <- get_order(plurality_data)
to_excel(1, ordered)
