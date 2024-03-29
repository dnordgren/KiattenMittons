setwd('..')

context('Approval')
test_that("Basic approval", {
	dat <- get_data('approval.csv')
	order <- get_order(dat)[,movie]
	expect_equal(order, c('a', 'c', 'b'))	
})

test_that("Approval with ties", {
	dat <- get_data('approval_ties.csv')
	order <- get_order(dat)[,movie]
	expect_equal(order, c('a', 'b', 'c', 'd'))	
})