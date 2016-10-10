package kaukau.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import kaukau.model.*;

public class ItemTests {

	/**
	 * Makes sure that a new game doesn't have any players.
	 */
	@Test
	public void newGameNoPlayers(){
		GameWorld game = new GameWorld();
		int size = game.getAllPlayers().size();
		assertEquals("New game has " + size + " players but should have 0.", 0, size);
	}

	@Test
	public void gameOnePlayer(){
		GameWorld game = HelperMethods.simpleGame();
		assertEquals("Should have 1 player but had " + game.getAllPlayers().size(), 1, game.getAllPlayers().size());
	}

	@Test
	public void coinConstructor(){
		Coin coin1 = new Coin();
		assertEquals("Coin should have amount of -1 but was " + coin1.getAmount(), -1, coin1.getAmount());
		int amt = 5;
		Coin coin2 = new Coin(amt);
		assertEquals("Coin should have amount of -1 but was " + coin2.getAmount(), amt, coin2.getAmount());
	}

	@Test
	public void validAddCoinToCoinBox(){
		Player p1 = HelperMethods.player();
		CoinBox box = new CoinBox(p1);
		box.addCoin(new Coin());
		assertEquals("Coin box should have total amount of -1 but was " + box.totalCoins(), -1, box.totalCoins());
		box.addCoin(new Coin(10));
		assertEquals("Coin box should have total amount of 9 but was " + box.totalCoins(), 9, box.totalCoins());
	}
}
