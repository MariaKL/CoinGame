package kaukau.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import kaukau.control.Client;
import kaukau.model.GameWorld;
import kaukau.storage.JAXBJavaToXml;

/**
 * This class is in charge of creating the application window
 * 	for the Kaukau adventure game. The application window interfaces
 * 	with the rendering window to create the game gui.
 *
 * @author Patrick
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
	public RenderWindow rw;

	// Field to store application window's copy of the game
	private GameWorld game;

	public ApplicationWindow(GameWorld game){
		super("Kaukau");

		this.game = game;

		// creating a menu
		initMenu();

		// construct render window with GameWorld
		rw = new RenderWindow(game, this);

		// adding the rendering window to the application
		add(rw);

		// setting title
		setTitle("Kaukau");
		// set size
		setSize(865, 625);
		// set display location
		setLocationRelativeTo(null);
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
	
	/**
	 * @return the applications copy of the game world
	 */
	public GameWorld getGame(){
		return this.game;
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
				//displayHelp();
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
	 * Displays dialog asking if user wants to exit the game
	 */
	private void confirmExit() {
		String msg = "Are You Sure You Want to Exit the Game?" ;
		int result = JOptionPane.showConfirmDialog(this, msg,
		        "Alert", JOptionPane.OK_CANCEL_OPTION);
		if(result==0){
			System.exit(0);
			dispose();
		}
	}
}