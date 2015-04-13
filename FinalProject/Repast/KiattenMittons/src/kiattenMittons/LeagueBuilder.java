package kiattenMittons;

import java.util.*;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
import kiattenMittons.LeagueGeneration.TeamGenerator;
import kiattenMittons.LeagueGeneration.TeamGenerator.TeamName;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class LeagueBuilder implements ContextBuilder<Object> {

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

		for (Player player: initialPlayers) {
			TeamName teamName = player.getTeamName();
			Team team = teamNameDictionary.get(teamName);
			team.addPlayer(player);
		}

		for (Team team: teams) {
			context.add(team);
		}
		return context;
	}

}
