package kaukau.control;

import kaukau.model.*;
import kaukau.view.ApplicationWindow;

import java.awt.EventQueue;
import java.io.IOException;


/**
 * Main method for the Kaukau adventure game
 * @author Patrick
 *
 */
public class Main {

	/**
	 * Connects a player to the game through the server.
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args) throws IOException{

    	EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				//
				ApplicationWindow aw = new ApplicationWindow();
				aw.setVisible(true);
				Client pClient = new Client(aw);
		    	pClient.start();
		    	aw.rw.addClient(pClient);
			}
		});

	}
}
