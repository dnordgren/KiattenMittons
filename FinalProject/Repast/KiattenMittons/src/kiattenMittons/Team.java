package kiattenMittons;

import java.util.*;

import repast.simphony.engine.schedule.ScheduledMethod;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class Team {
	private TeamName teamName;
	private List<Player> players;
	
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
	public double getPowerIndex() {
		Collections.sort(players, Player.comparator);
		
		/*
		 * Right now, teams with fewer than 15 players 
		 * are just missing out on potential minutes
		 */
		double powerIndex = 0;
		for(int i = 0; i < players.size(); i++) {
			powerIndex += players.get(i).getPER() * weights[i]; 
		}
		
		return powerIndex;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void doSomething() {
		System.out.println("NBA");
	}
}
