library(data.table)

get_median_parity <- function(filename) {
	parity_data <- data.table(read_csv(filename, col_names=F))
	setnames(parity_data, c('raw_parity', 'raw_tick'))
	parity_data[,parity:=as.numeric(as.character(regmatches(raw_parity, 
															regexpr('[0-9]*\\.[0-9]*', raw_parity))))]
	parity_data[,mean(parity)]
}

get_box <- function(dir) {
	files <- list.files(dir, full.names = T)
	parity_files <- files[grep('parity', files)]
	parities <- sapply(parity_files, get_median_parity)
	data.table(raw_type=dir, parity=parities)
}
