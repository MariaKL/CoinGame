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
				GameWorld gw = new GameWorld();
				Player user = gw.player(gw.addPlayer());
				ApplicationWindow aw = new ApplicationWindow(gw, user);
				aw.setVisible(true);
				Client pClient = new Client(aw);
		    	pClient.start();
		    	//aw.rw.addClient(pClient);
		    	// TODO: pass info from server where game is saved to client,
		    	// client should get copy of gameworld from server and pass into app window
			}
		});

	}
}
