import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;

public class BoardDraw
{

	private Rectangle2D.Double[][] picturesRectangles;
	private int width;
	private int height;
	private Rectangle2D.Double[] mRects;
	private int boardLength;
	private Image game_board;
	private Image pit;
	private Image stone;
	private Image mancala;
	private static Random rand = new Random();
/**
	 * Constructor that creates the layout of the game
	 * @param nPlayers the number of players
	 * @param boardLength the number of pits
	 */
	public BoardDraw()
	{
		picturesRectangles = new Rectangle2D.Double[Kalah.NUMBER_OF_PLAYERS][Kalah.STONES_NUMBER];
		this.boardLength = Kalah.STONES_NUMBER;
		mRects = new Rectangle2D.Double[Kalah.NUMBER_OF_PLAYERS];
		try
		{
			game_board = ImageIO.read(new File("resources/kalah_game_board.png"));
			stone = ImageIO.read(new File("resources/shiny_stone.png"));
		}
		catch (Exception e) { game_board = null; }
	}

	/**
	 * Redraw using Image files.
	 * See BoardLayout class
	 */
	public void redraw(Graphics g, Board b, int[][] pits, int[] kalahs)
	{
		g.drawImage(game_board, 0, 0, 455, 325, b);

		Graphics2D g2 = (Graphics2D) g;

		// Draw the stones.
		for (int r = 0; r < pits.length; r++)
			for (int c = 0; c < pits[r].length; c++)
				for (int s = 0; s < pits[r][c]; s++)
					drawStone(picturesRectangles[r][c], g, b, s);
		for (int m = 0; m < mRects.length; m++)
			for(int s = 0; s < kalahs[m]; s++)
				drawStone(mRects[m], g, b, s);
		}

	/**
	 * Sets the size of the board and all the internals.
	 * See BoardLayout class
	 */
	public void setSize(int w, int h)
	{
		int m = 5; // margin
		int kW = 55; // Kalah Width
		int kH = 225; // Kalah Height
		int kY = 45; // Kalah Y value
		int pD = 50; // Pit Width and Height
		int pTop = 75; // Pit Y value
		int pBottom = 190; // Pit Y value
		width = w;
		height = h;
		mRects[0] = new Rectangle2D.Double(width - m - kW, kY, kW, kH);
		mRects[1] = new Rectangle2D.Double(m, kY, kW, kH);

		int s = boardLength - 1; /* Hack var to reverse direction */
		for (int c = 0; c < boardLength; c++)
		{
			picturesRectangles[0][c] = new Rectangle2D.Double(m + kW + m * (c + 1) + pD * c,
					pBottom, pD, pD);
			picturesRectangles[1][c] = new Rectangle2D.Double(m + kW + m * (s + 1) + pD * s,
					pTop, pD, pD);
			--s;
		}
	}

	public String getName() { return "Board Layout"; }

	/**
	 * Creates an ellipse to use as a stone.
	 * @param r the bounding box to draw into
	 * @param g the graphics context to draw into
	 * @param b the board to use for as component to draw to
	 * @param n an integer to aid in seeding the random number generator
	 */
	private void drawStone(Rectangle2D.Double r, Graphics g, Board b, int n)
	{
		rand.setSeed((int)r.getX() * n +  (int)r.getY() * n + n * n);
		int x = rand.nextInt((int)r.getWidth() - stone.getWidth(b));
		int y = rand.nextInt((int)r.getHeight() - stone.getHeight(b));
		g.drawImage(stone, (int)r.getX() + x, (int)r.getY() + y, stone.getWidth(b),
				stone.getHeight(b), b);
	}
	
	public Rectangle2D.Double[][] getPicturesRectangles() { return picturesRectangles; }
	
}