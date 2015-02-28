get_data <- (function() {
	source('app_config.R', local = T)
	data_dir <- get_config('data_dir')
	function(fname) {
		read.csv(paste0(data_dir, '/', fname))
	}
})()