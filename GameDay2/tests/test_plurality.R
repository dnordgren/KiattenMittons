setwd('..')

test_that('Basic plurality', {
	dat <- get_data('plurality.csv')
	winner <- get_order(dat)[1,movie]
	expect_equal(winner, 'Pitch Perfect')
})