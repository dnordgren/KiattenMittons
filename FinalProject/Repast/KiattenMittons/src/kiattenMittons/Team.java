package kiattenMittons;

import java.util.*;

import repast.simphony.engine.schedule.ScheduledMethod;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class Team {
	private TeamName teamName;
	private List<Player> players;
	private List<Double> powerIndices;
	
	// Weights based on the proportions of minutes given to actual NBA players
	private static final double[] weights = {
		0.10288684, 0.09795186, 0.09288728, 0.08755351, 0.07967748,
		0.07162200, 0.06504203, 0.06070489, 0.05616697, 0.05136091,
		0.04838750, 0.04834696, 0.04645859, 0.04923016, 0.04172301
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
			if(i < weights.length) {				
				powerIndex += players.get(i).getPER() * weights[i]; 
			}
			/*
			 *  teams with more than 15 players get nothing for those players,
			 *  because they should never have more than 15 players
			 */
		}
		
		return powerIndex;
	}

	// TODO: we might want to combine this with the method for removing old players so we can be sure of the order of execution 
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH)
	public void recordPowerIndex() {
		powerIndices.add(getPowerIndex());
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void doSomething() {
		System.out.println("NBA");
	}
}
