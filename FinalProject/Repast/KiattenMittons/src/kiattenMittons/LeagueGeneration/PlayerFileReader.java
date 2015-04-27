package kiattenMittons.LeagueGeneration;

import kiattenMittons.Player;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PlayerFileReader {

	private static final String DEFAULT_PLAYER_FILE = "resources/players.csv";
	
	private static final String PLAYER_CSV = "NO,31.06\nOKC,28.92\nGS,28.04\nOKC,27.66\nHOU,26.81\nMIA,26.3\nCLE,26.04\nLAC,26\nSAC,25.33\nPOR,23.02\nCHI,22.89\nLAC,22.77\nBKN,22.73\nSA,22.66\nSA,22.29\nUTAH,22\nUTAH,21.66\nIND,21.63\nNY,21.59\nCLE,21.56\nORL,21.56\nMIA,21.47\nMEM,21.42\nDET,21.36\nCHI,21.3\nDET,21.27\nATL,21.26\nLAC,21.04\nPOR,20.93\nPHX,20.79\nATL,20.74\nTOR,20.67\nGS,20.65\nPHX,20.45\nNY,20.39\nUTAH,20.3\nATL,20.2\nWSH,20.15\nUTAH,20.11\nNO,20.09\nLAL,20.06\nTOR,20.03\nMIN,20.01\nUTAH,19.92\nMIA,19.89\nMIN,19.87\nDAL,19.83\nSAC,19.79\nDET,19.75\nCHA,19.73\nMEM,19.57\nPHI,19.53\nGS,19.5\nDAL,19.36\nIND,19.33\nTOR,19.28\nHOU,19.15\nBOS,19.01\nSA,19\nNO,18.85\nCLE,18.8\nMEM,18.63\nDEN,18.63\nPHX,18.58\nMEM,18.57\nDEN,18.49\nGS,18.35\nATL,18.32\nWSH,18.25\nTOR,18.19\nBKN,18.17\nBOS,18.16\nMIL,18.12\nCHI,17.98\nGS,17.92\nHOU,17.89\nCLE,17.75\nLAL,17.72\nNO,17.68\nCHA,17.61\nSAC,17.56\nNO,17.55\nPHX,17.48\nDAL,17.46\nTOR,17.43\nPOR,17.4\nNY,17.32\nMIN,17.26\nPHX,17.2\nIND,17.01\nLAL,16.97\nOKC,16.96\nBKN,16.83\nLAC,16.78\nATL,16.77\nORL,16.76\nNY,16.72\nSA,16.72\nMIN,16.7\nDAL,16.62\nOKC,16.6\nDEN,16.57\nDEN,16.54\nOKC,16.53\nMIL,16.5\nPOR,16.47\nGS,16.45\nSA,16.43\nBOS,16.41\nDAL,16.38\nLAL,16.38\nLAL,16.36\nMIN,16.36\nCHI,16.34\nLAC,16.29\nIND,16.26\nPOR,16.17\nCHI,16.16\nMIA,16.15\nGS,16.14\nATL,16.13\nORL,16.12\nSA,15.99\nBKN,15.88\nSA,15.87\nBOS,15.87\nGS,15.83\nBKN,15.76\nPHX,15.74\nNO,15.71\nMIL,15.71\nSA,15.69\nLAL,15.68\nMIL,15.62\nMEM,15.61\nCHA,15.59\nIND,15.59\nIND,15.58\nMIA,15.58\nCLE,15.55\nGS,15.5\nATL,15.48\nCHA,15.48\nNO,15.47\nDAL,15.44\nDAL,15.44\nPHX,15.43\nWSH,15.41\nIND,15.4\nCHI,15.36\nWSH,15.36\nTOR,15.35\nUTAH,15.35\nBKN,15.28\nMIL,15.27\nMIN,15.26\nPOR,15.17\nATL,15.14\nCHA,15.13\nDET,15.12\nPHX,15.09\nMIN,15.09\nMIL,15.09\nIND,15.09\nPHI,15.05\nPOR,15.05\nPHI,14.98\nSAC,14.96\nCHA,14.92\nMEM,14.91\nOKC,14.9\nDAL,14.88\nOKC,14.87\nDET,14.86\nDEN,14.85\nLAL,14.84\nORL,14.82\nHOU,14.82\nTOR,14.82\nATL,14.82\nBKN,14.66\nMEM,14.65\nPHI,14.63\nMIL,14.63\nPHI,14.62\nDEN,14.58\nUTAH,14.57\nMEM,14.55\nDAL,14.5\nSAC,14.48\nHOU,14.47\nPHI,14.44\nPHX,14.39\nWSH,14.33\nCHI,14.31\nLAL,14.29\nSA,14.27\nMIN,14.24\nWSH,14.15\nPHI,14.15\nCHA,14.13\nDET,14.13\nDAL,14.11\nDET,14.08\nOKC,14.07\nBKN,14.06\nMEM,14.06\nOKC,14.01\nORL,14\nDAL,13.94\nIND,13.94\nMIN,13.91\nMIN,13.85\nATL,13.82\nNY,13.78\nPHX,13.74\nMIL,13.55\nPHX,13.54\nTOR,13.54\nPOR,13.53\nDET,13.52\nATL,13.5\nDAL,13.45\nPOR,13.44\nIND,13.42\nWSH,13.4\nORL,13.38\nGS,13.31\nBKN,13.22\nPHX,13.22\nDEN,13.19\nCHA,13.19\nLAL,13.17\nPOR,13.17\nPOR,13.12\nUTAH,13.1\nPOR,13.09\nBOS,12.87\nNO,12.8\nHOU,12.79\nUTAH,12.79\nGS,12.78\nSAC,12.73\nWSH,12.72\nMEM,12.69\nSA,12.61\nSA,12.57\nTOR,12.57\nSA,12.54\nUTAH,12.51\nSAC,12.49\nMIL,12.47\nORL,12.46\nPHI,12.46\nMIA,12.46\nCHA,12.44\nPHX,12.43\nNY,12.43\nGS,12.42\nMIL,12.33\nOKC,12.32\nNY,12.32\nMIN,12.27\nSAC,12.27\nNY,12.27\nNY,12.16\nDEN,12.16\nMEM,12.09\nNY,12.07\nPHX,12.05\nWSH,12.01\nPHI,12.01\nMEM,12\nMEM,11.95\nBOS,11.95\nWSH,11.94\nPOR,11.84\nNY,11.82\nCHI,11.8\nCHA,11.8\nNY,11.79\nORL,11.79\nMIA,11.78\nPHI,11.74\nSAC,11.7\nCHA,11.69\nLAL,11.68\nMIA,11.61\nDEN,11.59\nDAL,11.55\nBOS,11.52\nMEM,11.49\nOKC,11.48\nGS,11.48\nHOU,11.39\nMIN,11.37\nHOU,11.35\nNY,11.35\nLAC,11.32\nBKN,11.31\nDAL,11.31\nCLE,11.29\nNO,11.27\nWSH,11.27\nTOR,11.24\nPHX,11.23\nMIL,11.21\nLAL,11.16\nNO,11.15\nNY,11.14\nHOU,11.13\nCHA,11.12\nDET,11.09\nLAC,11.08\nOKC,11.08\nDEN,11.05\nWSH,11.03\nMIN,11.02\nNY,11.02\nBKN,11.02\nCLE,10.98\nWSH,10.94\nLAL,10.92\nIND,10.88\nTOR,10.82\nPHI,10.81\nBOS,10.81\nMIA,10.74\nBKN,10.72\nPOR,10.72\nORL,10.66\nMIA,10.66\nPHI,10.65\nLAC,10.64\nMIA,10.64\nBKN,10.64\nLAC,10.62\nPHI,10.61\nBKN,10.57\nOKC,10.55\nPHI,10.54\nMIN,10.53\nCHA,10.41\nTOR,10.4\nLAC,10.36\nCHI,10.28\nDAL,10.28\nIND,10.27\nDET,10.26\nMIA,10.23\nLAL,10.23\nLAL,10.23\nCHI,10.22\nSAC,10.22\nSAC,10.15\nSA,10.14\nHOU,10.07\nMIL,10.06\nMIN,10.06\nORL,10.02\nMEM,10.02\nUTAH,9.99\nLAC,9.97\nUTAH,9.92\nNO,9.87\nATL,9.81\nATL,9.76\nATL,9.72\nLAC,9.59\nORL,9.54\nDEN,9.42\nMIA,9.29\nPOR,9.29\nOKC,9.2\nBKN,9.15\nORL,9.14\nDET,9.06\nNO,9\nCHA,8.97\nPHI,8.97\nCHA,8.86\nMIA,8.84\nORL,8.83\nSA,8.81\nUTAH,8.76\nMIA,8.67\nOKC,8.54\nLAL,8.47\nBOS,8.47\nIND,8.2\nSA,8.19\nLAC,8.19\nHOU,8.13\nORL,8.07\nCLE,8.02\nLAC,7.58\nSAC,7.55\nDEN,7.54\nUTAH,7.47\nIND,7.42\nBOS,7.15\nSAC,7.1\nUTAH,6.91\nCHI,6.83\nCLE,6.46\nCHI,6.16\nORL,5.87\nCLE,5.86\nNO,5.13\nHOU,4.89\nDEN,4.82\nCLE,4.65\nMIL,4.64\nMIL,4.13\nWSH,4.02\nHOU,3.62\nLAC,3.31\nDET,1.61\nNO,0.56\nGS,0.17\nSAC,0\nSAC,0\nIND,0\nGS,0\nCLE,0\nCLE,0\nTOR,0\nCHI,0\nDEN,0\nWSH,0\nTOR,0\nCHI,0\nTOR,0\nNO,0\nBOS,20.79\nBOS,20.45\nBOS,15.09\nDET,16.96";
	
	public static List<Player> GeneratePlayers() {
		return GeneratePlayers(DEFAULT_PLAYER_FILE);
	}
	
	/**
	 * Read in the file of default players.
	 * @param playerCsv
	 * @return List of players read from the csv
	 */
	public static List<Player> GeneratePlayers(String playerCsv) {
		List<Player> players = new ArrayList<Player>();
		
		//Scanner playerScanner = new Scanner(playerFile);
		StringTokenizer lines = new StringTokenizer(PLAYER_CSV, "\n");
		String playerLine;
		while(lines.hasMoreTokens()) {
			playerLine = lines.nextToken();
			players.add(parsePlayerLine(playerLine));
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
		
		return new Player(per, teamName);
	}
}
