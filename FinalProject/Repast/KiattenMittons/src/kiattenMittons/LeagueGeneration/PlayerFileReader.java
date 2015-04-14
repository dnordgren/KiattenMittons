package kiattenMittons.LeagueGeneration;

import kiattenMittons.Player;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PlayerFileReader {

	private static final String DEFAULT_PLAYER_FILE = "resources/players.csv";
	
	public static List<Player> GeneratePlayers() {
		return GeneratePlayers(DEFAULT_PLAYER_FILE);
	}
	
	public static List<Player> GeneratePlayers(String playerCsv) {
		List<Player> players = new ArrayList<Player>();
		
		File playerFile = new File(playerCsv);
		try {
			Scanner playerScanner = new Scanner(playerFile);
			
			String playerLine;
			while(playerScanner.hasNext()) {
				playerLine = playerScanner.next();
				players.add(parsePlayerLine(playerLine));
			}
			
			playerScanner.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Error initializing the players.");
			e.printStackTrace();
		}
		
		return players;
	}
	
	/**
	 * Parses a line from the players.csv into a Player object
	 * @param playerLine
	 * @return Player
	 */
	private static Player parsePlayerLine(String playerLine) {
		
		StringTokenizer tokens = new StringTokenizer(playerLine, ",");
		
		TeamName teamName = TeamName.valueOf(tokens.nextToken());
		double per = Double.parseDouble(tokens.nextToken());
		Random rn = new Random();
		int yearsLeft = rn.nextInt(10) + 1;
		
		return new Player(per, teamName, yearsLeft, null);
	}
}
