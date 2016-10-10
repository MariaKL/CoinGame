package kaukau.control;

import kaukau.model.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


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
	private Thread listeningThread;
	private Thread commandThread;
	// client sockets
	private static HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();
	private static HashMap<Integer, ObjectInputStream> in = new HashMap<Integer, ObjectInputStream>();
	private static HashMap<Integer, ObjectOutputStream> out = new HashMap<Integer, ObjectOutputStream>();
	// commands received from clients
	private Queue<String> commands = new LinkedList<String>();
	// game
	private static GameWorld game;
	// current player to listen to
	private static int player = 1;

	/**
	 * Creates the listening socket and the two threads to connect clients and handle player actions.
	 * @param game
	 */
	public Server(GameWorld game){
		this.game = game;
        try {
			listener = new ServerSocket(portNumber);
			System.out.println("Created server");
			// start connecting and listening to clients
			listeningThread = makeListeningThread();
			commandThread = makeCommandThread();
			listeningThread.start();
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
		    		System.out.println("Waiting for commands");
			    	while(atleastOneConnection()) {
			    		System.out.println("At least one connection");
	//					game.setState(Board.READY);
//						Thread.sleep(3000);
	//					game.setState(Board.PLAYING);

						//TODO: go through each client and read input

						while(!game.isOver()) {
				    		System.out.println("Game continues");
							Socket sock = sockets.get(player);
							if(sock == null) System.out.println("No sock");
							// read input from player
//							ObjectInputStream input = new ObjectInputStream(sock.getInputStream());
							ObjectInputStream input = getInputStream(sock);
							if(input==null)
								System.out.println("No inputstream for the sock");
							// reads in commands
							int uid = input.readInt();
							int dir = input.readInt();
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
									System.out.println(uid + ": Moved left");
									break;
								case KeyEvent.VK_UP:
								case KeyEvent.VK_KP_UP:
									game.movePlayer(uid, Direction.NORTH);
									System.out.println(uid + ": Moved up");
									break;
								case KeyEvent.VK_DOWN:
								case KeyEvent.VK_KP_DOWN:
									game.movePlayer(uid, Direction.SOUTH);
									System.out.println(uid + ": Moved down");
									break;
							}
							System.out.println("Updated game");
							// send back to clients
							updateAll();
							//OR
	//						sendToAll(uid + " " + dir);
//							Thread.sleep(3000);
						}
			    		System.out.println("Game over");
						// If we get here, then we're in game over mode
//						Thread.sleep(3000);
	//					// Reset board state
	//					game.setState(Board.WAITING);
	//					game.fromByteArray(state);
					}
		    		System.out.println("No connections");
		    	} catch(IOException e){
		    		e.printStackTrace();
		    	}
//		    	catch(InterruptedException e){
//		    		e.printStackTrace();
//		    	}
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
		        try{
		        	System.out.println("Listening");
		            while(game.getAllPlayers().size() < 2){ // max two players
						Socket socket = listener.accept();
						// if there are more than two players then don't accept the new player
	            		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	            		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		            	if(sockets.size() > 2){
//		            		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		    				output.writeBoolean(false);
//		    				output.writeUTF("Cannot accept another player");
				        	output.flush();
		            	}
		            	// otherwise accept a new player
		            	else{
							// accept a new client
							int uid = game.addPlayer();
							sockets.put(uid, socket);
							out.put(uid, output);
							in.put(uid, input);
							System.out.println("New socket: " + socket.getPort() + ", UID: " + uid);

//		            		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		    				output.writeBoolean(true);
//				        	output.flush();
							// send the client's uid to the client
							output.writeInt(uid);
//				        	output.flush();
				        	// send new game to all clients
							System.out.println("Server: number of players: " + game.getAllPlayers().size());
							System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
							// starts listening for commands when a player is connected
							if(!commandThread.isAlive())
								commandThread.start();
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
        	  if(sock.isClosed())
        		  continue;
        	  // sends game to byte array to each client
//        	  ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
        	  ObjectOutputStream output = getOutputStream(sock);
        	  if(out==null){
        		  System.out.println("No output stream initialised for this sock " + getUID(sock));
        	  }
        	  output.writeObject(game.toByteArray());
        	  output.flush();
        	  System.out.println("Sent updated game to player " + getUID(sock));
          }
    	  System.out.println("Sent updated game to players");
    	}
    	catch(IOException e){
    		e.printStackTrace();
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
//        	  ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
        	  ObjectOutputStream output = getOutputStream(sock);
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
     * Returns the input stream of a given sock.
     * @param sock
     * @return
     */
    private static ObjectInputStream getInputStream(Socket sock){
    	for(Integer i: sockets.keySet())
    		if(sockets.get(i).equals(sock))
    			return in.get(i);
    	return null;
    }

    /**
     * Returns the output stream of a given sock.
     * @param sock
     * @return
     */
    private static ObjectOutputStream getOutputStream(Socket sock){
    	for(Integer i: sockets.keySet())
    		if(sockets.get(i).equals(sock))
    			return out.get(i);
    	return null;
    }

    /**
     * Returns the uid associated with this socket.
     * @param sock
     * @return
     */
    private static int getUID(Socket sock){
    	for(Integer i: sockets.keySet())
    		if(sockets.get(i).equals(sock))
    			return i;
    	return -1;
    }

    /**
     * Returns the main game.
     * @return
     */
    public static GameWorld getGame(){
    	return game;
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
