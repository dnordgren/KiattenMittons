package kiattenMittons;

import java.util.List;

import kiattenMittons.LeagueGeneration.PlayerGenerator;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class League {
	public static final double CONTRACT_MAX = contractMax();
	public static final double CONTRACT_MIN = contractMin();
	public static final double SALARY_CAP = salaryCap();
	private List<Team> teams;

	public League(List<Team> teams) {
		this.teams = teams;
	}

	/**
	 * Static method to get the user defined parameter for maximum contract amount.
	 * @return maximum contract amount
	 */
	private static double contractMax() {
		return (Double)RunEnvironment.getInstance().getParameters().getValue("maxIndividualContractSize");
	}

	/**
	 * Static method to get the user defined parameter for minimum contract amount.
	 * @return minimum contract amount
	 */
	private static double contractMin() {
		return (Double)RunEnvironment.getInstance().getParameters().getValue("minIndividualContractSize");
	}

	/**
	 * Static method to get the user defined parameter for team salary cap.
	 * @return team salary cap
	 */
	private static double salaryCap() {
		return (Double)RunEnvironment.getInstance().getParameters().getValue("salaryCap");
	}

	/**
	 * Simulates a draft by adding new Player objects to the context.
	 */
	@ScheduledMethod(start = LeagueBuilder.YEAR_LENGTH, interval = LeagueBuilder.YEAR_LENGTH, priority = 4.0)
	public void draft() {
		List<Player> draftClass = PlayerGenerator.generateDraftClass();
		
		Context<Object> context = ContextUtils.getContext(this);
		for(Player player : draftClass) {
			context.add(player);
		}
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
}
