setwd('..')
context('Spoiler')
test_that('Borda from H15 Sensitivity', {
	dat <- get_data('sensitivity.csv', T)
	spo <- spoiler_orderings(dat)
	winner_c <- as.character(spo$c[1])
	# confirms that Borda will pick b if c is taken out
	expect_equal(winner_c, 'b')
})