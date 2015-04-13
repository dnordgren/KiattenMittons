
source('per_data.R')

nth <- function(v, n) {
	if(length(v) < n + 1) {
		return(NA_real_)
	}
	v[order(v)][length(v)-n]
}

dts <- rbindlist(lapply(0:14, function(x) {
	dt <- per_data[,nth(MPG, x),by=team]
	setnames(dt, c('team', 'mpg'))
	dt[,rank:=x+1]
	dt
}))

dts[,list(avg_min=mean(mpg, na.rm=T)/(48*5)),by=rank]
