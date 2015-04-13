library(httr)
library(XML)

get_experiences <- function(page_number) {
	base_url <- 'http://insider.espn.go.com/nba/hollinger/statistics/_/page/'
	per_url <- paste0(base_url, page_number)
	per_page <- content(GET(per_url))
	
	player_urls <- sapply(xpathApply(per_page, '//td/a', xmlAttrs), function(x) {x['href']})
	player_urls <- player_urls[grep('playerId', player_urls)]
	
	experiences <- lapply(player_urls, function(purl) {
		player_page <- content(GET(purl))	
		exp <- xpathApply(player_page, 
						  "//ul/li/span[text() = 'Experience']/../text()", 
						  xmlValue)[[1]]
	})
	do.call('c', experiences)
}
experiences <- sapply(1:8, get_experiences)
experiences <- do.call('c', experiences)
experiences <- as.numeric(regmatches(x, regexpr('[0-9]{1,2} ', x)))
