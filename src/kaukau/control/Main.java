package kaukau.control;

import java.awt.EventQueue;

import kaukau.view.ApplicationWindow;

/**
 * Main method for the Kaukau adventure game
 * @author Patrick
 *
 */
public class Main {
	
	public static void main (String[] args){
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				// 
				ApplicationWindow aw = new ApplicationWindow();
				aw.setVisible(true);
				
			}
			
		});
	}
	
}
