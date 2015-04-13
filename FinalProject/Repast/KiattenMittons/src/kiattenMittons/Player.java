package kiattenMittons;

import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class Player {
	private double per;
	private TeamName teamName;
	private int yearsLeft;
	
	/**
	 * Constructor that auto-generates player efficiency
	 */
	public Player() {
		per = Math.random();
	}
	
	/**
	 * Constructor that lets you assign PER,
	 * for setting up the initial league
	 * @param per
	 */
	public Player(double per, TeamName teamName, int yearsLeft) {
		this.per = per;
		this.teamName = teamName;
		this.yearsLeft = yearsLeft;
	}
	
	/**
	 * Copy constructor
	 * @param player to be copied
	 */
	public Player(Player player) {
		this.per = player.per;
		this.teamName = player.teamName;
	}
	
	/**
	 * Getter for PER
	 * @return PER
	 */
	public double getPER() {
		return per;
	}
	
	public TeamName getTeamName() {
		return teamName;
	}

	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}

	public int getYearsLeft() {
		return this.yearsLeft;
	}

	public void updateYearsLeft() {
		--this.yearsLeft;
	}
}
