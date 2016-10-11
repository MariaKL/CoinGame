package kaukau.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Unmarshaller;

import kaukau.model.Coin;
import kaukau.model.CoinBox;
import kaukau.model.Door;
import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Key;
import kaukau.model.PickupableItem;
import kaukau.model.Player;

public class JAXBXmlToJava {

	public Player unmarshalPlayer(int playerID) {
		Player player = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(Player.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			File XMLfile = new File("Player"+playerID+".xml");
			// this will create Java object - country from the XML file
			player = (Player) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}

		return player;

	}

	public GameMap unmarshalMap() {
		GameMap map = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(GameMap.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			File XMLfile = new File("Map.xml");
			// this will create Java object - country from the XML file
			map = (GameMap) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}
		return map;

	}

	public static void testUnmarshalledPlayer(Player player) {
		if (player != null) {
			System.out.println("Player name: " + player.getName());
			System.out.println("Player id: " + player.getUserId());
			System.out.println("Player direction: " + player.getfacingDirection());
			System.out.println("Player location: " + player.getLocation().toString() + " with tile type: "
					+ player.getLocation().getTileType());
			System.out.println("Player inventory limit: " + player.getStorageSize());
			for (PickupableItem p : player.getInventory()) {
				if (p instanceof Key) {
					Key k = (Key) p;
					System.out.println("Player inventory key code: " + k.getCode());
				} else if (p instanceof CoinBox) {
					CoinBox cb = (CoinBox) p;
					for (PickupableItem pi : cb.getStorage()) {
						Coin c = (Coin) pi;
						System.out.println("Coin : " + c.getAmount());
					}
					System.out.println("Player coinbox ammount: " + cb.totalCoins());
					System.out.println("Player coinbox is full?: " + cb.isStorageFull());
				}
			}
		}
	}

	public static void testUnmarshalledMap(GameMap map) {
		if (map != null) {
			for (Door d : map.getAllDoors()) {
				System.out.println("Door code: " + d.getDoorCode());
				System.out.println("Door is locked?: " + d.isLocked());
				/*if (d.getLocation() == null) {
					System.out.println("Door location is null!");
				} else {
					System.out.println("Door location: " + d.getLocation().toString());
				}*/

			}
		}
	}

	public static void main(String[] args) {
		JAXBXmlToJava unmarshaling = new JAXBXmlToJava();
		Player player = unmarshaling.unmarshalPlayer(1);
		// testUnmarshalledPlayer(player);
		GameMap map = unmarshaling.unmarshalMap();
		testUnmarshalledMap(map);
	}

}
