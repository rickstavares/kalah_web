import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class Board extends JComponent
{
	private int[][] pits;
	private int[] kalahs;
	private BoardDraw layout;

	public Board(BoardDraw layout)
	{
		this.layout = layout;
	}

	public Rectangle2D.Double[][] getPitRectangles()
	{
		return layout.getPicturesRectangles();
	}

	public void setBoardSize(int w, int h)
	{
		setPreferredSize(new Dimension(w,h));
		layout.setSize(w,h);
	}

	public void paintComponent(Graphics g)
	{
		layout.redraw(g, this, pits, kalahs);
	}

	public void setData(int[][] pits, int[] kalahs)
	{
		this.pits = pits;
		this.kalahs = kalahs;
	}

}