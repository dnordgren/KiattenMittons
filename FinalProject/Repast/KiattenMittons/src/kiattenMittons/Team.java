package kiattenMittons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kiattenMittons.Helpers.WeightScaler;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import repast.simphony.engine.schedule.ScheduledMethod;

public class Team {
	private TeamName teamName;
	private List<Player> players;
	private List<Double> powerIndices;
	
	// Weights based on the proportions of minutes given to actual NBA players
	private static final double[] PLAYER_WEIGHTS = {
		0.10288684, 0.09795186, 0.09288728, 0.08755351, 0.07967748,
		0.07162200, 0.06504203, 0.06070489, 0.05616697, 0.05136091,
		0.04838750, 0.04834696, 0.04645859, 0.04923016, 0.04172301
	};
	
	private static final double[] YEAR_WEIGHTS = {
		.1, .1, .2, .2, .2, .1, .1
	};
	
	/**
	 * Constructor that lets you assign a team name.
	 * @param teamName
	 */
	public Team(TeamName teamName) {
		this.teamName = teamName;
		this.players = new ArrayList<Player>();
		this.powerIndices = new ArrayList<Double>();
	}

	public TeamName getTeamName() {
		return this.teamName;
	}

	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public void addPlayer(Player player) {
		this.players.add(player);
	}

	public void removePlayer(Player player) {
		this.players.remove(player);
	}
	
	/**
	 * @return power index (weighted sum of PERs)
	 */
	private double getPowerIndex() {
		Collections.sort(players, Player.comparator);
		
		/*
		 * Right now, teams with fewer than 15 players 
		 * are just missing out on potential minutes
		 */
		double powerIndex = 0;
		for(int i = 0; i < players.size(); i++) {
			if(i < PLAYER_WEIGHTS.length) {				
				powerIndex += players.get(i).getPER() * PLAYER_WEIGHTS[i]; 
			}
			/*
			 *  teams with more than 15 players get nothing for those players,
			 *  because they should never have more than 15 players
			 */
		}
		
		return powerIndex;
	}
	
	/**
	 * Compute the contribution that this team makes to the
	 * overall league parity value 
	 * @return team contribution to parity
	 */
	public double getParityContribution() {
		
		//if no powerIndices have been saved, parity is not meaningful, so return 0
		if(powerIndices.size() == 0) {
			return 0;
		}
		
		/*
		 * either iterating through all of the weights, or all of the pairs of
		 * power indices (of which there are .size()-1), 
		 * whichever there are fewer of 
		 */
		int size = Math.min(powerIndices.size() - 1, YEAR_WEIGHTS.length);
		double[] scaledWeights = WeightScaler.scaleWeights(YEAR_WEIGHTS, size);
		
		/*
		 * summing the absolute differences between the most recent year and
		 * the previous years (up to 7), multiplied by a weight 
		 */
		double sum = 0, diff;
		for(int i = 0; i < size - 1; i++) {
			//difference between first most recent power ranking, and (i+1) seasons back 
			diff = Math.abs(powerIndices.get(size - 1) - powerIndices.get(size - (2 + i)));
			sum += diff * scaledWeights[i];
		}

		return sum;
	}

	@ScheduledMethod(start = 0, interval = LeagueBuilder.YEAR_LENGTH / LeagueBuilder.NUM_OFFSEASON_WEEKS)
	public void makeOffersToTopFreeAgents() {

	}

	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 0.0)
	public void recordPowerIndex() {
		powerIndices.add(getPowerIndex());
	}

	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 3.0)
	public void updateRoster() {
		// determine which players retired or are free agents
		ArrayList<Player> removedPlayers = new ArrayList<Player>();
		for (Player p : players) {
			if (0 == p.getYearsLeft() || null == p.getContract().getSignedTeam()) {
				removedPlayers.add(p);
			}
		}
		
		// remove the players from the team's roster
		for (Player p : removedPlayers) {
			players.remove(p);
		}
	}
}
