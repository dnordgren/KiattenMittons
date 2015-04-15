package kiattenMittons.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import kiattenMittons.Player;

public class TestPlayerSorting extends TestCase {

	public void testSort() {
		List<Player> players = new ArrayList<Player>();
		
		players.add(new Player(1, null));
		players.add(new Player(3, null));
		players.add(new Player(2, null));
		
		Collections.sort(players, Player.comparator);
		
		assertEquals(players.get(0).getPER(), 3.0);
		assertEquals(players.get(1).getPER(), 2.0);
		assertEquals(players.get(2).getPER(), 1.0);
	}
}
