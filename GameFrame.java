/**
 * 
 */
package ticTacNole;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * @author Doug
 *
 */
public class GameFrame extends JFrame
{
	private final BorderLayout LAYOUT;
	private final JLabel NAMEBAR;
	private final GameBoardPanel board;
	private final int PLAYER1 = 1;
	private final int PLAYER2 = 2;
	private final JMenuBar menuBar;
	private final JMenu menu, subMenu, subMenu1;
	private JMenuItem menuItem;
	private int currentPlayer;
	private int winner;
	private JOptionPane WinnerAnnouncement;
	private boolean[] isComputer = new boolean[3];
	private Thread thread;
	private ExecutorService gameExecutor = Executors.newSingleThreadExecutor();
	private Future<?> gameTask;
	private final ImageIcon FRAMEICON = new ImageIcon(getClass().getResource("images/FSU.jpg"));
	
	
	public GameFrame()
	{
		super("FSU");
		winner = 0;
		currentPlayer = 0;
		for(int i = 0; i < isComputer.length; ++i)
		{
			isComputer[i] = false;
		}
		
		//set the layout
		LAYOUT = new BorderLayout(5,5);
		setLayout(LAYOUT);
		
		//create menu
		//create MenuBar
		menuBar = new JMenuBar();
		
		//build the Menu
		menu = new JMenu("Game Options");
		menu.setMnemonic(KeyEvent.VK_G);
		menuBar.add(menu);
		
		//create the New Game submenu
		subMenu = new JMenu("New Game");
		subMenu.setMnemonic(KeyEvent.VK_N);
		
		subMenu1 = new JMenu("1 Player Game");
		subMenu1.setMnemonic(KeyEvent.VK_1);
		
		menuItem = new JMenuItem("Human Player 1");
		menuItem.setMnemonic(KeyEvent.VK_1);
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				isComputer[PLAYER1] = false;
				isComputer[PLAYER2] = true;
				startNewGame();
			}
		});
		subMenu1.add(menuItem);
		
		menuItem = new JMenuItem("Human Player 2");
		menuItem.setMnemonic(KeyEvent.VK_2);
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				isComputer[PLAYER1] = true;
				isComputer[PLAYER2] = false;
				startNewGame();
			}
		});
		subMenu1.add(menuItem);
		
		subMenu.add(subMenu1);
		
		//finish New Game Submenu
		menuItem = new JMenuItem("2 Player Game");
		menuItem.setMnemonic(KeyEvent.VK_2);
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				isComputer[PLAYER1] = false;
				isComputer[PLAYER2] = false;
				startNewGame();
			}
		});
		subMenu.add(menuItem);
		
		menu.add(subMenu);
		menu.addSeparator();
		
		menuItem = new JMenuItem("Exit Game");
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent evt)
				{
					System.exit(0);
				}
			});
		menu.add(menuItem);
		
		//attach the menu bar
		setJMenuBar(menuBar);
		
		
		//create container subcomponents
		board = new GameBoardPanel();
		
		NAMEBAR = new JLabel("By Douglas Hennenfent");
		NAMEBAR.setHorizontalTextPosition(JLabel.CENTER);
		
		
		//add all components
		add(NAMEBAR, BorderLayout.SOUTH);
		add(board, BorderLayout.CENTER);
		
		
		setIconImage(FRAMEICON.getImage());
	}//end GameFrame()
	
	private void playGame()
	{
		board.initializeGame();
		//start with player 1
		winner = 0;
		currentPlayer = PLAYER1;
		
		//enter game loop
		int counter = 9;
		while(counter > 0)
		{
			board.setActivePlayer(currentPlayer);
			if(!isComputer[currentPlayer])
			{
				board.setGameActive(true);
			}
			
			if(isComputer[currentPlayer])
			{
				board.computerMove(currentPlayer);
				board.setGameActive(false);
				//System.out.println("Finished Computer Move");
			}
			
			else
			{
				//wait for human player to press button
				while(board.isGameActive())
				{
					Thread.yield();
					if(Thread.currentThread().isInterrupted())
					{
						return;
					}
				}
			}
			
			//check for winner
			winner = board.checkWinner();
			
			//if winner break from loop
			if(0 != winner)
			{
				break;
			}
			
			//toggle active player
			switch(currentPlayer)
			{
			case PLAYER1:	currentPlayer = PLAYER2;
							break;
			case PLAYER2:	currentPlayer = PLAYER1;
							break;
			}
			--counter;
			
			if(Thread.currentThread().isInterrupted())
			{
				return;
			}
		}
		
		if(0 != winner)
		{
			WinnerAnnouncement.showMessageDialog(getParent(), "Player " + winner + " wins!");
		}
		else
		{
			WinnerAnnouncement.showMessageDialog(getParent(), "It's a draw!");
		}
	}//end playGame()
	
	public void startNewGame()
	{
		if(gameTask != null)
		{
			gameTask.cancel(true);
		}
		gameTask = gameExecutor.submit(new Runnable()
			{
				public void run()
				{
					playGame();
				}
			});
	}//end startNewGame()
	
}//end Class GameFrame

/**
 *  References:
 *  http://stackoverflow.com/questions/9419252/why-does-this-simple-java-swing-program-freeze
 */