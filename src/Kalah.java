import java.util.*;

import javax.swing.event.*;

public class Kalah {

	private int[][] pits;
	private int[] kalahs;
	private int activePlayer;
	private ArrayList<ChangeListener> changeListeners;
	private boolean gameOver;

	public static final int NUMBER_OF_PLAYERS = 2;
	public static final int STONES_NUMBER = 6;

	public Kalah() {
		initiateVariables();
	}

	public int[][] getPits() {
		return pits;
	}

	public int[] getKalahs() {
		return kalahs;
	}

	private void initiateVariables() {
		pits = new int[NUMBER_OF_PLAYERS][STONES_NUMBER];
		kalahs = new int[NUMBER_OF_PLAYERS];
		changeListeners = new ArrayList<ChangeListener>();
		gameOver = false;
		for (int p = 0; p < NUMBER_OF_PLAYERS; p++) {
			kalahs[p] = 0;
			for (int col = 0; col < STONES_NUMBER; col++)
				pits[p][col] = STONES_NUMBER;
		}
		activePlayer = 0;
	}

	public void move(int playerSide, int pit) {
		if (playerSide != activePlayer)
			throw new IllegalArgumentException("It's not your turn, big boy ;).");

		if (pits[playerSide][pit] == 0)
			throw new IllegalArgumentException("Do you see any rock in there? u.u.");

		int rockQuantityOnHand = pits[playerSide][pit];
		pits[playerSide][pit] = 0;
		while (rockQuantityOnHand > 0) {
			pit = getNextPit(pit);

			if (pit == 0) {
				if (playerSide == activePlayer) {
					++kalahs[playerSide];
					--rockQuantityOnHand;
					if (rockQuantityOnHand == 0) {
						checkIfAllPitsAreEmpty();
						boardChanged();
						return;
					}
				}

				playerSide = getNextPlayer(playerSide);
			}
			++pits[playerSide][pit];
			--rockQuantityOnHand;
		}
		endTurn(playerSide, pit);
	}

	private void printBoardToConsole() {
		// Commandline output of stones
		System.out.print(" ");
		for (int p = pits[1].length - 1; p >= 0; p--)
			System.out.print(" " + pits[1][p]);
		if (kalahs[1] < 10)
			System.out.print("\n" + kalahs[1] + "             " + kalahs[0] + "\n ");
		else
			System.out.print("\n" + kalahs[1] + "           " + kalahs[0] + "\n ");
		for (int p = 0; p < pits[0].length; p++)
			System.out.print(" " + pits[0][p]);
		System.out.println("\n");
	}

	private void endTurn(int playerSide, int pit) {
		if (playerSide == activePlayer && pits[playerSide][pit] == 1) {
			/*
			 * Gets all of the stones of the oponent and put in current player's
			 * Kalah as it fell in an empty pit
			 */
			kalahs[playerSide] += 1 + pits[getNextPlayer(playerSide)][STONES_NUMBER - pit - 1];
			pits[playerSide][pit] = 0;
			pits[getNextPlayer(playerSide)][STONES_NUMBER - pit - 1] = 0;
		} else {
			activePlayer = getNextPlayer(activePlayer);
		}

		checkIfAllPitsAreEmpty();

		boardChanged();
	}

	public boolean isEndGame() {
		return gameOver;
	}

	private void checkIfAllPitsAreEmpty() {
		int[][] pits = getPits();
		int empty;
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			empty = 0;
			for (int j = 0; j < STONES_NUMBER; j++) {
				if (pits[i][j] == 0)
					empty++;
			}

			if (empty == STONES_NUMBER) {
				finishGame(getNextPlayer(i));
				break;
			}
		}
	}

	private void finishGame(int playerSide) {
		gameOver = true;

		// empties the remainder of the board
		for (int i = 0; i < STONES_NUMBER; i++) {
			kalahs[playerSide] += pits[playerSide][i];
			pits[playerSide][i] = 0;
		}

		if (kalahs[activePlayer] == kalahs[getNextPlayer(activePlayer)])
			activePlayer = -1;
		else if (kalahs[activePlayer] < kalahs[getNextPlayer(activePlayer)])
			activePlayer = getNextPlayer(activePlayer);
	}

	private int getNextPit(int pit) {
		if (++pit >= STONES_NUMBER) {
			pit = 0;
		}

		return pit;
	}

	private int getNextPlayer(int side) {
		if (++side >= NUMBER_OF_PLAYERS) {
			side = 0;
		}

		return side;
	}

	public int getActivePlayer() {
		return activePlayer;
	}

	public String printActivePlayer() {
		return "Player " + (activePlayer + 1) + " Turn";
	}

	// Listener for any change
	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
		boardChanged();
	}

	// Updates listener
	public void boardChanged() {
		printBoardToConsole();

		for (ChangeListener listener : changeListeners) {
			listener.stateChanged(new ChangeEvent(this));
		}
	}
}