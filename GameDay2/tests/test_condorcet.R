setwd('..')
context('Condorcet')
test_that("Condorcet winner from H15 intro", {
	# uses the same preference order as elimination
	dat <- get_data('h15_intro_elimination.csv', T)
	winner <- get_condorcet(dat)
	expect_equal(winner, 'b')
})