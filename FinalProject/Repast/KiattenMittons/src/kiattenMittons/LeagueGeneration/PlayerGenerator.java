package kiattenMittons.LeagueGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kiattenMittons.Player;
import kiattenMittons.Helpers.WeightedProbability;

public class PlayerGenerator {

	private static Random randomGenerator = new Random();
	private static List<Player> players = null;
	private static final int[] INIT_CONTRACT_LENGTHS = {1, 2, 3, 4};
	private static final double[] INIT_CONTRACT_WEIGHTS = {1.0/3.0, 1.0/3.0, 2.0/9.0, 1.0/9.0};
	private static final int DRAFT_SIZE = 90;
	private static final double TEAM_PREFERENCE_MEAN = .2;
	
	private static List<Player> getPlayers() {
		if(players == null) {
			players = PlayerFileReader.GeneratePlayers();
		}
		return players;
	}
	
	/*
	 * approximated from an exponential distribution with rate = 1/6 
	 * to maintain a relatively constant number of players in the league
	 */
	private static final double[] YEAR_WEIGHTS = {
		0.159197485, 0.134757762, 0.114069983, 0.096558156, 0.081734714,
		0.069186942, 0.058565482, 0.049574610, 0.041964001, 0.035521760,
		0.030068521, 0.025452453, 0.021545037, 0.018237480, 0.015437693,
		0.013067725, 0.011061591, 0.009363434, 0.007925976, 0.006709194
	};
	
	/**
	 * Make a shallow copy of the initial players singleton,
	 * and add in the initial years left
	 * @return Initial players
	 */
	public static List<Player> generatePlayers() {
		List<Player> newPlayers = new ArrayList<Player>(getPlayers());
		
		for(Player player : newPlayers) {
			player.setYearsLeft(generateYearsLeft());
		}
		
		return newPlayers;
	}
	
	/**
	 * Generates a random contract length to give to the
	 * initial agents in the NBA, based on the equilibrium
	 * proportions that would exist in the NBA if new 
	 * contracts are 2, 3, or 4 years with equal probability of each
	 * @return contract length
	 */
	public static int generateSeedContractLength() {
		int index = WeightedProbability.weightedSelect(INIT_CONTRACT_WEIGHTS);
		return INIT_CONTRACT_LENGTHS[index];
	}
	
	/**
	 * Samples a player at random, makes a copy, and sets up
	 * some initial values
	 * @return a random Player based on the current NBA distribution
	 */
	private static Player samplePlayer() {
		int index = randomGenerator.nextInt(getPlayers().size());
		
		Player sampled = getPlayers().get(index);
		sampled.setYearsLeft(generateYearsLeft());
		
		return new Player(sampled);
	}
	
	/**
	 * Randomly select a number of years remaining
	 * based on the set of weights defined
	 * @return number of years remaining in NBA
	 */
	public static int generateYearsLeft() {
		//returning + 1 since we want the range to start at 1
		return WeightedProbability.weightedSelect(YEAR_WEIGHTS) + 1;
	}
	
	/**
	 * Creates a random preference factor (bounded 0 to 1)
	 * @return
	 */
	public static double generatePreferenceFactor() {
		double standardDeviation = .05;
		double gaussian = randomGenerator.nextGaussian();
		
		double tpf = (gaussian * standardDeviation) + TEAM_PREFERENCE_MEAN;
		
		//keep it between 0 and 1
		tpf = Math.min(tpf, 1);
		tpf = Math.max(tpf, 0);
		return tpf;
	}
	
	/**
	 * The "draft" will simply be adding players to the pool 
	 * as free agents based on the original distribution of ratings.
	 * @return List of randomly sampled Players
	 */
	public static List<Player> generateDraftClass() {
		List<Player> draftClass = new ArrayList<Player>();
		
		for(int i = 0; i < DRAFT_SIZE; i++) {
			draftClass.add(samplePlayer());
		}
		
		return draftClass;
	}
}
