
library(shiny)

shinyUI(fluidPage(

  titlePanel("Game Day 1"),

  sidebarLayout(
    sidebarPanel(
		numericInput("a", label = "Action", value = 1, step = 1, min=1),
		numericInput("s", label = "State", value = 1, step = 1, min=1),
		numericInput("nexts", label = "Next State", value = 1, step = 1, min=1),
		numericInput("r", label = "Reward", value = 0, step = .1),
		br(),
		includeHTML('www/button.html'),
		
		# the defaults are .5 and .5 in QLM.java
		h5('Parameters'),
		numericInput("alpha", label="Learning Rate", value = .75, step = .01, min=0, max=1),
		numericInput("beta", label="Discount Rate", value = .5, step = .01, min=0, max=1)
    ),

    mainPanel(
    	plotOutput('heat'),
    	textOutput('recommendation'),
    	tableOutput('counts'))
  )
))
