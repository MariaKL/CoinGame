package kaukau.tests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import kaukau.model.Direction;
import kaukau.model.GameMap;
import kaukau.model.GameMap.TileType;
import kaukau.model.GameWorld;
import kaukau.model.Player;
import kaukau.model.Tile;

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
		assertTrue(player.getfacingDirection() == Direction.SOUTH);
	}
	
	/**
	 * Test player move to an empty tile.
	 */
	@Test
	public void testMovePlayer_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(game.movePlayer(uid, Direction.SOUTH));
		assertTrue(player.getfacingDirection() == Direction.SOUTH);
		assertTrue(game.movePlayer(uid, Direction.EAST));
		assertTrue(player.getfacingDirection() == Direction.EAST);
	}

	/**
	 * Test invalid move when player try to move into a door, 
	 * player use enter commands to interact with door.
	 */
	@Test
	public void testInvalidMovePlayer_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile pos = player.getLocation();
		Tile wall = board.getTileAt(new Point(pos.X(), pos.Y()-1));
		assertTrue(wall.getTileType() == TileType.DOOR);
		assertFalse(game.movePlayer(uid, Direction.NORTH));
	}

	@Test
	public void testCreateGame_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		kaukau.model.Tile[][] tiles = board.getBoard();
		for(int row = 0; row < board.height(); row++){
			for (int col = 0; col < board.width(); col++){
				assertTrue(tiles[col][row] != null);
			}
		}
	}


}
