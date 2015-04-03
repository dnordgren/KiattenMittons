package kiattenMittons;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class LeagueBuilder implements ContextBuilder {

	public Context build(Context context) {
		
		context.setId("NBA");
		
		int numPlayers = 300;
		for (int i = 0; i < numPlayers; i++) {
			context.add(new Player());
		}
		
		return context;
	}

}
