package kiattenMittons;

import java.util.*;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class Team {
	private TeamName teamName;
	private List<Player> players;
	
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
	
	@ScheduledMethod(start = 1, interval = 1)
	public void doSomething() {
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		for (Player player: players) {
			player.updateYearsLeft();
			int yearsLeft = player.getYearsLeft();
			if (yearsLeft <= 0) {
				playersToRemove.add(player);
			}
		}

		for (Player player: playersToRemove) {
			players.remove(player);
			Context<Object> context = ContextUtils.getContext(player);
			context.remove(player);
		}
	}
}
