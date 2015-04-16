package kiattenMittons.Helpers;

import java.util.Comparator;

import kiattenMittons.Player;

public class ProspectivePlayer {
	private double valueAdded;
	private Player player;
	
	public ProspectivePlayer(double valueAdded, Player player) {
		this.valueAdded = valueAdded;
		this.player = player;
	}
	
	public double getValueAdded() {
		return valueAdded;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Comparator for sorting prospective players. The first player after
	 * sorting should be the one whose ratio of value added to expected
	 * price is highest.
	 */
	public static Comparator<ProspectivePlayer> comparator = new Comparator<ProspectivePlayer>() {
		public int compare(ProspectivePlayer p1, ProspectivePlayer p2) {
			double p1ValueAddedRatio = p1.getValueAdded() / p1.getPlayer().getPerBasedValue();
			double p2ValueAddedRatio = p2.getValueAdded() / p2.getPlayer().getPerBasedValue();
			if (p1ValueAddedRatio < p2ValueAddedRatio) {
				return 1;
			} else if (p1ValueAddedRatio > p2ValueAddedRatio) {
				return -1;
			}
			return 0;
		}
	};
}
