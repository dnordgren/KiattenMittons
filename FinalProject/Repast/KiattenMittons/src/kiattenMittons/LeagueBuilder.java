package kiattenMittons;

import java.util.*;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
import kiattenMittons.LeagueGeneration.TeamGenerator;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class LeagueBuilder implements ContextBuilder<Object> {
	
	public static final int YEAR_LENGTH = 100;

	public Context<Object> build(Context<Object> context) {

		context.setId("KiattenMittons");

		List<Player> initialPlayers = PlayerGenerator.generatePlayers();
		for (Player player: initialPlayers) {
			context.add(player);
		}

		List<Team> teams = TeamGenerator.generateTeams();

		Map<TeamName, Team> teamNameDictionary = new HashMap<TeamName, Team>();
		for (Team team: teams) {
			TeamName teamName = team.getTeamName();
			teamNameDictionary.put(teamName, team);
		}

		//adds each player to its team, and signs the contract
		for (Player player: initialPlayers) {
			TeamName teamName = player.getTeamName();
			Team team = teamNameDictionary.get(teamName);
			int contractLength = PlayerGenerator.generateSeedContractLength();
			double contractSize = player.getPerBasedValue();

			player.signWithTeam(team, contractLength, contractSize);
			team.addPlayer(player);
		}

		for (Team team: teams) {
			context.add(team);
		}
		
		League league = new League(teams);
		context.add(league);
		
		return context;
	}

}
