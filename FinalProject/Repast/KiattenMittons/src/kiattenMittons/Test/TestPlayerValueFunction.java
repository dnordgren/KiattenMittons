package kiattenMittons.Test;

import junit.framework.TestCase;
import kiattenMittons.Player;

public class TestPlayerValueFunction extends TestCase {

	public void testLow() throws Exception {
		Player player = new Player(10, null, 0);
		double value = player.getPerBasedValue();
		assertTrue(value < 5e6);
	}
	
	public void testHigh() throws Exception {
		Player player = new Player(30, null, 0);
		double value = player.getPerBasedValue();
		assertTrue(value > 20e6);
	}
}
