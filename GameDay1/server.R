library(shiny)
source('helpers.R')
source('visualize.R')


shinyServer(function(input, output) {
	observe({
		if(input$submit == 0) {
			return()
		}
		
		isolate({
			# update qlearn
			next_utility <- compute_utility(input$s, input$nexts, input$a, input$r, input$alpha, input$beta)			
			update_table(input$s, input$a, next_utility)
			current_q <<- current_q + 1
			
			# update counts table
			inc_counts(input$s, input$a)
			output$counts <- renderTable(counts)

			# update heatmap
			output$heat <- renderPlot({
				plot_heat(current_q)
			})
			
			# get recommendation
			next_state <- input$nexts			
			output$recommendation <- renderText({
				dat <- read_q(current_q)
				maxes <- which(dat[next_state,] == max(dat[next_state,]))
				paste('Choose from actions', paste(maxes, collapse=' '))
			})
		})
	})
})