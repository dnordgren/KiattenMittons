setwd('..')
context('Borda')
test_that('Borda from H15 intro', {
	# uses the same preference order as elimination
	dat <- get_data('h15_intro_elimination.csv', T)
	winner <- get_order(dat)[1,movie]
	expect_equal(winner, 'b')
})

test_that('Borda from H15 Sensitivity', {
	dat <- get_data('sensitivity.csv', T)
	winner <- get_order(dat)[1,movie]
	expect_equal(winner, 'a')
})