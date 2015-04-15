package kiattenMittons;

import java.util.*;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
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
	private ArrayList<Contract> offers;
	private double teamPreferenceFactor;
	private double desperationMultiplier;
	
	private static final double[] COEFFICIENTS = {
		12671051.1, -3003452.8, 217568.5, -3483.8
	};
	
	public static Comparator<Player> comparator = new Comparator<Player>() {
		public int compare(Player p1, Player p2) {
			if (p1.getPER() > p2.getPER()) {
				return -1;
			} else if (p1.getPER() < p2.getPER()) {
				return 1;
			}
			return 0;
		}
	};
	
	/**
	 * Constructor that lets you assign PER,
	 * for setting up the initial league
	 * @param per
	 */
	public Player(double per, TeamName teamName) {
		this.per = per;
		this.teamName = teamName;
		this.contract = new Contract();
		this.offers = new ArrayList<Contract>();
		this.yearsLeft = PlayerGenerator.generateYearsLeft();
		this.teamPreferenceFactor = PlayerGenerator.generatePreferenceFactor();
		this.desperationMultiplier = 1;
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
		this.offers = new ArrayList<Contract>();
		this.teamPreferenceFactor = player.teamPreferenceFactor;
		this.desperationMultiplier = player.desperationMultiplier;
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

	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 1.0)
	public void updateYearsLeft() {
		//check if they leave the NBA
		if (0 == --this.yearsLeft) {
			Context<Object> context = ContextUtils.getContext(this);
 			context.remove(this);
 			return;
		}
		
		//update years
		if (null != this.contract) {
			this.contract.updateYearsRemaining();
		}
		
		//scale up TPF slightly every year
		teamPreferenceFactor += (1 - teamPreferenceFactor) * 0.1;
		
		//reset desperation multiplier
		desperationMultiplier = 1;
	}
	
	/**
	 * Multiplier decreases at each round (will be reset at
	 * the end of a year).
	 */
	@ScheduledMethod(start = 1, interval = 1)
	public void increaseDesperation() {
		desperationMultiplier *= .99;
	}
	
	public Contract getContract() {
		return this.contract;
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
	
	public void addOffer(Contract contract) {
		this.offers.add(contract);
	}

	/**
	 * Accept the best offer if it is good enough
	 */
	public void evaluateOffers() {
		Contract bestOffer = findBestOffer();
		boolean accept = acceptOffer(bestOffer);
		if (accept) {
			bestOffer.getSignedTeam().registerAcceptedOffer(this, bestOffer);
		}
		else {
			bestOffer.getSignedTeam().registerDeclinedOffer(this, bestOffer);
		}
	}
	
	/**
	 * Compute the values of each offer, and return the one
	 * with the highest value to the player
	 * @return highest valued offer
	 */
	private Contract findBestOffer() {
		Contract bestOffer = offers.get(0), currentOffer;
		double bestOfferValue = evaluateOffer(bestOffer), currentOfferValue;
		for(int i = 1; i < offers.size(); i++) {
			currentOffer = offers.get(i);
			currentOfferValue = evaluateOffer(currentOffer);
			if(currentOfferValue > bestOfferValue) {
				bestOfferValue = currentOfferValue;
				bestOffer = currentOffer;
			}
		}
		return bestOffer;	
	}

	/**
	 * Players evaluate an offer as the combination of its value ($) and 
	 * the skill of the team offering that contract. Both of the raw values
	 * are scaled to be between 0 and 1, and then weighted by the Player's
	 * team preference factor
	 * @param offer
	 * @return offer value
	 */
	private double evaluateOffer(Contract offer) {
		//power index is scaled out of 35 to put it on the same scale as $
		double scaledTeamIndex = offer.getSignedTeam().getPowerIndex() / 35.0;
		
		double scaledContractValue = (offer.getValue() - League.CONTRACT_MIN) / 
				(League.CONTRACT_MAX - League.CONTRACT_MIN);
		
		double offerValue = teamPreferenceFactor * scaledTeamIndex + 
				(1 - teamPreferenceFactor) * scaledContractValue;
		
		return offerValue;
	}

	public void endOffseason() {
		this.offers = new ArrayList<Contract>();
	}

	/**
	 * Checks if an offer is acceptable
	 * @param offer
	 * @return true if offer is acceptable
	 */
	private boolean acceptOffer(Contract offer) {
		double expectedAmount = getPerBasedValue() * desperationMultiplier;
		return offer.getValue() >= expectedAmount;
	}
}
