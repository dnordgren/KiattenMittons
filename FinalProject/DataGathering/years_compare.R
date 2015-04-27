library(httr)
library(data.table)
library(XML)
library(ggplot2)

years <- as.numeric(readLines('sim_years.txt'))
years <- years[years > 0]
years <- tail(years, n=400)


get_page <- function(page_number) {
	base_url <- 'http://insider.espn.go.com/nba/hollinger/statistics/_/page/%d/qualified/false'
	per_url <- sprintf(base_url, page_number)
	per_page <- content(GET(per_url))
	links <- unlist(xpathApply(per_page, '//a', function(x) {xmlAttrs(x)['href']}))
	links <- links[grep('playerId', links)]
	
	sapply(links, function(l) {
		pg <- content(GET(l))
		xpathApply(pg, "//li/span[text()='Experience']/..", function(x) {xmlValue(x, recursive=F)})
	})
}

# there are 10 pages
exps <- lapply(1:10, get_page)
exps <- unlist(exps)
real_years <- as.numeric(regmatches(exps, regexpr('^[0-9]*', es)))
hist(real_years)
hist(years)


dat <- data.frame(years=c(real_years, years-17), type=c(rep(0, length(real_years)), rep(1, length(years))))
ggplot(dat, aes(years, group=type, fill=factor(type))) + geom_density(alpha=.2)

