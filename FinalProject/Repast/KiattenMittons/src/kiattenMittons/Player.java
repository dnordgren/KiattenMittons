package kiattenMittons;

import java.util.Comparator;

import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import kiattenMittons.Contract;

public class Player {
	private double per;
	private TeamName teamName;
	private int yearsLeft;
	private Contract contract;
	
	private static final double[] COEFFICIENTS = {
		12671051.1, -3003452.8, 217568.5, -3483.8
	};
	
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
	public Player(double per, TeamName teamName, int yearsLeft) {
		this.per = per;
		this.teamName = teamName;
		this.yearsLeft = yearsLeft;
		this.contract = new Contract();
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
	
	public void setYearsLeft(int years) {
		this.yearsLeft = years;
	}

	public void updateYearsLeft() {
		--this.yearsLeft;
	}
	
	public Contract getContract() {
		return this.contract;
	}
	
	public void updateContract() {
		if (null != this.contract) {
			this.contract.updateYearsRemaining();
		}
	}
	
	public void signWithTeam(Team team, int years, double value) throws Exception{
		if(this.contract.getSignedTeam() == null) {			
			this.contract.signWithTeam(team, years, value);
		} else {
			throw new Exception("Attempting to sign a player with an existing contract");
		}
    }
	
	/**
	 * Based on a function fit on current NBA player PER
	 * and salary data, determine the expected value
	 * of this NBA player
	 * @return
	 */
	public double getPerBasedValue() {
		double value = 0;
		
		/*
		 * the fitted function is as follows:
		 * value = c[0] + c[1] * per + c[2] * per^2 + c[3] * per^3
		 */
		for(int i = 0; i < COEFFICIENTS.length; i++) {
			value += COEFFICIENTS[i] * Math.pow(per, i);
		}
		
		return value;
	}
}
