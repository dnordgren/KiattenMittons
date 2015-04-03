package kiattenMittons;

public class Player {
	private double per;
	
	/**
	 * Constructor that auto-generates player efficiency
	 */
	public Player() {
		per = Math.random();
	}
	
	/**
	 * Constructor that lets you assign PER,
	 * for setting up the initial league
	 * @param per
	 */
	public Player(double per) {
		this.per = per;
	}
	
	/**
	 * Getter for PER
	 * @return PER
	 */
	public double getPER() {
		return per;
	}
}
