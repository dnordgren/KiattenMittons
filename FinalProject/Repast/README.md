# Kiatten Mittons - NBA Simulation

Our project is a multiagent system that simulates the NBA. The main agents in the system are the Players and the Teams. Both of these are modeled after real NBA players and teams. There is also a League agent that handles the draft (our way of entering new agents into the system). One tick represents a unit of time in the offseason. At each tick, each team can offer one player. After each tick, a player looks at all the offers presented, and can either choose the best one or pass. After 100 ticks, the offseason ends and teams Power Indices are immediately calculated and stored. At this point, players age by a year and contracts decrease by a year, so players may leave and become free agents or retire. The process then repeats.

## Running the Simulation
1. Open Eclipse
2. Set workspace to '{directory-containing-project}/KiattenMittons/Repast'
3. Run 'KiattenMittons Model'
4. When the Repast Simphony GUI has appeared, click 'Start'
5. Configurable system inputs can be changed in the 'Parameters' tab (see Parameters section)
6. Use the buttons at the bottom to select the chart to display (see Charts section)
7. The simulation will save four .csv files that are logs from each run. We will use these to analyze the data in our report.

## Parameters
- Contract adjustment - increase or decrease the length of contracts given out
   - Value: an integer between -2 and 2 (inclusive)
- Max individual contract size - default is the actual max contract size in the NBA (subject to special exceptions)
   - Value: double
- Min individual contract size - default is the actual min contract size in the NBA
   - Value: double
- Max team salary cap overage - percentage that a team is willing to go over the salary cap by
   - Value: double [0, 1]
- Percentage of teams willing to exceed the salary cap
   - Value: double [0, 1]
- Proportion of risky teams - percentage of teams that will try to bid lower than a Player's value
   - Value: double [0, 1]
- Risk percentage (for risky teams) - amount below a Player's value a risky team is willing to bid
   - Value: double [0, 1]
- Salary cap - default is the actual team salary cap for the NBA
   - Value: double
- Team preference factor - weight a player gives to how good a team is when considering offers
   - Value: double [0, 1]

## Charts
- League Parity
   - Plots the parity of the league as a whole over time. Parity is the measure of how much change there is year to year among the skill levels of teams in the NBA.
- Player Count
   - Monitors the number of players in the system. This is important to make sure that players enter and exit the NBA at a rate such that the number of players stays relatively constant.
- Team Players
   - This chart displays the average number of players signed to each team. You can use it to see when players leave at the start of the offseason, and are signed as the offseason progresses.
- Team Power Index
   - Shows the standard deviation of the team's Power Indices at any point in time. This shows how spread out the ratings are (low if teams are generally grouped in the middle, high if some teams are highly skilled while others are weak).

