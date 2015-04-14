package kiattenMittons.LeagueGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kiattenMittons.Player;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

public class PlayerGenerator {

	private static final int NUM_PICKS = 60; //the number of picks in the draft	
	private static Random randomGenerator = new Random();
	private static List<Player> players = PlayerFileReader.GeneratePlayers();
	
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
	 * and add in the initial years left and contracts
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
	 * @return
	 */
	private static int generateYearsLeft() {
		double generated = randomGenerator.nextDouble();
		
		double sum = 0;
		for(int i = 0; i < YEAR_WEIGHTS.length; i++) {
			sum += YEAR_WEIGHTS[i];
			if(sum > generated) {
				return (i + 1);
			}
		}
		//if nothing was matched, it's the last year
		return YEAR_WEIGHTS.length - 1;
	}
}
