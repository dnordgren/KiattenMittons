setwd('..')
context('Elimination')
test_that('Elimination on the example in H15 intro', {
	dat <- get_data('h15_intro_elimination.csv', T)
	source('elimination_helper.R', local=T)
	morder <- get_order(dat)[,movie]
	expect_equal(morder, c('c', 'a', 'b'))
})