package kaukau.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Unmarshaller;

import kaukau.model.Coin;
import kaukau.model.CoinBox;
import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Key;
import kaukau.model.PickupableItem;
import kaukau.model.Player;

public class JAXBXmlToJava {

	public Player unmarshalPlayer() {
		Player player = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(Player.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			File XMLfile = new File("Player.xml");
			// this will create Java object - country from the XML file
			player = (Player) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}

		return player;

	}

	public CoinBox unmarshalCoinBox() {
		CoinBox cb = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(CoinBox.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			File XMLfile = new File("CoinBox.xml");
			// this will create Java object - country from the XML file
			cb = (CoinBox) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}

		return cb;

	}

	public Coin unmarshalCoin() {
		Coin c = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(Coin.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			File XMLfile = new File("Coin.xml");
			// this will create Java object - country from the XML file
			c = (Coin) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}

		return c;

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

	public static void main(String[] args) {
		JAXBXmlToJava unmarshaling = new JAXBXmlToJava();
		Player player = unmarshaling.unmarshalPlayer();
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
						Coin c = (Coin)pi;
						System.out.println("Coin : " + c.getAmount());
					}
					System.out.println("Player coinbox ammount: " + cb.totalCoins());
					System.out.println("Player coinbox is full?: " + cb.isStorageFull());
				}
			}
		}
		// GameMap map = unmarshaling.unmarshalMap();

		Coin coin = unmarshaling.unmarshalCoin();
		System.out.println("coin ammount: " + coin.getAmount());
	}

}
