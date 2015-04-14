package kiattenMittons;

import java.util.Comparator;

import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import kiattenMittons.Contract;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class Player {
	private double per;
	private TeamName teamName;
	private int yearsLeft;
	private Contract contract;
	
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
	 * Constructor that lets you assign PER,
	 * for setting up the initial league
	 * @param per
	 */
	public Player(double per, TeamName teamName, int yearsLeft, Contract contract) {
		this.per = per;
		this.teamName = teamName;
		this.yearsLeft = yearsLeft;
		this.contract = contract;
	}
	
	/**
	 * Copy constructor
	 * @param player to be copied
	 */
	public Player(Player player) {
		this.per = player.per;
		this.yearsLeft = player.yearsLeft;
		this.teamName = player.teamName;
		this.contract = player.contract;
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

	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 1.0)
	public void updateYearsLeft() {
		if (0 == --this.yearsLeft) {
			Context<Object> context = ContextUtils.getContext(this);
 			context.remove(this);
		}
	}
	
	public Contract getContract() {
		return this.contract;
	}
	
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 2.0)
	public void updateContract() {
		this.contract.updateYearsRemaining();
	}
	
	public void signWithTeam(Team team, int years, double value) {
        this.contract.signWithTeam(team, years, value);
    }
}