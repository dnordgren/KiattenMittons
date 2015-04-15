package kiattenMittons;

import java.util.List;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class League {
	/*
	 * These values are based off of actual min and max values
	 * for the NBA in 2015.  There are exceptions to these values,
	 * but for simplification, we are ignoring those special cases.  
	 */
	public static final double CONTRACT_MIN = contractMin();
	public static final double CONTRACT_MAX = contractMax();
	public static final double SALARY_CAP = salaryCap();
	private List<Team> teams;
	
	public League(List<Team> teams) {
		this.teams = teams;
	}

	private static double contractMin() {
		return (Double)RunEnvironment.getInstance().getParameters().getValue("minIndividualContractSize");
	}

	private static double contractMax() {
		return (Double)RunEnvironment.getInstance().getParameters().getValue("maxIndividualContractSize");
	}

	private static double salaryCap() {
		return (Double)RunEnvironment.getInstance().getParameters().getValue("salaryCap");
	}
	
	/**
	 * Computes the total parity of the league, which is simply the
	 * sum of the parity contributions for each team
	 * @return league parity metric
	 */
	public double getParity() {
		double sum = 0;
		for(Team tm : teams) {
			sum += tm.getParityContribution();
		}
		return sum;
	}
	
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 4.0)
	public void draft() {
		List<Player> draftClass = PlayerGenerator.generateDraftClass();
		
		Context<Object> context = ContextUtils.getContext(this);
		for(Player player : draftClass) {
			context.add(player);
		}
	}
}
