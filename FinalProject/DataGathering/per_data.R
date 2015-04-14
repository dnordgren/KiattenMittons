library(httr)
library(XML)
library(data.table)

get_page <- function(page_number) {
	base_url <- 'http://insider.espn.go.com/nba/hollinger/statistics/_/page/%d/qualified/false'
	per_url <- sprintf(base_url, page_number)
	per_page <- content(GET(per_url))
	
	per_table <- data.table(readHTMLTable(per_page)[[1]])
	setnames(per_table, sapply(per_table[1,], as.character))
	per_table <- per_table[RK != "RK",]
	per_table[,PER:=as.numeric(as.character(PER))]
	per_table[,team:=regmatches(PLAYER, regexpr('[A-Z]+$', PLAYER))]
	per_table	
}

# there are 10 pages
per_tables <- lapply(1:10, get_page)
per_data <- rbindlist(per_tables) 

# write.csv(per_data[,list(team, PER)], row.names = F, col.names = F, file='../Repast/KiattenMittons/resources/players.csv')