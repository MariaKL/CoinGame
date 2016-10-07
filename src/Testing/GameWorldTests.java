package Testing;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import kaukau.model.Direction;
import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Player;

public class GameWorldTests {

	/**
	 * This test construct a game then add one player to the game.
	 * Check if the player get the right user id and start location.
	 */
	@Test
	public void testAddPlayer_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(uid == 1);
		assertTrue(player.getLocation().equals(board.getTileAt(new Point(2, 1))));
	}

	/**
	 * This test construct a game then add two player to the game.
	 * Check if the players is not start on the same tiles.
	 */
	@Test
	public void testAddPlayer_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid1 = game.addPlayer();
		Player player1 = game.player(uid1);
		assertTrue(uid1 == 1);
		assertTrue(player1.getLocation().equals(board.getTileAt(new Point(2, 1))));

		int uid2 = game.addPlayer();
		Player player2 = game.player(uid2);
		assertTrue(uid2 == 2);
		assertTrue(player2.getLocation() != player1.getLocation());
		assertTrue(player2.getLocation().equals(board.getTileAt(new Point(2, 2))));
		assertTrue(board.getTileAt(new Point(2, 1)).isTileOccupied());
		assertTrue(board.getTileAt(new Point(2, 2)).isTileOccupied());
	}

	/**
	 * Test player move to an empty tile.
	 */
	@Test
	public void testMovePlayer_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(game.movePlayer(uid, Direction.SOUTH));
		assertTrue(player.facingDirection() == Direction.SOUTH);
	}

	@Test
	public void testCreateGame_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		assertTrue(board.width() == 14);
		assertTrue(board.height() == 14);
	}






}
