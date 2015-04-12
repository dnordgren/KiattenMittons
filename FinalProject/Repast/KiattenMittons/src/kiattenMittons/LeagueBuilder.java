package kiattenMittons;

import java.util.List;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class LeagueBuilder implements ContextBuilder<Object> {

	public Context<Object> build(Context<Object> context) {
		
		context.setId("NBA");
		
		List<Player> initialPlayers = PlayerGenerator.generatePlayers();
		for (int i = 0; i < initialPlayers.size(); i++) {
			context.add(initialPlayers.get(i));
		}
		
		return context;
	}

}
