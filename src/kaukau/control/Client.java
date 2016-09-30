package kaukau.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Date;

import kaukau.model.GameWorld;

/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi
 *
 */


/**
 * A client to connects
 */
public class Client extends Thread implements KeyListener{
	private String address = "127.0.0.1";
	private Socket sock;
	private GameWorld game;
//	private Server server;

	private ObjectInputStream input;
	private ObjectOutputStream output;

	private int uid;


	/**
	 * Creates a client for the player.
	 */
	public Client(){
		// makes a new socket and sets input/output streams
		try{
//			while(name.length() < 1){
//				name = "Player" + Math.random();
//			}
	        sock = new Socket(address, Server.portNumber);
	        input = new ObjectInputStream(sock.getInputStream());
	        output = new ObjectOutputStream(sock.getOutputStream());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			boolean accepted = input.readBoolean();
			// if there were too many players then close the socket
			if(!accepted){
				sock.close();
				System.out.println("Not accepted into server");
				return;
			}
			uid = input.readInt();
			game = (GameWorld)input.readObject();

			String line;
			//TODO: make sure thread + client stay open until player closes the client
			// read client command
			while ((line = input.readLine()) != null)  {
				System.out.println(line);
				// write to server
				output.writeUTF("Test message one: " + line + "\n");
				output.flush();
			}
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	/**
//	 * Sends a player command to the server
//	 */
//	public void send(){
//		try {
//			output.write("testString".getBytes("US-ASCII"));
//			output.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Send valid key events to server.
	 */
	public void keyPressed(KeyEvent e) {
		try {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
				output.writeInt(uid);
				output.writeInt(code);
			} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
				output.writeInt(uid);
				output.writeInt(code);
			} else if(code == KeyEvent.VK_UP) {
				output.writeInt(uid);
				output.writeInt(code);
			} else if(code == KeyEvent.VK_DOWN) {
				output.writeInt(uid);
				output.writeInt(code);
			}
			output.flush();
		} catch(IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server.  So, we just ignore it.
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}