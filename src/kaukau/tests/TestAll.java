package kaukau.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import kaukau.model.GameWorld;

public class TestAll {

	/**
	 * Makes sure that a new game doesn't have any players.
	 */
	@Test
	public void newGameNoPlayers(){
		GameWorld game = new GameWorld();
		int size = game.getAllPlayers().size();
		assertEquals("New game has " + size + " players but should have 0.", 0, size);
	}

}
