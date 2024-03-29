setwd('..')
context('Plurality')
test_that('Basic plurality', {
	dat <- get_data('plurality.csv')
	winner <- get_order(dat)[1,movie]
	expect_equal(winner, 'Pitch Perfect')
})

test_that('H15I plurality', {
	dat <- get_data('h15_intro_plurality.csv')
	winner <- get_order(dat)[1,movie]
	expect_equal(winner, 'a')
})