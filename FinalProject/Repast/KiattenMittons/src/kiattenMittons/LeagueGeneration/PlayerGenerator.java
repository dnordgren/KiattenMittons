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
	
	/**
	 * @return Shallow copy of players arraylist
	 */
	public static List<Player> generatePlayers() {
		return new ArrayList<Player>(players);
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
}
