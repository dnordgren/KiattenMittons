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
	per_table[,MPG:=as.numeric(as.character(MPG))]
	per_table[,team:=regmatches(PLAYER, regexpr('[A-Z]+$', PLAYER))]
	per_table	
}

# there are 10 pages
per_tables <- lapply(1:10, get_page)
per_data <- rbindlist(per_tables)

# players that are really bad, or don't actually play get PER = 0
per_data[PER<0 | MPG <= 6.09,PER:=0]

# take only the top 15 on each team
per_data <- per_data[order(PER, decreasing = T)]
per_data[,rank:=order(PER, decreasing = T), by=team]
per_data <- per_data[rank <= 15]


# semimanually adding back players that were traded to Boston
# since they were short of the 13 player minimum without them
traded <- per_data[grep('BOS/', PLAYER)][1:3]
traded[,team:="BOS"]
detroit <- per_data[grep('DET/', PLAYER)][1]
detroit[,team:="DET"]
per_data <- rbind(per_data, traded)
per_data <- rbind(per_data, detroit)

# write.table(per_data[,list(team, PER)], row.names = F, col.names = F, file='../Repast/KiattenMittons/resources/players.csv', sep=',', quote=F)