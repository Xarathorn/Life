package ye;

import java.awt.Point;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
	private ArrayList<Point> idc; // indices
	private int r, c;
	private boolean wrap = true;
	
	Board() {
		resetBoard(0,0);
	}
	Board(int rows, int cols) {
		resetBoard(rows, cols);
	}
	// ==========================VARIABLE MANAGEMENT============================\\
	/**
	 * Resets the number of rows to rows
	 * @param rows
	 */
	public void setRows(int rows) {
		r = rows;
	}
	
	/**
	 * Resets the number of cols to cols
	 * @param cols
	 */
	public void setCols(int cols) {
		c = cols;
	}
	
	/**
	 * Sets wrapping on/off
	 * @param b
	 */
	public void setWrap(boolean b) {
		wrap = b;
	}
	
	/**
	 * Returns the number of rows
	 * @return number of rows
	 */
	public int getRows() {
		return r;
	}
	
	/**
	 * Returns the number of columns
	 * @return number of columns
	 */
	public int getCols() {
		return c;
	}
	
	/**
	 * Returns an ArrayList of living cells
	 * @return
	 */
	public ArrayList<Point> getIndices() {
		return idc;
	}
	
	/**
	 * Returns the number of living cells
	 * @return
	 */
	public int getLiveCells() {
		return idc.size();
	}

	/**
	 * Returns if cell at (row, col) is alive
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean getIndice(int row, int col) {
		Point p = new Point(row, col);
		return getIndice(p);
	}
	
	/**
	 * Returns if cell at (row, col) is alive
	 * @param p
	 * @return
	 */
	public boolean getIndice(Point p) {
		if (p.getX() >= r || p.getY() >= c)
			throw new ArrayIndexOutOfBoundsException();
		return idc.contains(p);
	}
	
	// ==========================CELL SETTING============================\\
	public void toggleIndice(int row, int col) {
		Point p = new Point(row, col);
		toggleIndice(p);
	}
	public void toggleIndice(Point p) {
		if (p.getX() < r || p.getY() < c)
			if (idc.contains(p))
				setIndice(p, false);
			else
				setIndice(p, true);
	}
	
	/**
	 * Sets the cell at (row, col) to bool
	 * @param row
	 * @param col
	 * @param bool
	 */
	public void setIndice(int row, int col, boolean bool) {
		Point p = new Point(row, col);
		setIndice(p, bool);
	}
	
	/**
	 * Sets the cell at p to bool
	 * @param p
	 * @param bool
	 */
	public void setIndice(Point p, boolean bool) {
		if (p.getX() < r || p.getY() < c)
			if (bool && !idc.contains(p))
				idc.add(p);
			else if (!bool && idc.contains(p))
				idc.remove(p);
	}
	
	// ==========================BOARD MANAGEMENT============================\\
	/**
	 * Sets board to blank 2D boolean array
	 */
	public void clearBoard() {
		idc = new ArrayList<Point>();
	}

	/**
	 * Resets board to size rows x cols
	 * @param rows
	 * @param cols
	 */
	public void resetBoard(int rows, int cols) {
		idc = new ArrayList<Point>();
		r = rows;
		c = cols;
	}
	
	/**
	 * Resets board to size p.getX() x p.getY()
	 * @param p
	 */
	public void resetBoard(Point p) {
		resetBoard((int)p.getX(),(int)p.getY());
	}
	
	/**
	 * Sets board to equal array ba
	 * 
	 * @param ba - 2D boolean array
	 */
	public void setBoard(boolean[][] ba) {
		r = ba.length;
		c = ba[0].length;
		idc = new ArrayList<Point>();
		for (int row = 0; row < ba.length; row++)
			for (int col = 0; col < ba[0].length; col++)
				if(ba[row][col])
					idc.add(new Point(row, col));
	}
	
	/**
	 * Creates 2D boolean array of board
	 * @return
	 */
	public boolean[][] getBoard() {
		boolean[][] out = new boolean[r][c];
		for (Point p : idc)
			out[(int) p.getX()][(int) p.getY()] = true;
		return out;
	}
	
	/**
	 * Resizes board to rows x cols
	 * @param rows
	 * @param cols
	 */
	public void resize(int rows, int cols) {
		r = rows;
		c = cols;
	}
	
	/**
	 * Updates cells according to Conway's Game of Life logic.
	 */
	public void update() {
		int h = r - 1;
		int w = c - 1;
		ArrayList<Point> tmp = new ArrayList<Point>();
		ArrayList<Point> adj = new ArrayList<Point>();
		for (Point p : idc) {
			int row = (int) p.getX();
			int col = (int) p.getY();
			int count = 0;
			for (int i = -1; i < 2; i++)
				for (int j = -1; j < 2; j++) {
					int hi = row + i;
					int wi = col + j;
					if (wrap) {
						hi = hi < 0 ? h : (hi > h ? 0 : hi);
						wi = wi < 0 ? w : (wi > w ? 0 : wi);
					}
					else if (hi < 0 || hi > h || wi < 0 || wi > w)
						continue;
					Point hiwi = new Point(hi, wi);
					if (idc.contains(hiwi))
						count++;
					else if (!adj.contains(hiwi))
						adj.add(hiwi);
				}
			if(count == 3 ? true : (idc.contains(new Point(row, col)) && count == 4 ? true : false))
				tmp.add(p);
		}
		for (Point p : adj) {
			int row = (int) p.getX();
			int col = (int) p.getY();
			int count = 0;
			for (int i = -1; i < 2; i++)
				for (int j = -1; j < 2; j++) {
					int hi = row + i;
					int wi = col + j;
					if (wrap) {
						hi = hi < 0 ? h : (hi > h ? 0 : hi);
						wi = wi < 0 ? w : (wi > w ? 0 : wi);
					}
					else if (hi < 0 || hi > h || wi < 0 || wi > w)
						continue;
					if (idc.contains(new Point(hi, wi)))
						count++;
				}
			if(count == 3)
				tmp.add(p);
		}
		idc = tmp;
	}
	
	// ==========================FILE I/O============================\\
	/**
	 * Converts file to 2D bool array board
	 * @param file - file to convert
	 * @return array version of file
	 * @throws Exception
	 */
	public void boardFromFile(File file) throws Exception {
		if (!file.getAbsolutePath().endsWith(".brd"))
			file = new File(file.getAbsolutePath()+".brd");
		Scanner sc = new Scanner(file);
		resetBoard(sc.nextInt(), sc.nextInt());
		sc.nextLine();
		for (int row = 0; row < r; row++) {
			String line = sc.nextLine();
			for (int col = 0; col < c; col++)
				if (line.charAt(col) == 'O')
					idc.add(new Point(row, col));
		}
		sc.close();
	}

	/**
	 * Converts 2D bool array board to file
	 * @param board - array to convert to file
	 * @param file - file from board
	 * @throws Exception
	 */
	public void boardToFile(File file) throws Exception {
		if (!file.getAbsolutePath().endsWith(".brd"))
			file = new File(file.getAbsolutePath()+".brd");
		PrintWriter writer = new PrintWriter(file);
		writer.write(r + " " + c + "\n");
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++)
				writer.write(idc.contains(new Point(i,j)) ? "O" : "-");
			writer.write("\n");
		}
		writer.close();
	}
}
