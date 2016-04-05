/**
 * 
 */
package ticTacNole;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Doug
 *
 */
public class GameBoardPanel extends JPanel{
	
	private final int BLANK = 0;
	private final int PLAYER1 = 1;
	private final int PLAYER2 = 2;
	private final int BOARD_SIZE = 9;
	private final GridLayout LAYOUT;
	private final Icon UNKNOWN = new ImageIcon(getClass().getResource("images/Unknown.jpg"));
	private final Icon PLAYER1ICON = new ImageIcon(getClass().getResource("images/FSU.jpg"));
	private final Icon PLAYER2ICON	 = new ImageIcon(getClass().getResource("images/JeanShorts.jpg"));
	private final Icon[] ICONS = {UNKNOWN, PLAYER1ICON, PLAYER2ICON};
	
	private int[] cells = new int[BOARD_SIZE];	//records which player has moved to which cell in the grid
	private JButton[] cellsButtons = new JButton[BOARD_SIZE]; //graphical overlay for cells
	private boolean gameActive;
	private int activePlayer;
	private Random rnd;
	
	public GameBoardPanel()
	{
		//create board
		LAYOUT = new GridLayout(3, 3, 1, 1);
		setLayout(LAYOUT);
		gameActive = false;
		rnd = new Random();
		
		
		//initialize player data
		activePlayer = PLAYER1;
		for(int i = 0; i < cellsButtons.length; ++i)
		{
			cellsButtons[i] = new JButton(ICONS[0]);
			cellsButtons[i].addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Object Source = e.getSource();
						if(Source instanceof JButton)
						{
							JButton TmpBtn = (JButton)Source;
							if(TmpBtn.getIcon() == UNKNOWN && gameActive)
							{
								TmpBtn.setIcon(ICONS[activePlayer]);
								updateCellsValues();
								gameActive = false;
							}
						}
					}
				}/* end private Class ActionListener*/);
			add(cellsButtons[i]);
		}
		
	}//end GameBoardPanel()
	
	/**
	 * @return the winning player
	 */
	protected int checkWinner()
	{
		//check columns
		for(int i = 0; i < 3; ++i)
		{
			if(cells[i] == cells [i + 3] && cells[i] == cells [i + 6] && 0 !=cells[i])
			{
				return cells[i];
			}
		}
		
		//check rows
		for(int i = 0; i < 3; ++i)
		{
			if(cells[(3 * i)] == cells[(3 * i) + 1] && cells[(3 * i)] == cells[(3 * i) + 2] && 0 !=cells[i])
			{
				return cells[(3 * i)];
			}
		}
		
		//check diagonals
		if(cells[0] == cells[4] && cells[0] == cells[8] && 0 !=cells[0])
		{
			return cells[0];
		}
		
		if(cells[6] == cells[4] && cells[6] == cells[2] && 0 != cells[6])
		{
			return cells[6];
		}
		
		//no winner found
		return 0;
	}
	
	protected void computerMove(int player)
	{
		int otherPlayer;
		if(player == PLAYER1)
		{
			otherPlayer = PLAYER2;
		}
		
		else
		{
			otherPlayer = PLAYER1;
		}
		
		//System.out.println("Computer Player " + player);		
		//take winning move:
		//check rows for winning move
		for(int i = 0; i < 3; ++i)
		{
			if(cells[3 * i] == player && cells[(3 * i) + 1] == player && cells[(3 * i) + 2] == BLANK)
				{
					cells[(3 * i) + 2] = player;
					updateCellsLabels();
					return;
				}
		}
		
		for(int i = 0; i < 3; ++i)
		{
			if(cells[(3 * i) + 1] == player && cells[(3 * i) + 2] == player && cells[(3 * i)] == BLANK)
				{
					cells[(3 * i)] = player;
					updateCellsLabels();
					return;
				}
		}
		
		//check columns for winning move
		//System.out.println("Checking Columns");
		for(int i = 0; i < 3; ++i)
		{
			if(cells[i] == player && cells[i + 3] == player && cells[(i + 6)] == BLANK)
			{
				cells[i + 6] = player;
				updateCellsLabels();
				return;
			}
		}
		
		for(int i = 0; i < 3; ++i)
		{
			if(cells[i + 3] == player && cells[i + 6] == player && cells[(i)] == BLANK)
			{
				cells[i] = player;
				updateCellsLabels();
				return;
			}
		}
		
		//check diagonals for winning move
		//System.out.println("Cheching Diagonals");
		if(cells[0] == player && cells[4] == player && cells[(8)] == BLANK)
		{
			cells[8] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[0] == player && cells[8] == player && cells[(4)] == BLANK)
		{
			cells[4] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[4] == player && cells[8] == player && cells[(0)] == BLANK)
		{
			cells[0] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[6] == player && cells[4] == player && cells[(2)] == BLANK)
		{
			cells[2] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[6] == player && cells[2] == player && cells[(4)] == BLANK)
		{
			cells[4] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[4] == player && cells[3] == player && cells[(6)] == BLANK)
		{
			cells[6] = player;
			updateCellsLabels();
			return;
		}
		//block opponents's winning move:
		//System.out.println("Checking Blocking Moves");
		
		//check rows for blocking move
		//System.out.println("Checking Blocking Rows");
		for(int i = 0; i < 3; ++i)
		{
			if(cells[3 * i] == otherPlayer && cells[(3 * i) + 1] == otherPlayer && cells[(3 * i) + 2] == BLANK)
				{
					cells[(3 * i) + 2] = player;
					updateCellsLabels();
					return;
				}
		}
		
		for(int i = 0; i < 3; ++i)
		{
			if(cells[(3 * i) + 1] == otherPlayer && cells[(3 * i) + 2] == otherPlayer && cells[(3 * i)] == BLANK)
				{
					cells[(3 * i)] = player;
					updateCellsLabels();
					return;
				}
		}
		
		//check columns for blocking move
		//System.out.println("Checking blocking columns");
		for(int i = 0; i < 3; ++i)
		{
			if(cells[i] == otherPlayer && cells[i + 3] == otherPlayer && cells[i + 6] == BLANK)
			{
				cells[i + 6] = player;
				updateCellsLabels();
				return;
			}
		}
		
		for(int i = 0; i < 3; ++i)
		{
			if(cells[i + 3] == otherPlayer && cells[i + 6] == otherPlayer && cells[i] == BLANK)
			{
				cells[i] = player;
				updateCellsLabels();
				return;
			}
		}
		
		//check diagonals for blocking move
		//System.out.println("Checking blocking diagonals");
		if(cells[0] == otherPlayer && cells[4] == otherPlayer && cells[8] == BLANK)
		{
			cells[8] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[0] == otherPlayer && cells[8] == otherPlayer && cells[4] == BLANK)
		{
			cells[4] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[4] == otherPlayer && cells[8] == otherPlayer && cells[0] == BLANK)
		{
			cells[0] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[6] == otherPlayer && cells[4] == otherPlayer && cells[2] == BLANK)
		{
			cells[2] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[6] == otherPlayer && cells[2] == otherPlayer && cells[4] == BLANK)
		{
			cells[4] = player;
			updateCellsLabels();
			return;
		}
		
		if(cells[4] == otherPlayer && cells[3] == otherPlayer && cells[6] == BLANK)
		{
			cells[6] = player;
			updateCellsLabels();
			return;
		}
		
		//move to center
		//System.out.println("Checking Center");
		if(cells[4] == BLANK)
		{
			cells[4] = player;
			updateCellsLabels();
			return;
		}
		
		//System.out.println("Checking Corners");
		//move to random corner
		if(cells[0] == BLANK || cells[2] == BLANK || cells[6] == BLANK || cells[8] == BLANK)
		{
			boolean moved = false;
			int move = -1;
			do
			{
				move = rnd.nextInt(9);
				if(cells[move] == BLANK && move % 2 == 0 && move != 4)
				{
					cells[move] = player;
					moved = true;
				}	
			}while(!moved);
			updateCellsLabels();
			//System.out.println("Moved To Corner");
			return;
		}
		
		//System.out.println("Checking Edges");
		//move to random corner
		if(cells[1] == BLANK || cells[3] == BLANK || cells[5] == BLANK || cells[7] == BLANK)
		{
			boolean moved = false;
			int move = -1;
			do
			{
				move = rnd.nextInt(9);
				if(cells[move] == BLANK && move % 2 == 1)
				{
					cells[move] = player;
					moved = true;
				}	
			}while(!moved);
			updateCellsLabels();
			//System.out.println("Moved To Edge");
			return;
		}
		
		/*
		//move to random corner
		int[] open = {-1, -1, -1, -1};
		boolean hasOpen = false;
		System.out.println("Attempting to move to corner");
		if(cells[0] == BLANK)
		{
			System.out.println("cells[0]");
			open[0] = 0;
			hasOpen = true;
		}
		
		if(cells[2] == BLANK)
		{
			System.out.println("cells[2]");
			open[1] = 2;
			hasOpen = true;
		}
		
		if(cells[2] == BLANK)
		{
			System.out.println("cells[6]");
			open[6] = 6;
			hasOpen = true;
		}
		
		if(cells[8] == BLANK)
		{
			System.out.println("cells[8]");
			open[3] = 8;
			hasOpen = true;
		}
		System.out.println("Open Corner");
		//if open corner choose one at random
		if(hasOpen)
		{
			System.out.println("Choosing Open Corner");
			int choice;
			do
			{
				choice = rnd.nextInt(4);	
			}while(open[choice] == -1);
			
			cells[open[choice]] = player;
			System.out.println("Chose corner: " + open[choice]);
			updateCellsLabels();
			return;
		}
		
		System.out.println("Did not move to corner");
		
		//move to random edge
		for(int i = 0; i < open.length; ++i)
		{
			open[i] = -1;
		}
		
		hasOpen = false;
		if(cells[1] == BLANK)
		{
			open[0] = 1;
			hasOpen = true;
		}
		if(cells[3] == BLANK)
		{
			open[1] = 3;
			hasOpen = true;
		}
		if(cells[5] == BLANK)
		{
			open[2] = 5;
			hasOpen = true;
		}
		if(cells[7] == BLANK)
		{
			open[7] = 7;
			hasOpen = true;
		}
		
		//if open corner choose one at random
		if(hasOpen)
		{
			int choice;
			do
			{
				choice = rnd.nextInt(4);	
			}while(open[choice] == -1);
			
			cells[open[choice]] = player;
			updateCellsLabels();
			return;
		}
		*/
		System.out.println("Found No Computer Move!");
	}//end computerMove();
	
	/**
	 * @return the activePlayer
	 */
	protected int getActivePlayer() {
		return activePlayer;
	}

	protected void initializeGame()
	{
		for(int i = 0; i < cells.length; ++i)
		{
			cells[i] = 0;
		}
		updateCellsLabels();
	}

	/**
	 * @return the gameActive status
	 */
	protected boolean isGameActive() {
		return gameActive;
	}

	/**
	 * prints underlying cells array. Intended for debugging.
	 */
	protected void printCells()
	{
		System.out.println("Game state:");
		for(int i = 0; i < cells.length; ++i)
			System.out.println(i + ": " + cells[i]);
	}

	/**
	 * @param activePlayer the activePlayer to set
	 */
	protected void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	/**
	 * @param gameActive the gameActive status to set
	 */
	protected void setGameActive(boolean gameActive) {
		this.gameActive = gameActive;
	}
	
	private void updateCellsLabels()
	{
		for(int i = 0; i < cells.length; ++i)
		cellsButtons[i].setIcon(ICONS[cells[i]]);
	}
	
	private void updateCellsValues()
	{
		for(int i = 0; i < cells.length; ++i)
		{
			if(cellsButtons[i].getIcon() == PLAYER1ICON)
			{
				cells[i] = PLAYER1;
			}
			
			else if(cellsButtons[i].getIcon() == PLAYER2ICON)
			{
				cells[i] = PLAYER2;
			}
			
			else
			{
				cells[i] = 0;
			}
		}
		//printCells();
	}
	
}//end Class GameFrame
