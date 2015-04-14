package kiattenMittons.LeagueGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kiattenMittons.Player;
import kiattenMittons.Helpers.WeightedProbability;

public class PlayerGenerator {

	private static Random randomGenerator = new Random();
	private static List<Player> players = PlayerFileReader.GeneratePlayers();
	private static final int[] INIT_CONTRACT_LENGTHS = {1, 2, 3, 4};
	private static final double[] INIT_CONTRACT_WEIGHTS = {1.0/3.0, 1.0/3.0, 2.0/9.0, 1.0/9.0};
	private static final int DRAFT_SIZE = 60;
	
	/*
	 * approximated from an exponential distribution with rate = 1/6 
	 * to maintain a relatively constant number of players in the league
	 */
	private static final double[] YEAR_WEIGHTS = {
		0.166666667, 0.141080287, 0.119421885, 0.101088443, 0.085569520,
		0.072433035, 0.061313240, 0.051900537, 0.043932856, 0.037188360,
		0.031479267, 0.026646624, 0.022555881, 0.019093141, 0.016161995,
		0.013680833, 0.011580575, 0.009802745, 0.008297845, 0.007023974
	};
	
	/**
	 * Make a shallow copy of the initial players singleton,
	 * and add in the initial years left
	 * @return Initial players
	 */
	public static List<Player> generatePlayers() {
		List<Player> newPlayers = new ArrayList<Player>(players);
		
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
		int index = randomGenerator.nextInt(players.size());
		
		Player sampled = players.get(index);
		sampled.setYearsLeft(generateYearsLeft());
		
		return new Player(sampled);
	}
	
	/**
	 * Randomly select a number of years remaining
	 * based on the set of weights defined
	 * @return number of years remaining in NBA
	 */
	private static int generateYearsLeft() {
		//returning + 1 since we want the range to start at 1
		return WeightedProbability.weightedSelect(YEAR_WEIGHTS) + 1;
	}
	
	/**
	 * The "draft" will simply be adding 60 (2 "rounds" for 
	 * each team) players to the pool as free agents
	 * based on the original distribution of ratings.
	 * @return
	 */
	public static List<Player> generateDraftClass() {
		List<Player> draftClass = new ArrayList<Player>();
		
		for(int i = 0; i < DRAFT_SIZE; i++) {
			draftClass.add(samplePlayer());
		}
		
		return draftClass;
	}
}
