setwd('..')
context('Pairwise')

test_that("Pairwise elimination", {
	pairwise_data <- get_data('pairwise.csv', rank_flip = T)
	source('pairwise_helpers.R', local=T)
	
	order <- get_order(pairwise_data)[,movie]
	expect_equal(order, c('h', 'a', 'e', 'c', 'g', 'f', 'd', 'b'))
})