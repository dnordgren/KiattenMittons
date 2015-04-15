package kiattenMittons.LeagueGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kiattenMittons.Player;
import kiattenMittons.Helpers.WeightedProbability;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class PlayerGenerator {

	private static final int NUM_PICKS = 60; //the number of picks in the draft	
	private static Random randomGenerator = new Random();
	private static List<Player> players = PlayerFileReader.GeneratePlayers();
	private static final int[] INIT_CONTRACT_LENGTHS = {1, 2, 3, 4};
	private static final double[] INIT_CONTRACT_WEIGHTS = {1.0/3.0, 1.0/3.0, 2.0/9.0, 1.0/9.0};
	
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
	 * Simulates a draft pick by randomly generating a draft and
	 * giving the player the corresponding pick in that draft.
	 * 
	 * Doing this as opposed to selecting from one random draft to
	 * allow the exact order of player skills to be random
	 * 
	 * @param teamName
	 * @param pick
	 * @return
	 */
	public Player draftPlayer(TeamName teamName, int pick) {
		List<Player> playerSample = new ArrayList<Player>();
		
		for(int i = 0; i < NUM_PICKS; i++) {
			playerSample.add(samplePlayer());
		}
		
		Collections.sort(playerSample, Player.comparator);
		
		Player draftedPlayer = playerSample.get(pick - 1);
		draftedPlayer.setTeamName(teamName);
		
		return new Player(draftedPlayer);
	}
	
	private Player samplePlayer() {
		int index = randomGenerator.nextInt(players.size());
		return players.get(index);
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
}
