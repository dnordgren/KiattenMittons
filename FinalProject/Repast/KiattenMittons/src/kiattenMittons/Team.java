package kiattenMittons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kiattenMittons.Helpers.ProspectivePlayer;
import kiattenMittons.Helpers.WeightScaler;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;

public class Team {
	// Weights based on the proportions of minutes given to actual NBA players
	private static final double[] PLAYER_WEIGHTS = {
		0.10907940, 0.10360217, 0.09810379, 0.09254197, 0.08694842,
		0.07767519, 0.07116173, 0.06405613, 0.05850488, 0.05406388,
		0.04668336, 0.04147048, 0.03597209, 0.03222165,	0.02791485
	};
	private static final double[] YEAR_WEIGHTS = {
		.1, .1, .2, .2, .2, .1, .1
	};

	private double dollarsSpentThisYear;
	private List<Player> players;
	private List<Double> powerIndices;
	private double riskAmount;
	private double salaryCapOverage;
	private TeamName teamName;

	/**
	 * Constructor that lets you assign a team name.
	 * @param teamName
	 */
	public Team(TeamName teamName) {
		this.teamName = teamName;
		this.players = new ArrayList<Player>();
		this.powerIndices = new ArrayList<Double>();
		
		assignRiskAmount();

		//set up willingness to go over the salary cap
		Random random = new Random();
		double maxOverage = (Double)RunEnvironment.getInstance().getParameters().getValue("maxTeamSalaryCapOverage");
		double probabilityOfOver = (Double)RunEnvironment.getInstance().getParameters().getValue("percentTeamsWillingOverSalaryCap");
		if(random.nextDouble() < probabilityOfOver) {
			this.salaryCapOverage = random.nextDouble() * maxOverage * League.SALARY_CAP; 
		} else {
			this.salaryCapOverage = 0;
		}
	}

	/**
	 * Adds player to the team roster.
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	/**
	 * Compute the contribution that this team makes to the
	 * overall league parity value 
	 * @return team contribution to parity
	 */
	public double getParityContribution() {
		
		// If no powerIndices have been saved, parity is not meaningful, so return 0.
		if(powerIndices.size() == 0) {
			return 0;
		}
		
		/*
		 * Either iterating through all of the weights, or all of the pairs of
		 * power indices (of which there are .size()-1), 
		 * whichever there are fewer of .
		 */
		int size = Math.min(powerIndices.size() - 1, YEAR_WEIGHTS.length);
		double[] scaledWeights = WeightScaler.scaleWeights(YEAR_WEIGHTS, size);
		
		/*
		 * Summing the absolute differences between the most recent year and
		 * the previous years (up to 7), multiplied by a weight.
		 */
		double sum = 0, diff;
		for(int i = 0; i < size - 1; i++) {
			// Difference between first most recent power ranking, and (i+1) seasons back.
			diff = Math.abs(powerIndices.get(powerIndices.size() - 1) - powerIndices.get(powerIndices.size() - (2 + i)));
			sum += diff * scaledWeights[i];
		}

		return sum;
	}

	/**
	 * @return power index (weighted sum of PERs)
	 */
	public double getPowerIndex() {
		return calculatePowerIndex(players);
	}

	/**
	 * Makes offers to the appropriate players each round.
	 */
	@ScheduledMethod(start = 1, interval = 1, priority = 0.0)
	public void makeOffers() {
		if (players.size() >= 15) {
			return;
		}

		// Need to determine which players to offer.
		List<ProspectivePlayer> playersToOffer = determinePlayersToOffer();
		determineOfferForPlayers(playersToOffer);
	}

	/**
	 * Tracks the most recent PowerIndex.
	 */
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 0.0)
	public void recordPowerIndex() {
		powerIndices.add(getPowerIndex());
	}

	/**
	 * Receiver for acceptance of an extended offer.
	 * @param player
	 * @param offer
	 */
	public void registerAcceptedOffer(Player player, Contract offer) {
		players.add(player);
		dollarsSpentThisYear += offer.getValue();
	}

	/**
	 * Removes a player from the roster.
	 * @param player
	 */
	public void removePlayer(Player player) {
		this.players.remove(player);
	}

	/**
	 * Removes players who have retired, or who had their contract expire.
	 */
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 3.0)
	public void updateRoster() {
		dollarsSpentThisYear = 0;
		// Determine which players retired or are free agents.
		List<Player> removedPlayers = new ArrayList<Player>();
		for (Player p : players) {
			if (0 == p.getYearsLeft() || (null == p.getContract() ? false : null == p.getContract().getSignedTeam())) {
				removedPlayers.add(p);
			}
			else {
				dollarsSpentThisYear += null == p.getContract() ? 0 : p.getContract().getValue();
			}
		}

		// Remove the players from the team's roster.
		for (Player p : removedPlayers) {
			players.remove(p);
		}
	}

	/**
	 * @return power index (weighted sum of PERs)
	 */
	private double calculatePowerIndex(List<Player> teamPlayers) {
		Collections.sort(teamPlayers, Player.comparator);

		/*
		 * Right now, teams with fewer than 15 players 
		 * are just missing out on potential minutes.
		 */
		double powerIndex = 0;
		for(int i = 0; i < teamPlayers.size(); i++) {
			if(i < PLAYER_WEIGHTS.length) {				
				powerIndex += teamPlayers.get(i).getPER() * PLAYER_WEIGHTS[i]; 
			}
			/*
			 *  Teams with more than 15 players get nothing for those players,
			 *  because they should never have more than 15 players.
			 */
		}
		
		return powerIndex;
	}

	/**
	 * Calculates offers to send until it finds a valid one within the constraints
	 * of the system. Then it will send that offer.
	 * @param prospectivePlayers
	 */
	private void determineOfferForPlayers(List<ProspectivePlayer> prospectivePlayers) {
		if (0 == prospectivePlayers.size()) {
			return;
		}

		ProspectivePlayer topProspect = prospectivePlayers.get(0);
		double fundsRemaining = kiattenMittons.League.SALARY_CAP + 
				salaryCapOverage - dollarsSpentThisYear;
		int spotsRemaining = 15 - players.size();
		spotsRemaining = Math.min(spotsRemaining, prospectivePlayers.size());

		double valueAddedByTopPlayers = 0;
		for (int i = 0; i < spotsRemaining; i++) {
			valueAddedByTopPlayers += prospectivePlayers.get(i).getValueAdded();
		}

		// Determine offer for most top prospect.
		double topProspectOffer = (topProspect.getValueAdded() / valueAddedByTopPlayers) * fundsRemaining;
		// Offer can't be greater than max salary.
		if (topProspectOffer > kiattenMittons.League.CONTRACT_MAX) {
			topProspectOffer = kiattenMittons.League.CONTRACT_MAX;
		}

		double offerDiscount = getOfferDiscountFactor();
		double topProspectExpectedReserve = topProspect.getPlayer().getPerBasedValue() * offerDiscount;

		// Verify that we're playing the least best prospect at least the minimum salary.
		double worstProspectOffer = (prospectivePlayers.get(spotsRemaining - 1).getValueAdded() / valueAddedByTopPlayers) * fundsRemaining;
		if (topProspectOffer >= topProspectExpectedReserve &&
			worstProspectOffer >= kiattenMittons.League.CONTRACT_MIN) {
			int contractLength = kiattenMittons.LeagueGeneration.PlayerGenerator.generateContractYearsLeft();
			if (contractLength > topProspect.getPlayer().getYearsLeft()) {
				contractLength = topProspect.getPlayer().getYearsLeft();
			}
			topProspect.getPlayer().addOffer(new Contract(this, topProspectOffer, contractLength));
			return;
		} else {
			prospectivePlayers.remove(0);
			determineOfferForPlayers(prospectivePlayers);
		}
	}
	
	private void assignRiskAmount() {
		Random random = new Random();
		double riskyProportion = (Double)RunEnvironment.getInstance().getParameters().getValue("riskyTeamProportion");
		if(random.nextDouble() < riskyProportion) {
			riskAmount = (Double)RunEnvironment.getInstance().getParameters().getValue("riskAmount");
		} else {
			riskAmount = 1;
		}
	}

	/**
	 * Finds all free agents, and calculates the value they would add to the team.
	 * @return List of ProspectivePlayers, which includes a player and value added to the
	 * PowerIndex.
	 */
	private List<ProspectivePlayer> determinePlayersToOffer() {
		// Find all free agents currently in the system.
		Iterable<Player> allPlayers = RunState.getInstance().getMasterContext().getObjects(Player.class);
		List<Player> availablePlayers = new ArrayList<Player>();
		for (Player player: allPlayers) {
			if (null == player.getContract() || null == player.getContract().getSignedTeam()) {
				availablePlayers.add(player);
			}
		}
		// Calculate the extra utility added if each prospective player were on the roster.
		List<ProspectivePlayer> prospectivePlayers = new ArrayList<ProspectivePlayer>();
		for (Player player: availablePlayers) {
			double powerDelta = getPlayerPowerDelta(player);
			ProspectivePlayer prospectivePlayer = new ProspectivePlayer(powerDelta, player);
			prospectivePlayers.add(prospectivePlayer);
		}

		// Sort prospective players by value added.
		Collections.sort(prospectivePlayers, ProspectivePlayer.comparator);
		return prospectivePlayers;
	}

	/**
	 * Based on a team's risk amount, the offer is discounted
	 * by a certain proportion
	 * @return
	 */
	private double getOfferDiscountFactor() {
		Random random = new Random();
		
		//team will low-ball an offer by up to riskAmount %
		double riskThisTime = random.nextDouble() * (1 - riskAmount);
		return 1 - riskThisTime;
	}

	/**
	 * Gets the affect a player would have on the PowerIndex.
	 * @param player
	 * @return value added to PowerIndex
	 */
	private double getPlayerPowerDelta(Player player) {
		double valueWithout = getPowerIndex();
		double valueWith = getPowerIndex(player);
		return valueWith - valueWithout;
	}

	/**
	 * @return power index (weighted sum of PERs)
	 */
	private double getPowerIndex(Player player) {
		List<Player> teamWithPlayer = new ArrayList<Player>(players);
		teamWithPlayer.add(player);
		return calculatePowerIndex(teamWithPlayer);
	}

	/**
	 * Gets the total number of players on the team.
	 * @return number of players
	 */
	public int getPlayerCount() {
		return players.size();
	}

	/**
	 * Gets the players on the team.
	 * @return players
	 */
	public List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Gets the team name.
	 * @return team name
	 */
	public TeamName getTeamName() {
		return this.teamName;
	}

	/**
	 * Sets the team name.
	 * @param teamName
	 */
	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}
}
