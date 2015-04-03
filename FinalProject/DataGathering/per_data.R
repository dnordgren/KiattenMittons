library(httr)
library(XML)
library(data.table)

get_page <- function(page_number) {
	base_url <- 'http://insider.espn.go.com/nba/hollinger/statistics/_/page/'
	per_url <- paste0(base_url, page_number)
	per_page <- content(GET(per_url))
	
	per_table <- data.table(readHTMLTable(per_page)[[1]])
	setnames(per_table, sapply(per_table[1,], as.character))
	per_table <- per_table[RK != "RK",]
	per_table[,PER:=as.numeric(as.character(PER))]
	per_table
}

# there are 8 pages
per_tables <- lapply(1:8, get_page)
per_data <- rbindlist(per_tables)