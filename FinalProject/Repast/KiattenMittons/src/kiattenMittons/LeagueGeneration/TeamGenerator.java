package kiattenMittons.LeagueGeneration;

import java.util.ArrayList;
import java.util.List;

import kiattenMittons.Team;

public class TeamGenerator {

	public enum TeamName {
		NO, OKC, GS, HOU, MIA,
		CLE, LAC, SAC, POR, CHI,
		UTAH, BKN, SA, IND, ORL,
		NY, MEM, DET, ATL, TOR,
		BOS, LAL, WSH, MIN, DAL,
		CHA, DEN, PHX, MIL, PHI
	}

	/**
	 * Generates a list of teams for each team in the enum
	 * @return
	 */
	public static List<Team> generateTeams() {
		List<Team> teams = new ArrayList<Team>();
		for (TeamName teamName : TeamName.values()) {
			teams.add(new Team(teamName));
		}
		return teams;
	}
}
