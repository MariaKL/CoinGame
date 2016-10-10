package kaukau.tests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import kaukau.model.Coin;
import kaukau.model.CoinBox;
import kaukau.model.Direction;
import kaukau.model.GameMap;
import kaukau.model.GameMap.TileType;
import kaukau.model.GameWorld;
import kaukau.model.Key;
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
	 * This test construct a game then add one player to the game.
	 * Check if the player get the right user id and start location.
	 * Also check if the player's inventory contain one item which is a CoinBox.
	 */
	@Test
	public void testAddPlayer_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		int size = player.getInventory().size();
		assertEquals("Player's inventory size is " + size + " players but should have 1.", 1, size);
		assertTrue(player.getInventory().get(0) instanceof CoinBox);
		CoinBox coinbox = player.getCoinBox();
		assertTrue(!coinbox.isStorageFull());
	}


	/**
	 * This test construct a game then add two player to the game.
	 * Check if the players is not start on the same tiles.
	 */
	@Test
	public void testAddPlayer_3() {
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
		assertTrue(board.getTileAt(new Point(2, 2)).getPlayer().equals(player2));
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
		assertTrue(player.getLocation().isTileOccupied());
	}

	/**
	 * Test player move to an empty tile and check player's facing
	 * direction is on the right direction.
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
		assertTrue(player.getLocation().isTileOccupied());
	}

	/**
	 * Move player around the board, old position should be not ocuppy
	 * after player move to the new position. The new position should be marked as occupy.
	 */
	@Test
	public void testMovePlayer_3() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile tile = player.getLocation();
		Point oldPos = new Point(tile.X(), tile.Y());
		assertTrue(board.getTileAt(oldPos).isTileOccupied());
		assertTrue(game.movePlayer(uid, Direction.SOUTH));
		assertTrue(board.getTileAt(new Point(oldPos.x, oldPos.y+1)).isTileOccupied());
		assertFalse(board.getTileAt(oldPos).isTileOccupied());
		assertTrue(player.getfacingDirection() == Direction.SOUTH);
		assertTrue(game.movePlayer(uid, Direction.EAST));
		assertTrue(player.getfacingDirection() == Direction.EAST);
	}

	/**
	 * Test a player to pick up a key on the board. Player only allow to pick
	 * up an item in front of them and the pickup item must be pickupable type.
	 */
	@Test
	public void testPickupItem_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(game.movePlayer(uid, Direction.EAST));
		assertTrue(game.movePlayer(uid, Direction.EAST));
		Tile tile = player.getLocation();
		Point pos = new Point(tile.X()+1, tile.Y());
		assertTrue(player.getInventory().size() == 1);
		assertFalse(game.movePlayer(uid, Direction.EAST));
		assertTrue(board.getTileAt(pos).isTileOccupied());
//		assertTrue(board.getTileAt(pos).getTileType() == TileType.EMPTY);
		assertTrue(game.pickupAnItem(uid));
		assertTrue(player.getInventory().size() == 2);
		assertTrue(player.getInventory().get(1) instanceof Key);
	}

	/**
	 * Test a player to pick up a Coin on the board. Player only allow to pick
	 * up an item in front of them and the pickup item must be pickupable type.
	 */
	@Test
	public void testPickupItem_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile coin = board.getTileAt(new Point(3,5));
		Tile newPos = board.getTileAt(new Point(2,5));
		int coinAmount = ((Coin) coin.getItem()).getAmount();
		player.setLocation(newPos);
		player.setFacingDirection(Direction.EAST);
		assertTrue(coin.getTileType() == TileType.TILE || coin.getTileType() == TileType.TILE_CRACKED);
		assertTrue(coin.getItem() instanceof Coin);
		assertTrue(game.pickupAnItem(uid));   // player pick up the coin
		assertTrue(!coin.isTileOccupied());   // the tile should be not occupy after player pickup coin
		int size = player.getInventory().size();
		assertEquals("Player's inventory size is " + size + " players but should have 1 after pick up a coin."
				+ "Coin should be stored in the Coinbox that inside the inventory.", 1, size);
		CoinBox coinBox = player.getCoinBox();
		assertEquals("The total money of the player is " + player.totalMoney() + ", it should be 30", coinAmount, player.totalMoney());
		assertEquals("The total coin in the CoinBox is " + coinBox.getStorage().size() + ", it should be 1.", 1, coinBox.getStorage().size());
	}

	/**
	 * Test a player to open a door. Player only open a door when
	 * facing direction contain a door.
	 */
	@Test
	public void testOpenDoor_1(){
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		player.setLocation(board.getTileAt(new Point(15,8)));
		player.setFacingDirection(Direction.SOUTH);
		assertTrue(board.getTileAt(new Point(15,9)).getTileType() == TileType.DOOR);
		assertTrue(game.openDoor(uid));
		assertFalse(board.getTileAt(new Point(15,8)).isTileOccupied());
		assertTrue(board.getTileAt(new Point(15,10)).isTileOccupied());
		// player's location should be updated by two tiles
		assertTrue(player.getLocation().equals(board.getTileAt(new Point(15,10))));
	}

	/**
	 * Test invalid open door action while the player facing other direction.
	 */
	@Test
	public void testInvalidOpenDoor_1(){
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		player.setLocation(board.getTileAt(new Point(15,8)));
		player.setFacingDirection(Direction.WEST);
		assertTrue(board.getTileAt(new Point(15,9)).getTileType() == TileType.DOOR);
		assertFalse(game.openDoor(uid));
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
		assertTrue(game.movePlayer(uid, Direction.WEST));
		Tile pos = player.getLocation();
		Tile wall = board.getTileAt(new Point(pos.X()-1, pos.Y()));
		assertTrue(wall.getTileType() == TileType.WALL);
		assertFalse(game.movePlayer(uid, Direction.WEST));
	}

	/**
	 * Test invalid move when player try to move into a tile that contain an item.
	 */
	@Test
	public void testInvalidMovePlayer_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile coin = board.getTileAt(new Point(3,5));
		Tile newPos = board.getTileAt(new Point(2,5));
		player.setLocation(newPos);
		player.setFacingDirection(Direction.EAST);
		assertTrue(coin.getTileType() == TileType.TILE || coin.getTileType() == TileType.TILE_CRACKED);
		assertTrue(coin.getItem() instanceof Coin);
		assertFalse(game.movePlayer(uid, Direction.EAST));
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
