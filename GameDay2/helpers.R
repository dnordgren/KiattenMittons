library(data.table)

get_data <- (function() {
	source('app_config.R', local = T)
	data_dir <- get_config('data_dir')
	function(fname, rank_flip=F) {
		vote_data <- fread(paste0(data_dir, '/', fname))
		
		if(rank_flip) {
			vote_cols <- colnames(vote_data)[-1]
			n <- nrow(vote_data)
			vote_data[,(vote_cols):=lapply(.SD, function(x){n - x}), .SDcols=vote_cols]
		}
		setkey(vote_data, 'movie')
		vote_data
	}
})()

to_excel <- function(round, movies) {
	source('app_config.R', local=T)
	team_name <- get_config('team_name')
	filename <- sprintf('Round%s_%s.xlsx', round, team_name)
	write.xlsx(x = movies, file = filename,
			   sheetName = sprintf('Round%s', round), row.names = FALSE)
}