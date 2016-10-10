package kaukau.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import kaukau.control.Client;
import kaukau.model.GameWorld;
import kaukau.model.Player;
import kaukau.storage.JAXBJavaToXml;

/**
 * This class is in charge of creating the application window
 * 	for the Kaukau adventure game. The application window interfaces
 * 	with the rendering window to create the game gui.
 *
 * @author Patrick and Matthias
 *
 */
@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame{

	//generate XML
	//private JAXBJavaToXml toXML = new JAXBJavaToXml();
	//private int saveNumber = 1;


	// path to the images folder
	private static final String IMAGE_PATH = "images/";

	// Field to store the rendering window for the game
	public RenderCanvas rc;

	// Field to store application window's copy of the game
	
	// Field to store the inventory frame
	public Inventory inventory;
	
	// Private instance variable for Client
	private Client client;
	
	// Final Variables to avoid magic numbers
	public final int WINDOW_WIDTH = 765;
	public final int WINDOW_HEIGHT = 525;
	public final int INVENTORY_HEIGHT = 250;
	
	private GameWorld game;

	//field to store the player of the game
	private Player player;

/*

	public ApplicationWindow(GameWorld game){
*/
	public ApplicationWindow(GameWorld gameWorld, Player user){
		super("Kaukau");

		this.game = gameWorld;
		this.player = user;

		// creating a menu
		initMenu();
		
		// make inventory
		inventory = new Inventory();
		
		// initialize client private instance variable

/*
		rw = new RenderWindow(game, this);
*/
		// construct render window with GameWorld
		rc = new RenderCanvas(game, user);

		// adding the rendering window to the application
		add(rc);

		// setting title
		setTitle("Kaukau");
		// set size
		//setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setSize(1020, 620);

		// set display location
		//setLocationRelativeTo(null);
		add(createCenterPanel(),BorderLayout.CENTER);		
		add(createBottomPanel(),BorderLayout.SOUTH);
		// set close operation
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// pack to minimum size
		//pack();
		// enforce minimum size
		setMinimumSize(getSize());
		// handles the closing of the game
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt){
                confirmExit();
            }
        });
	}
	

	private JPanel createCenterPanel() {
		//MainDisplay display = new MainDisplay();
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		Border cb = BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(3, 3, 3, 3), BorderFactory
				.createLineBorder(Color.gray));
		centerPanel.setBorder(cb);
		centerPanel.add(rc, BorderLayout.CENTER);
		return centerPanel;
	}
	
	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		bottomPanel.setBorder(BorderFactory.createTitledBorder(blackline, "Inventory"));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));				
		bottomPanel.add(inventory);
		return bottomPanel;
	}

	/**
	 * Sets the application's copy of the game world.
	 * @param game
	 */
	public void setGame(GameWorld game){
		this.game = game;
		this.player = game.getAllPlayers().get(player.getUserId());
		rc.setGame(game);
	}

	/**
	 * @return the applications copy of the game world
	 */
	public GameWorld getGame(){
		return this.game;
	}

	/**
	 * Associates this window with a client.
	 * @param client
	 */
	public void setClient(Client client){
		this.client = client;
		rc.setClient(client);
	}

	/**
	 * Creates the menu bar for the game
	 */
	private void initMenu() {
		//Creating the menu bar
		JMenuBar menuBar = new JMenuBar();

		// Creating icons
		ImageIcon iconExit = new ImageIcon(IMAGE_PATH + "exit.png");
		ImageIcon iconHelp = new ImageIcon(IMAGE_PATH + "help.png");

		// creating menu and help menus
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		JMenu save = new JMenu("Save");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JAXBJavaToXml toXml = new JAXBJavaToXml();
				toXml.generateXML(game);
			}
		});
		JMenu load = new JMenu("Load");
		load.setMnemonic(KeyEvent.VK_L);

		// creating the view help menu item
		JMenuItem hMenuItem = new JMenuItem("View Help", iconHelp);
		hMenuItem.setMnemonic(KeyEvent.VK_H);
		hMenuItem.setToolTipText("Click for Game Help");
		hMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// displays a help message to user
				displayHelp();
			}
		});
		// creating the exit menu item
		JMenuItem eMenuItem = new JMenuItem("Exit", iconExit);
		eMenuItem.setMnemonic(KeyEvent.VK_E);
		eMenuItem.setToolTipText("Exit App");
		eMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// Confirms user wants to exit
				confirmExit();
			}
		});
		// adding menu and help menus
		menu.add(eMenuItem);
//		menu.add(save);
//		menu.add(load);
		help.add(hMenuItem);
		// adding menus to menubar
		menuBar.add(menu);
		menuBar.add(help);
		menuBar.add(save);
		menuBar.add(load);
		// set the menu bar
		setJMenuBar(menuBar);
	}

	/**
	 * 
	 */
	protected void displayHelp() {
		
		JOptionPane.showMessageDialog(this, "Using the keyboard: \nUse W, S, A, D or the arrow keys \n"
				+ "to move up, down, left, and right respectively.\n\n "
				+ "Use the R key to rotate the board.\n\n Use the "
				+ "E key when facing a door to enter it.\n If "
				+ "the door is locked and you don't have the key,\n "
				+ "you will not be able to enter.\n\n Press the "
				+ "P key to pick-up an item you are facing.");
		
	}


	/**
	 * Displays dialog asking if user wants to exit the game
	 */
	private void confirmExit() {
		String msg = "Are You Sure You Want to Exit the Game?" ;
		int result = JOptionPane.showConfirmDialog(this, msg,
		        "Alert", JOptionPane.OK_CANCEL_OPTION);
		if(result==0){
			//client.closeClientSock();  //client 
			//TODO: remove from gameworld hashmap
			System.exit(0);
			dispose();
		}
	}
	
	public class Inventory extends Canvas implements MouseListener {

		
		private final int SIZE_DIVISOR = 6;
		private final int NUM_ITEMS = 8;
		
		public Inventory() {
			setBounds(0, 0, (WINDOW_WIDTH*NUM_ITEMS)/SIZE_DIVISOR, (INVENTORY_HEIGHT)/SIZE_DIVISOR);
			addMouseListener(this);
			// makes the inventory unfocusable so key bindings can work for render canvas
			this.setFocusable(false);
		}
		
		public void paint (Graphics g){
			//get Player and his inventory
			//paint
			
			//client = rw.getClient();
			//client = rc.getClient();
			int uid = player.getUserId();
			//temp instance of player in temp instance of 
			//game.addPlayer();
			//System.out.println("");
			//System.out.println("Got player id through app window: "+ uid); //test see player uid
			HashMap<Integer, Player> players = game.getAllPlayers();
			//here test
			if (!players.containsKey(uid)) {
				System.out.println("Player id not in hashmap");
				if (players.isEmpty())
					System.out.println("hashmap is emtpty");
			}
			//test to get key set of players hashmap
			Set<Integer> temp = players.keySet(); //
			//System.out.println(temp.toString()); //
			//get the uid of player in hashmap
			int tempuid = temp.iterator().next(); //
			//System.out.println("Using this player id: " + tempuid); //
			//get player inventory
			ArrayList<kaukau.model.PickupableItem> inv = players.get(tempuid).getInventory();			
			if (inv.size()<2){
				//add item to player bag for testing purposes
				players.get(tempuid).addToBag(new kaukau.model.Key(1)); //
			}
			for(int i=0;i<inv.size();++i) {
				kaukau.model.PickupableItem item = null;
				if(i < inv.size()) {
					//System.out.println("inventory size: "+inv.size());
					//System.out.println("This is the inventory: "+inv.toString());
					item = inv.get(i);
				}				
				drawLocation(i,0,WINDOW_WIDTH/SIZE_DIVISOR,INVENTORY_HEIGHT/SIZE_DIVISOR,item,g);
			}
			 
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = (e.getX()*SIZE_DIVISOR) / WINDOW_WIDTH;
			//get player and his inventory
			HashMap<Integer, Player> players = game.getAllPlayers();
			Set<Integer> temp = players.keySet();
			//get the uid of player in hashmap
			int tempuid = temp.iterator().next();
			ArrayList<kaukau.model.PickupableItem> inv = players.get(tempuid).getInventory();
			System.out.println("Inventory size: "+inv.size());
			
			
			if(x < inv.size()) {
				createActionMenu(e,inv.get(x));
				System.out.println("You clicked item "+ x);
				
			} 				
			this.repaint();
		}
		
		private void createActionMenu(MouseEvent e, kaukau.model.PickupableItem item) {
			JPopupMenu actionMenu = new JPopupMenu();
			//String[] actions = item.getActions();
							
			JMenuItem mi = new JMenuItem("Description");
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(ApplicationWindow.this,item.getName());
				}			
			});
			actionMenu.add(mi);	
			/*
			for(int i=0;i!=actions.length;++i) {
				mi = new JMenuItem(actions[i]);
				mi.addActionListener(createItemListener(item,actions[i]));
				actionMenu.add(mi);
			}			*/	
			actionMenu.show(e.getComponent(), e.getX(), e.getY());		
		}
		
		/*private ActionListener createItemListener(final Item i, final String action) {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean r = i.performAction(action, game.getPlayer());
					if(!r) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,"Could not \"" + action + "\" item");
					}
					// Force a repain in case anything has changed
					ApplicationWindow.this.repaint();
				}			
			};
		} */

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	} //end of inventory window class
	
	//TODO: fix the image file read and get files for item icons
	private void drawLocation(int x, int y, int width, int height, kaukau.model.PickupableItem item, Graphics g) {
		g.translate(x*width,y*height);
		g.setClip(0,0,width,height);
		if(item != null) { 
			try {
				String name = item.getName();
				//TODO: find coinbox asset and change hardcode here, replace with name
				if (name.equals("Coin Box")){
					name = "cube4";
				}
				if (name.equals("Key")){
					name = "cube2";
				}
				name.toLowerCase();
				//System.out.println(name);
				BufferedImage image = ImageIO.read(new File("images/" + name + ".png"));
				if(image != null){
	    			g.drawImage(image, x, y, this);
	    		}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		g.translate(-(x*width), -(y*height));
	}
}