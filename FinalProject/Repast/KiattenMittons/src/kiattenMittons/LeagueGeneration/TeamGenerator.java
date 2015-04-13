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
		CHA, DEN, PHX, MIL, PHI,
		FREE
	}

	public static List<Team> generateTeams() {
		List<Team> teams = new ArrayList<Team>();
		for (TeamName teamName : TeamName.values()) {
			if (teamName != TeamName.FREE) {
				teams.add(new Team(teamName));
			}
		}
		return teams;
	}
}
