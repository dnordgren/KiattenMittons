setwd('..')

test_that('Elimination on the example in H15 intro', {
	dat <- get_data('h15_intro_elimination.csv')
	morder <- get_order(dat)[,movie]
	expect_equal(morder, c('c', 'a', 'b'))
})