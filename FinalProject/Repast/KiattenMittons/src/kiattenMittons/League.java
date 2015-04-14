package kiattenMittons;

import java.util.List;

public class League {
	private List<Team> teams;
	
	public League(List<Team> teams) {
		this.teams = teams;
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
