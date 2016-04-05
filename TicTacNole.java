/**
 * 
 */
package ticTacNole;

import javax.swing.JFrame;

/**
 * @author Doug
 *
 */
public class TicTacNole {

	public TicTacNole() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameFrame game = new GameFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setSize(750, 500);
		game.setVisible(true);
		game.startNewGame();
	}

}
