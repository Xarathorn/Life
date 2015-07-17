package ye;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

/**
 * Game of Life board
 * 
 * @author William
 */
// TODO only redraw cells that have changed, beautify code, add fullscreen w/ escape to exit
class BoardPanel extends JPanel implements ActionListener, ComponentListener {
	private static final long serialVersionUID = 1L;

	Board b;

	private Color col, fCol = Color.WHITE;
	private int delay = 100, gen = 0, gt = 0, extraSpace, pr = 0, pc = 0, h = 0, w = 0,  sqr; // timer delay, # of generations,
										// generation times, amount of empty space, height of panel, width of panel,  square side length
	private double cs = 5;
	private Board prefab = null;
	private boolean persist = false, cycle = false; // prefab persisting, rainbow cycling
	private Timer timer;
	private Surface s; // enclosing panel
	private File fil;
	private Point lastDrawn; // last drawn cell

	// ==========================INSTANTIATE============================\\
	BoardPanel(int rows, int cols, Color color, Surface surf) {
		s = surf;
		initTimer();
		col = color;
		b = new Board(rows, cols);
		MouseInput mi = new MouseInput();
		addMouseListener(mi);
		addMouseMotionListener(mi);
		addComponentListener(this);
	}

	// ==========================COLOR============================\\
	/**
	 * Sets cell's color
	 * 
	 * @param color - color to be changed to
	 */
	public void setColor(Color color) {
		col = color;
	}

	/**
	 * Turns rainbow cycling on and off
	 */
	private void cycleRainbow() {
		// HSB Solution (colors cap at 191)
		float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
		hsb[0] = hsb[0] * 360;
		hsb[0] = (float) (hsb[0] + cs > 360 ? 360 - hsb[0] + cs : hsb[0] + cs) / 360;
		hsb[1] = Math.abs(hsb[1] - 1) > .05 ? hsb[1] > 1f ? hsb[1] - .05f : hsb[1] < 1f ? hsb[1] + .05f : 1f : 1f; // Approaches 1
		hsb[2] = Math.abs(hsb[2] - .75) > .05 ? hsb[2] > .75f ? hsb[2] - .05f : hsb[2] < .75f ? hsb[2] + .05f : .75f : .75f; // Approaches .75
		col = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

	/**
	 * Sets rainbow cycling speed to cycleSpeed
	 * 
	 * @param cycleSpeed
	 */
	public void setCycleSpeed(double cycleSpeed) {
		cs = cycleSpeed;
	}
	
	/**
	 * Sets rainbow cycling to bool
	 * @param bool
	 */
	public void setCycle(boolean bool) {
		cycle = bool;
	}

	/**
	 * Toggles dark theme
	 */
	public void dark() {
		if (fCol == Color.WHITE) {
			this.setBackground(Color.BLACK);
			fCol = Color.DARK_GRAY;
			if (col != Color.WHITE && !cycle) {
				col = Color.WHITE;
				s.updateColor(col);
			}
		} else {
			this.setBackground(Color.LIGHT_GRAY);
			fCol = Color.WHITE;
			if (col != Color.BLACK && !cycle) {
				col = Color.BLACK;
				s.updateColor(col);
			}
		}
	}

	/**
	 * Lightens color
	 * @param color
	 * @return lighter version of color
	 */
	private Color lightenColor(Color color) {
		int red = colorRange(color.getRed() + 40);
		int grn = colorRange(color.getGreen() + 40);
		int blu = colorRange(color.getBlue() + 40);
		return new Color(red, grn, blu);
	}

	/**
	 * Darkens color
	 * @param color
	 * @return darker version of color
	 */
	private Color darkenColor(Color color) {
		int red = colorRange(color.getRed() - 40);
		int grn = colorRange(color.getGreen() - 40);
		int blu = colorRange(color.getBlue() - 40);
		return new Color(red, grn, blu);
	}

	/**
	 * Constrains to the color range
	 * @param value
	 * @return
	 */
	private int colorRange(int value) {
		return range(0, 255, value);
	}

	/**
	 * Is color dark?
	 * @param color
	 * @return
	 */
	private boolean dark(Color color) {
		return !(color.getRed() > 128 || color.getGreen() > 128 || color.getBlue() > 128);
	}

	// ==========================TIMER============================\\
	/**
	 * Initializes timer
	 */
	private void initTimer() {
		timer = new Timer(delay, this);
		timer.start();
	}

	/**
	 * Gets timer
	 * 
	 * @return timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Sets timer delay to int i
	 * 
	 * @param i
	 */
	public void setTimerDelay(int i) {
		timer.setDelay(i);
	}

	/**
	 * Stops timer
	 */
	public void stopTimer() {
		timer.stop();
	}

	/**
	 * Starts timer
	 */
	public void startTimer() {
		timer.start();
	}

	/**
	 * Whenever timer updates
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (cycle) {
			cycleRainbow();
			s.updateColor(col);
		}
		b.update();
		if (gen == Integer.MAX_VALUE) {
			gen = 0;
			gt++;
		}
		s.lblGen.setText("Generation: " + ++gen + (gt > 0 ? "^" + gt : ""));
		s.lblCells.setText("Live Cells: " + b.getLiveCells());
		repaint();
	}

	// ==========================BOARD============================\\
	/**
	 * Calculates side length of cells
	 */
	public void calcSquareSide() {
		int i1 = Math.round((this.getHeight() - (1 * b.getRows())) / b.getRows());
		int i2 = Math.round((this.getWidth() - (1 * b.getCols())) / b.getCols());
		sqr = i1 <= i2 ? i1 : i2; // Makes the squares be square
		h = this.getHeight();
		w = this.getWidth();
		extraSpace = (w - (1 * b.getCols() + 1 + b.getCols() * sqr)) / 2; // centers horizontally
	}

	/**
	 * Sets board to blank 2D boolean array
	 */
	public void clearBoard() {
		b.clearBoard();
		gen = 0;
		gt = 0;
		repaint();
	}

	/**
	 * Sets board to equal array ba
	 * 
	 * @param ba
	 */
	public void setBoard(boolean[][] ba) {
		b.setBoard(ba);
		repaint();
	}

	/**
	 * Paints board
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int r = 0; r < b.getRows(); r++)
			for (int c = 0; c < b.getCols(); c++) {
				g2d.setColor(b.getIndice(r, c) ? col : fCol);
				if (prefab != null)
					if (r >= pr && r < pr + prefab.getRows() && c >= pc && c < pc + prefab.getCols()
							&& prefab.getIndice(r - pr, c - pc))
						g2d.setColor(dark(g2d.getColor()) ? lightenColor(g2d.getColor()) : darkenColor(g2d.getColor()));
				g2d.fillRect(1 * c + 1 + c * sqr + extraSpace, 1 * r + 1 + r * sqr, sqr, sqr);
			}
	}

	/**
	 * Lets you plop down presets based on the file
	 * 
	 * @param fileName
	 */
	public void addPrefab(String fileName) {
		fil = new File(s.getPath() + "Prefabs\\" + fileName);
		prefab = new Board();
		try {
			prefab.boardFromFile(fil);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ==========================MOUSE============================\\
	class MouseInput extends MouseAdapter {
		public void mouseMoved(MouseEvent e) {
			if (prefab != null) {
				int col = (int) Math.ceil((e.getX() - extraSpace - sqr) / (sqr + 1.0));
				int row = (int) Math.ceil((e.getY() - sqr) / (sqr + 1.0));
				if (row != pr || col != pc) {
					pr = row;
					pc = col;
					repaint();
				}
			}
		}

		public void mousePressed(MouseEvent e) {
			draw(e);
		}

		public void mouseDragged(MouseEvent e) {
			draw(e);
		}

		public void mouseReleased(MouseEvent e) {
			lastDrawn = null;
		}

		/**
		 * Handles all mouse drawing
		 * 
		 * @param e
		 */
		private void draw(MouseEvent e) {
			int col = (int) Math.ceil((e.getX() - extraSpace - sqr) / (sqr + 1.0));
			int row = (int) Math.ceil((e.getY() - sqr) / (sqr + 1.0));
			if (col >= 0 && col < b.getCols() && row >= 0 && row < b.getRows()) { // makes
																					// sure
																					// mouse
																					// was
																					// clicked
																					// on
																					// board
				Point p = new Point(row, col);
				if (prefab == null && !p.equals(lastDrawn)) {
					b.toggleIndice(p);
					lastDrawn = p;
				} else if (!p.equals(lastDrawn)) { // if it's applying a prefab
					lastDrawn = p;
					try {
						for (Point pa : prefab.getIndices())
							b.setIndice(row + (int) pa.getX(), col + (int) pa.getY(), true);
						if (persist) {
							prefab = null;
							fil = null;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				repaint();
			}
		}
	}

	// ==========================MISC============================\\
	/**
	 * Reset number of generations
	 */
	public void resetGenerations() {
		gen = 0;
		gt = 0;
	}

	/**
	 * Toggles prefabs persisting
	 */
	public void persist() {
		persist = !persist;
		prefab = null;
	}

	/**
	 * Constrains value to min and max
	 * 
	 * @param min
	 * @param max
	 * @param value
	 * @return min if value <= min; value if min <= value <= max; max if value
	 *         >= max
	 */
	private int range(int min, int max, int value) {
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Resets the size spinners to new rows and columns
	 * @param rows
	 * @param cols
	 */
	private void modifySizeSpinners(int rows, int cols) {
		h = this.getHeight();
		w = this.getWidth();
		calcSquareSide();
		s.spinnerRow.setToolTipText("Number of rows. (0-" + ((h - 2) / 2) + ")");
		s.spinnerRow.setModel(new SpinnerNumberModel(range(0, (h - 2) / 2, rows), 0, (h - 2) / 2, 1));
		s.spinnerCol.setToolTipText("Number of cols. (0-" + (w - 2) / 2 + ")");
		s.spinnerCol.setModel(new SpinnerNumberModel(range(0, (w - 2) / 2, cols), 0, (w - 2) / 2, 1));
	}

	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) {
		modifySizeSpinners((int) s.spinnerRow.getValue(), (int) s.spinnerCol.getValue());
	}
}