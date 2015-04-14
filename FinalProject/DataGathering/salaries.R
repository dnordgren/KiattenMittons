library(httr)
library(XML)
library(data.table)
library(readr)

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

players <- read_csv('../Repast/KiattenMittons/resources/players.csv', col_names=c('team', 'per'))
players <- data.table(players)

salary_per <- data.table(salary=salary_table$SALARY[1:nrow(players)], per=players$per)

# sorted pers vs sorted salaries
g <- ggplot(salary_per, aes(x=per, y=salary)) + geom_point()
print(g)
