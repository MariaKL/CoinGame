package kaukau.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * This class is in charge of creating the application window
 * 	for the Kaukau adventure game. The application window interfaces 
 * 	with the rendering window to create the game gui.
 *  
 * @author Patrick
 *
 */
public class ApplicationWindow extends JFrame implements KeyListener {
	
	// path to the images folder
	private static final String IMAGE_PATH = "images/";
	
	// Field to store the rendering window for the game
	RenderWindow rw = new RenderWindow();

	public ApplicationWindow(){
		super("Kaukau");
		
		// creating a menu
		initMenu();
		
		// adding this as a key listener on the render window
		rw.setFocusable(true);
		rw.requestFocusInWindow();
		rw.addKeyListener(this);
		// adding the rendering window to the application
		add(rw);
			
		// setting title
		setTitle("Kaukau");
		// set size
		setSize(710, 550);
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
		help.add(hMenuItem);
		// adding menus to menubar
		menuBar.add(menu);
		menuBar.add(help);
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
	
	/* Keylistner methods
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// getting player tile position
		Point p = rw.player.getTilePos();
		switch(keyCode){
			// key up
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				// move player up
				//rw.player.y--;
				Point updatedPos = new Point(p.x - 1, p.y - 1);
				rw.player.setPosFromTilePos(updatedPos);
				rw.repaint();
				break;
			// key down
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				// move player down
				//rw.player.y++;
				updatedPos = new Point(p.x + 1, p.y + 1);
				rw.player.setPosFromTilePos(updatedPos);
				rw.repaint();
				break;
			// key left
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				// move player left
				//rw.player.x--;
				updatedPos = new Point(p.x - 1, p.y + 1);
				rw.player.setPosFromTilePos(updatedPos);
				rw.repaint();
				break;
			// key right
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				// move player right
				//rw.player.x++;
				updatedPos = new Point(p.x + 1, p.y - 1);
				rw.player.setPosFromTilePos(updatedPos);
				rw.repaint();
				break;
			default:
				break;
		}	
	}
	/* unimplemented methods.
	 */
	@Override
	public void keyTyped(KeyEvent e) { }
	@Override
	public void keyReleased(KeyEvent e) { }
}
