library(data.table)
library(readr)

get_median_parity <- function(filename) {
	parity_data <- data.table(read_csv(filename, col_names=F))
	setnames(parity_data, c('run', 'raw_parity', 'raw_tick'))
	parity_data[,parity:=as.numeric(as.character(regmatches(raw_parity, 
															regexpr('[0-9]*\\.[0-9]*', raw_parity))))]
	parity_data[,run:=regmatches(run, regexpr(' [0-9]*', run))]
	parity_data[,run:=as.numeric(as.character(run))]
	parity <- parity_data[,list(parity=median(parity)),by=run]
	parity[order(run)]
}