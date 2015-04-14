library(httr)
library(XML)
library(data.table)

url_format <- 'http://espn.go.com/nba/salaries/_/page/%d'

get_salary_table <- function(page_number) {
	
	page_url <- sprintf(url_format, page_number)
	page_content <- content(GET(page_url))
	salary_table <- readHTMLTable(page_content)[[1]]
	salary_table <- data.table(salary_table)
	salary_table <- salary_table[RK != "RK"]
	salary_table[,SALARY:=gsub('\\$', '', SALARY)]
	salary_table[,SALARY:=gsub(',', '', SALARY)]
	salary_table[,SALARY:=as.numeric(SALARY)]
	salary_table
}


# there are 11 pages
salary_tables <- lapply(1:11, get_salary_table)
salary_table <- rbindlist(salary_tables)
