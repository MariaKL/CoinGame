package kaukau.control;

import kaukau.model.*;

import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi and Vivienne Yapp
 *
 */

public class Server{
	private String address = "127.0.0.1";
	// port as all clients should be able to access this port
	public static int portNumber = 4000;
	// server sockets
	private ServerSocket listener; // accepts clients
	// client sockets
	private static HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();
	// client threads associated with sockets
//	private Set<ClientThread> threads = new HashSet<ClientThread>();
	// commands received from clients
	private Queue<String> commands = new LinkedList<String>();
	// game
	private static GameWorld game;
	// current player to listen to
	private static int player = 0;

	public Server(GameWorld game){
		this.game = game;
        try {
			listener = new ServerSocket(portNumber);
			System.out.println("Created server");
			// start connecting and listening to clients
			Thread listeningThread = makeListeningThread();
			Thread commandThread = makeCommandThread();
			listeningThread.start();
			commandThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a thread to handle player commands.
	 * @return
	 */
    public Thread makeCommandThread(){
		Thread commandThread = new Thread() {
		    public void run() {
		    	try{
			    	while(atleastOneConnection()) {
	//					game.setState(Board.READY);
						Thread.sleep(3000);
	//					game.setState(Board.PLAYING);

						//TODO: go through each client and read input

						while(!game.isOver()) {
							Socket sock = sockets.get(player);
							if(sock == null) System.out.println("No sock");
							// read input from player
							ObjectInputStream input = new ObjectInputStream(sock.getInputStream());
							int uid = input.readInt(); // get player id
							int dir = input.readInt(); // get movement
							System.out.println("Read command from player " + uid + ", direction = " + dir);
							// update game with player movement
							switch(dir) {
								case KeyEvent.VK_RIGHT:
								case KeyEvent.VK_KP_RIGHT:
									game.movePlayer(uid, Direction.EAST);
									System.out.println(uid + ": Moved right");
									break;
								case KeyEvent.VK_LEFT:
								case KeyEvent.VK_KP_LEFT:
									game.movePlayer(uid, Direction.WEST);
									break;
								case KeyEvent.VK_UP:
								case KeyEvent.VK_KP_UP:
									game.movePlayer(uid, Direction.NORTH);
									break;
								case KeyEvent.VK_DOWN:
								case KeyEvent.VK_KP_DOWN:
									game.movePlayer(uid, Direction.SOUTH);
									break;
							}
							System.out.println("Updated game");
							// send back to clients
							updateAll();
							//OR
	//						sendToAll(uid + " " + dir);
							Thread.sleep(3000);
						}
						// If we get here, then we're in game over mode
						Thread.sleep(3000);
	//					// Reset board state
	//					game.setState(Board.WAITING);
	//					game.fromByteArray(state);
					}
		    	} catch(IOException e){
		    		e.printStackTrace();
		    	} catch(InterruptedException e){
		    		e.printStackTrace();
		    	}
		    }
		};
		return commandThread;
    }

    /**
     * Returns a thread to connect clients.
     * @return
     */
	public Thread makeListeningThread(){
		Thread listeningThread = new Thread() {
		    public void run() {
		    	//TODO: allow for multiple players
		        try{
		        	System.out.println("Listening");
		            while(true){
						Socket socket = listener.accept();
						// if there are more than two players then don't accept the new player
		            	if(sockets.size() > 2){
		            		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		    				output.writeBoolean(false);
		    				output.writeUTF("Cannot accept another player");
				        	output.flush();
		            	}
		            	// otherwise accept a new player
		            	else{
							// accept a new client
							int uid = game.addPlayer();
							sockets.put(uid, socket);
							System.out.println("New socket: " + socket.getPort() + ", UID: " + uid);

		            		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		    				output.writeBoolean(true);
							// send the client's uid to the client
							output.writeInt(uid);
				        	output.flush();
				        	System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
				        	// send new game to all players
				        	updateAll();
		            	}
		            }
		        } catch(IOException e){
		        	e.printStackTrace();
		        	System.out.println("Failed to accept client on port " + portNumber + "\n");
		        } catch(Exception e){
		        	e.printStackTrace();
		        } finally {
		        	try{
		        		listener.close();
		        	}
		        	catch(IOException e){
		        		e.printStackTrace();
		        	}
		        }
		    }
		};
		return listeningThread;
	}

	/**
	 * Checks there's at least one socket that's connected.
	 * @return
	 */
	private static boolean atleastOneConnection() {
		for (Socket s : sockets.values()) {
			if (s.isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
     * Sends updated game to all clients.
     * @param message
     */
    public static void updateAll(){
    	try{
          for(Socket sock: sockets.values()){
        	  // sends game to byte array to each client
        	  ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        	  out.writeObject(game.toByteArray());
        	  out.flush();
          }
    	  System.out.println("Sent updated game to players");
    	}
    	catch(IOException e){
    		System.out.println("Failed to create buffered writer.\n");
    	}
    }

    /**
     * Sends String command to all clients.
     * @param message
     */
    public static void sendToAll(String message){
    	try{
          for(Socket sock: sockets.values()){
        	  // sends game to byte array to each client
        	  ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
        	  output.writeUTF(message);
        	  output.flush();
          }
    	}
    	catch(IOException e){
    		System.out.println("Failed to create buffered writer.\n");
    	}
    }

    /**
     * Closes all sockets.
     */
    public void closeAll(){
    	for(Socket sock: sockets.values()){
    		try {
				sock.close();
			} catch (IOException e) {
				System.out.println("Unable to close socket");
			}
    	}
    }

    /**
     * Starts a game and associates it with a server.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
		// create a new game
    	GameWorld newGame = new GameWorld();
    	// make and run a server
    	new Server(newGame);
	}
}
