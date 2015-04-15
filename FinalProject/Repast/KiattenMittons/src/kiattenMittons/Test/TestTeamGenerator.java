package kiattenMittons.Test;

import java.util.List;

import junit.framework.TestCase;
import kiattenMittons.Player;
import kiattenMittons.LeagueGeneration.PlayerGenerator;

public class TestTeamGenerator extends TestCase {
	
	public void testDraft() throws Exception {
		List<Player> draftClass = PlayerGenerator.generateDraftClass();
		assertNotNull(draftClass.get(0));
	}
}
