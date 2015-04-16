package kiattenMittons;

import java.util.ArrayList;
import java.util.Comparator;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import kiattenMittons.Contract;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class Player {
	private static final double[] COEFFICIENTS = {
		12671051.1, -3003452.8, 217568.5, -3483.8
	};

	private Contract contract;
	private double desperationMultiplier;
	private ArrayList<Contract> offers;
	private double per;
	private TeamName teamName;
	private double teamPreferenceFactor;
	private int yearsLeft;

	/**
	 * Constructor that lets you assign PER,
	 * for setting up the initial league
	 * @param per
	 * @param teamName
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
	 * Overloaded comparator that allows for proper ordering of Player objects.
	 */
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
	 * Adds offer to list of offers player will consider.
	 * @param contract
	 */
	public void addOffer(Contract contract) {
		this.offers.add(contract);
	}

	/**
	 * Accept the best offer if it is good enough.
	 */
	@ScheduledMethod(start = 1, interval = 1, priority = 2.0)
	public void evaluateOffers() {
		if (offers.size() == 0) {
			return;
		}
		Contract bestOffer = findBestOffer();
		boolean accept = acceptOffer(bestOffer);
		if (accept) {
			bestOffer.getSignedTeam().registerAcceptedOffer(this, bestOffer);
			this.contract = bestOffer;
		}
		resetOffers();
	}

	/**
	 * Based on a function fit on current NBA player PER
	 * and salary data, determine the expected value
	 * of this NBA player. Analogous to what players
	 * believe they deserve in a negotiation.
	 * @return
	 */
	public double getPerBasedValue() {
		double value = 0;
		
		/*
		 * the function is only trained on this range of values,
		 * so anything outside this range hits a ceiling/floor
		 */
		double localMin = 8.7349, localMax = 32.8994;
		double effectivePer = Math.max(per, localMin);
		effectivePer = Math.min(effectivePer, localMax);
		
		/*
		 * the fitted function is as follows:
		 * value = c[0] + c[1] * per + c[2] * per^2 + c[3] * per^3
		 */
		for(int i = 0; i < COEFFICIENTS.length; i++) {
			value += COEFFICIENTS[i] * Math.pow(effectivePer, i);
		}
		
		value = Math.min(value, League.CONTRACT_MAX);
		
		/*
		 * if the player's rating is below 8.73 (local min in the fitted function)
		 * replace it with a linear scale from the contract minimum to and 8.73 PER contract
		 */
		if(per < localMin) {
			return (value - League.CONTRACT_MIN) * (per / localMin) + League.CONTRACT_MIN; 
		}
		
		return value;
	}

	/**
	 * Multiplier decreases at each round (will be reset at
	 * the end of a year).
	 */
	@ScheduledMethod(start = 1, interval = 1, priority = 1.0)
	public void increaseDesperation() {
		desperationMultiplier *= .993;
	}

	/**
	 * Sets a contract for a player. This is only used initially in the LeagueBuilder.
	 * @param team
	 * @param years
	 * @param value
	 */
	public void signWithTeam(Team team, int years, double value) {
		this.contract.signWithTeam(team, years, value);
    }

	/**
	 * Updates the years left for the player, as well as the years left on its
	 * current contract.
	 */
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 1.0)
	public void updateYearsLeft() {
		// Update years left and check if the player has no more years left (exits NBA).
		if (0 == --this.yearsLeft) {
			Context<Object> context = ContextUtils.getContext(this);
            context.remove(this);
            return;
		}

		// Update years left on the contract, if it exists.
		if (null != this.contract) {
			this.contract.updateYearsRemaining();
		}

		// Increase the team preference factor each year.
		teamPreferenceFactor += (1 - teamPreferenceFactor) * 0.1;
		
		// Reset the desperation factor.
		desperationMultiplier = 1;
	}

	/**
	 * Checks if an offer is acceptable.
	 * @param offer
	 * @return true if offer is acceptable
	 */
	private boolean acceptOffer(Contract offer) {
		double expectedAmount = getPerBasedValue() * desperationMultiplier;
		// players can't be worth more than the max contract
		if (expectedAmount > kiattenMittons.League.CONTRACT_MAX) {
			expectedAmount = kiattenMittons.League.CONTRACT_MAX;
		}

		return offer.getValue() >= expectedAmount;
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
	 * Clear out offers from the previous round.
	 */
	private void resetOffers() {
		this.offers = new ArrayList<Contract>();
	}

	/**
	 * Gets the Player's contract.
	 * @return contract
	 */
	public Contract getContract() {
		return this.contract;
	}

	/**
	 * Gets the PER.
	 * @return PER
	 */
	public double getPER() {
		return per;
	}

	/**
	 * Gets the team the player is associated with.
	 * @return teamName
	 */
	public TeamName getTeamName() {
		return teamName;
	}

	/**
	 * Sets the teamName.
	 * @param teamName
	 */
	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}

	/**
	 * Gets the years the player has remaining in the league.
	 * @return yearsLeft
	 */
	public int getYearsLeft() {
		return this.yearsLeft;
	}

	/**
	 * Sets the years the player has remaining in the league.
	 * @param years
	 */
	public void setYearsLeft(int years) {
		this.yearsLeft = years;
	}
}
