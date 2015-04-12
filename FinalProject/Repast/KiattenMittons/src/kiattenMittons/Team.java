package kiattenMittons;

import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class Team {
	private TeamName teamName;
	
	/**
	 * Constructor that lets you assign a team name.
	 * @param teamName
	 */
	public Team(TeamName teamName) {
		this.teamName = teamName;
	}
	
	public TeamName getTeamName() {
		return this.teamName;
	}
	
	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}
}
