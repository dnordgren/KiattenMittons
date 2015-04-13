package kiattenMittons;

import java.util.Comparator;

import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class Player {
	private double per;
	private TeamName teamName;
	
	//TODO: make sure that this is sorting correctly
	public static Comparator<Player> comparator = new Comparator<Player>() {
		public int compare(Player p1, Player p2) {
			if (p1.getPER() > p2.getPER()) {
				return 1;
			} else if (p1.getPER() < p2.getPER()) {
				return -1;
			}
			return 0;
		}
	};
	
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
	public Player(double per, TeamName teamName) {
		this.per = per;
		this.teamName = teamName;
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
}
