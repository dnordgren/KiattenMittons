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

players <- read_csv('tmp.csv', col_names=c('team', 'per'))
players <- data.table(players)

salary_per <- data.table(salary=salary_table$SALARY[1:nrow(players)], per=players$per)

# sorted pers vs sorted salaries
g <- ggplot(salary_per, aes(x=per, y=salary)) + geom_point()
print(g)

# lm with polynomial factors is pretty significant
salary_model <- lm(salary~per+I(per^2)+I(per^3), data=salary_per)
print(summary(salary_model))


per_range <- seq(min(salary_per$per), max(salary_per$per), by=.1)

line_data <- data.frame(per=per_range)
line_data$salary <- predict(salary_model, newdata=line_data)
fitted_g <- ggplot(salary_per, aes(x=per, y=salary)) + geom_point() +
	geom_line(data=line_data, aes(x=per, y=salary)) + labs(title='Player Value', x='PER', y='Salary')
print(fitted_g)
