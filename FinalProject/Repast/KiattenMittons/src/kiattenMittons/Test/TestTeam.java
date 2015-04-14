package kiattenMittons.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import kiattenMittons.Player;
import kiattenMittons.Team;
import kiattenMittons.LeagueGeneration.TeamGenerator;

public class TestTeam extends TestCase {

	public void testEmpty() throws Exception 
	{
	    Team team = new Team(TeamGenerator.TeamName.ATL);
	    
	    team.recordPowerIndex();
	    
	    double parity = team.getParityContribution();
	    assertEquals(0.0, parity);
	}
	
	public void testOne() throws Exception 
	{
	    Team team = new Team(TeamGenerator.TeamName.ATL);
	    
	    team.addPlayer(new Player(1, TeamGenerator.TeamName.ATL, 100, null));
	    team.recordPowerIndex();
	    
	    double parity = team.getParityContribution();
	    assertEquals(0.0, parity);
	}
	
	public void testParity() throws Exception 
	{
	    Team team = new Team(TeamGenerator.TeamName.ATL);
	    
	    team.addPlayer(new Player(1, TeamGenerator.TeamName.ATL, 100, null));
	    team.recordPowerIndex();
	    team.addPlayer(new Player(1, TeamGenerator.TeamName.ATL, 100, null));
	    team.recordPowerIndex();
	    team.addPlayer(new Player(1, TeamGenerator.TeamName.ATL, 100, null));
	    team.recordPowerIndex();
	    
	    double parity = team.getParityContribution();
	    assertTrue(parity > 0);
	}
	
	public static void main(String[] args) 
	{
		TestRunner.runAndWait(new TestSuite(TestTeam.class));
	}
}
