import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class KalahGUI extends JFrame implements MouseListener, ChangeListener, ActionListener {
	private Kalah game;
	private Board board;
	private JLabel player;
	private static final int WIDTH = 700;
	private static final int HEIGHT = 400;
	private static final int BOARD_WIDTH = 455;
	private static final int BOARD_HEIGHT = 325;

	/**
	 * The view and frame that shows the mancala game visually
	 */
	public KalahGUI() {
		createLayouts();
	}

	public void start(BoardDraw layout) {

		game = new Kalah();
		board = new Board(layout);
		game.addChangeListener(this);

		setSize(WIDTH, HEIGHT);
		board.setBoardSize(BOARD_WIDTH, BOARD_HEIGHT);

		// Displays the active player
		player = new JLabel(game.printActivePlayer());
		player.setPreferredSize(new Dimension(250, 10));

		// Add Board's mouseListener
		board.addMouseListener(this);

		setLayout(new FlowLayout());
		add(board);
		add(player);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}

	private void createLayouts() {
		BoardDraw layout = new BoardDraw();
		start(layout);
	}

	public void stateChanged(ChangeEvent event) {
		board.setData(game.getPits(), game.getKalahs());
		board.repaint();
		if (game.isEndGame())
			if (game.getActivePlayer() < 0)
				JOptionPane.showMessageDialog(this, "DRAW", "Game Over", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "Player " + (game.getActivePlayer() + 1) + " is the winner!", "Game Over",
						JOptionPane.INFORMATION_MESSAGE);
	}

	public void mousePressed(MouseEvent e) {
		if (game.isEndGame())
			return;
		Rectangle2D.Double[][] rects = board.getPitRectangles();

		for (int row = 0; row < rects.length; row++)
			for (int col = 0; col < rects[row].length; col++)
				if (rects[row][col].contains(e.getPoint()))
					try {
						game.move(row, col);
						player.setText(game.printActivePlayer());
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Move",
								JOptionPane.WARNING_MESSAGE);
					}
	}

	public void actionPerformed(ActionEvent event) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}