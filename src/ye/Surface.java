package ye;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingConstants;

public class Surface extends JPanel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;

	public int r, c, red = 0, green = 0, blue = 0;
	public JLabel lblGen, lblCells;
	public JSpinner spinnerRow, spinnerCol;

	private JSlider sliderR, sliderG, sliderB, sliderSpeed;
	private JSpinner spinnerR, spinnerG, spinnerB;
	private JToggleButton rainbowButton, pauseButton;
	private JButton btnOpen, btnSave, btnClear, btnGlider, btnBlock, btnExploder, btnGun, btnBloop, btnLShip, btnTmblr;
	private JCheckBox wrapBox, darkBox;
	private JFileChooser jfc;
	private int DELAY = 75;
	private BoardPanel panel;
	private JMenu mnPrefabs;
	private String path;
	private JCheckBox persistBox;
	private Box rainBox;
	private JSlider sliderRain;
	private Box horizontalBox;
	private Component horizontalStrut;

	public Surface() { // TODO make ghosts of droppable items
		r = 25;
		c = 50;
		path = Surface.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("/", "\\")+"Boards\\";

		// MENUBAR
		setLayout(new BorderLayout());
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

				// FILE MENU
				JMenu mnFile = new JMenu("File");
				menuBar.add(mnFile);
		
						// FILE CHOOSER
						jfc = new JFileChooser(path+"Saves\\");
				
						// NEW FILE BUTTON
						btnOpen = new JButton("Open");
						btnOpen.addActionListener(this);
						btnOpen.setMaximumSize(new Dimension(100, 100));
						mnFile.add(btnOpen);
				
						// SAVE FILE BUTTON
						btnSave = new JButton("Save");
						btnSave.addActionListener(this);
						btnSave.setMaximumSize(new Dimension(100, 100));
						mnFile.add(btnSave);
				
				// PREFAB MENU
				mnPrefabs = new JMenu("Prefabs");
				menuBar.add(mnPrefabs);
				
						// PERSIST CHECKBOX
						persistBox = new JCheckBox("Persist");
						persistBox.setSelected(true);
						persistBox.setMaximumSize(new Dimension(100, 25));
						persistBox.addItemListener(this);
						mnPrefabs.add(persistBox);
						
						// GLIDER PRESET
						btnGlider = new JButton("Glider");
						makeButton(mnPrefabs, btnGlider);
						
						// BLOCK PRESET
						btnBlock = new JButton("Block");
						makeButton(mnPrefabs, btnBlock);
						
						// EXPLODER PRESET
						btnExploder = new JButton("Exploder");
						makeButton(mnPrefabs, btnExploder);
				
						// GUN PRESET
						btnGun = new JButton("Gun");
						makeButton(mnPrefabs, btnGun);
				
						// BLOOP PRESET
						btnBloop = new JButton("Bloop");
						makeButton(mnPrefabs, btnBloop);
				
						// TUMBLER PRESET
						btnTmblr = new JButton("Tumbler");
						makeButton(mnPrefabs, btnTmblr);
				
						// SPACE SHIP PRESET
						btnLShip = new JButton("Space Ship");
						makeButton(mnPrefabs, btnLShip);
		
				// PAUSE BUTTON
				pauseButton = new JToggleButton("Pause");
				pauseButton.addItemListener(this);
				menuBar.add(pauseButton);
		
				// CLEAR BUTTON
				btnClear = new JButton("Clear");
				btnClear.addActionListener(this);
				menuBar.add(btnClear);
		
				// RGB OPTIONS
				JMenu mnColor = new JMenu("Color");
				menuBar.add(mnColor);
		
						// RED
						Box horizontalBoxRed = Box.createHorizontalBox();
						mnColor.add(horizontalBoxRed);
				
						JLabel lblRed = new JLabel("Red  ");
						horizontalBoxRed.add(lblRed);
				
						Component horizontalStrutR = Box.createHorizontalStrut(6);
						horizontalBoxRed.add(horizontalStrutR);
				
						sliderR = new JSlider();
						horizontalBoxRed.add(sliderR);
						setRGBSlider(sliderR, "Red");
				
						spinnerR = new JSpinner();
						horizontalBoxRed.add(spinnerR);
						spinnerR.setModel(new SpinnerNumberModel(0, 0, 250, 1));
						spinnerR.addChangeListener(new SpinnerListener());
				
						// GREEN
						Box horizontalBoxGreen = Box.createHorizontalBox();
						mnColor.add(horizontalBoxGreen);
				
						JLabel lblGreen = new JLabel("Green");
						horizontalBoxGreen.add(lblGreen);
				
						sliderG = new JSlider();
						horizontalBoxGreen.add(sliderG);
						setRGBSlider(sliderG, "Green");
				
						spinnerG = new JSpinner();
						horizontalBoxGreen.add(spinnerG);
						spinnerG.setModel(new SpinnerNumberModel(0, 0, 250, 1));
						spinnerG.addChangeListener(new SpinnerListener());
				
						// BLUE
						Box horizontalBoxBlue = Box.createHorizontalBox();
						mnColor.add(horizontalBoxBlue);
				
						JLabel lblBlue = new JLabel("Blue ");
						horizontalBoxBlue.add(lblBlue);
				
						Component horizontalStrutB = Box.createHorizontalStrut(6);
						horizontalBoxBlue.add(horizontalStrutB);
				
						sliderB = new JSlider();
						horizontalBoxBlue.add(sliderB);
						setRGBSlider(sliderB, "Blue");
				
						spinnerB = new JSpinner();
						horizontalBoxBlue.add(spinnerB);
						spinnerB.setModel(new SpinnerNumberModel(0, 0, 250, 1));
						spinnerB.addChangeListener(new SpinnerListener());

						// RAINBOWS!!!!!!!!
						rainBox = Box.createVerticalBox();
						rainBox.setMaximumSize(new Dimension(95, 0));
						mnColor.add(rainBox);
						
						rainbowButton = new JToggleButton("Rainbows!!");
						rainbowButton.addItemListener(this);
						rainBox.add(rainbowButton);
						
						sliderRain = new JSlider();
						sliderRain.setAlignmentX(Component.LEFT_ALIGNMENT);
						sliderRain.setMaximumSize(new Dimension(95, 25));
						sliderRain.setValue(5);
						sliderRain.setMaximum(20);
						sliderRain.addChangeListener(new SliderListener());
						rainBox.add(sliderRain);
		
				// SPEED
				JLabel lblSpeed = new JLabel("Speed:");
				menuBar.add(lblSpeed);
		
				sliderSpeed = new JSlider();
				sliderSpeed.setMajorTickSpacing(75);
				sliderSpeed.setMinorTickSpacing(25);
				sliderSpeed.setPaintLabels(true);
				sliderSpeed.setPaintTicks(true);
				sliderSpeed.setName("Speed:");
				sliderSpeed.setMaximum(150);
				sliderSpeed.setValue(75);
				sliderSpeed.addChangeListener(new SliderListener());
				menuBar.add(sliderSpeed);
		
				// ROWS
				JLabel lblRows = new JLabel("Rows:");
				menuBar.add(lblRows);
		
				spinnerRow = new JSpinner();
				spinnerRow.setFont(spinnerRow.getFont().deriveFont(spinnerRow.getFont().getSize() + 4f));
				spinnerRow.setToolTipText("Number of rows. (0-300)");
				spinnerRow.setModel(new SpinnerNumberModel(25, 0, 300, 1));
				menuBar.add(spinnerRow);
				spinnerRow.addChangeListener(new SpinnerListener());
		
				// COLUMNS
				JLabel lblCols = new JLabel("Cols:");
				menuBar.add(lblCols);
		
				spinnerCol = new JSpinner();
				spinnerCol.setFont(spinnerCol.getFont().deriveFont(spinnerCol.getFont().getSize() + 4f));
				spinnerCol.setToolTipText("Number of cols. (0-300)");
				spinnerCol.setModel(new SpinnerNumberModel(50, 0, 300, 1));
				menuBar.add(spinnerCol);
				spinnerCol.addChangeListener(new SpinnerListener());
		
				// WRAP CHECKBOX
				wrapBox = new JCheckBox("Wrap");
				wrapBox.setSelected(true);
				wrapBox.addItemListener(this);
				menuBar.add(wrapBox);
				
				// DARK CHECKBOX
				darkBox = new JCheckBox("Dark");
				darkBox.addItemListener(this);
				menuBar.add(darkBox);

		// PANEL
		panel = new BoardPanel(r, c, new Color(red, green, blue), this);
		add(panel, BorderLayout.CENTER);
		
		// BOTTOM LABELS
		horizontalBox = Box.createHorizontalBox();
		add(horizontalBox, BorderLayout.SOUTH);
		
				// GENERATION LABEL
				lblGen = new JLabel("Generation: 0");
				horizontalBox.add(lblGen);
				lblGen.setHorizontalAlignment(SwingConstants.LEFT);
				
				horizontalStrut = Box.createHorizontalStrut(20);
				horizontalBox.add(horizontalStrut);
				
				// GENERATION LABEL
				lblCells = new JLabel("Live Cells: 0");
				horizontalBox.add(lblCells);
				lblCells.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	/**
	 * Creates a button
	 * @param m
	 * @param b
	 */
	private void makeButton(JMenu m, JButton b) {
		b.setMaximumSize(new Dimension(100, 25));
		b.addActionListener(this);
		m.add(b);
	}

	/**
	 * Updates red, green, blue, sliders, and spinners
	 * 
	 * @param c
	 *            - color method updates to
	 */
	public void updateColor(Color c) {
		red = c.getRed();
		blue = c.getBlue();
		green = c.getGreen();
		sliderR.setValue(red);
		sliderG.setValue(green);
		sliderB.setValue(blue);
		spinnerR.setValue(red);
		spinnerB.setValue(blue);
	}

	/**
	 * Sets up slider
	 * 
	 * @param slider to be set
	 * @param tipText - ToolTipText
	 */
	private void setRGBSlider(JSlider slider, String tipText) {
		slider.setToolTipText(tipText);
		slider.setMaximum(250);
		slider.setValue(0);
		slider.setPaintLabels(true);
		slider.addChangeListener(new SliderListener());
	}
	
	/**
	 * Get path of class
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Handles spinners updating
	 * 
	 * @author William
	 */
	class SpinnerListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSpinner spinner = (JSpinner) e.getSource();
			if (spinner == spinnerR) { // Red spinner
				red = (int) spinner.getValue();
				sliderR.setValue(red);
			} else if (spinner == spinnerG) { // Green spinner
				green = (int) spinner.getValue();
				sliderG.setValue(green);
			} else if (spinner == spinnerB) { // Blue spinner
				blue = (int) spinner.getValue();
				sliderB.setValue(blue);
			} else if (spinner == spinnerRow) { // Row spinner
				r = (int) spinner.getValue();
				panel.b.setRows(r);
				panel.calcSquareSide();
				panel.b.resize(r, c); // Updates board without logic
				panel.repaint();
			} else if (spinner == spinnerCol) { // Column spinner
				c = (int) spinner.getValue();
				panel.b.setCols(c);
				panel.calcSquareSide();
				panel.b.resize(r, c);
				panel.repaint();
			}
			panel.setColor(new Color(red, green, blue)); // Updates color
		}
	}

	/**
	 * Handles sliders updating
	 * 
	 * @author William
	 */
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			if (slider == sliderR) { // Red slider
				red = slider.getValue();
				spinnerR.setValue(red);
			} else if (slider == sliderG) { // Green slider
				green = slider.getValue();
				spinnerG.setValue(green);
			} else if (slider == sliderB) { // Blue slider
				blue = slider.getValue();
				spinnerB.setValue(blue);
			} else if (slider == sliderSpeed) { // Speed slider
				DELAY = slider.getValue();
				panel.setTimerDelay(DELAY);
			} else if (slider == sliderRain) {
				panel.setCycleSpeed(sliderRain.getValue());
			}
			panel.setColor(new Color(red, green, blue));
		}
	}

	/**
	 * Button handling
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnClear) // Clears board
			panel.clearBoard();
		else if (e.getSource() == btnOpen) { // Open file button
			int returnVal = jfc.showOpenDialog(Surface.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) { // file was selected
				File file = jfc.getSelectedFile();
				try {
					panel.b.boardFromFile(file); // Loads file
					panel.resetGenerations();
					r = panel.b.getRows();
					c = panel.b.getCols();
					spinnerRow.setValue(r);
					spinnerCol.setValue(c);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == btnSave) { // Save file
			int returnVal = jfc.showSaveDialog(Surface.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) { // file was selected
				File file = jfc.getSelectedFile();
				try {
					panel.b.boardToFile(file); // saves file
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == btnGlider)
			panel.addPrefab("glider.brd");
		else if (e.getSource() == btnBlock)
			panel.addPrefab("block.brd");
		else if (e.getSource() == btnExploder)
			panel.addPrefab("exploder.brd");
		else if (e.getSource() == btnGun)
			panel.addPrefab("gun.brd");
		else if (e.getSource() == btnBloop)
			panel.addPrefab("bloop.brd");
		else if (e.getSource() == btnTmblr)
			panel.addPrefab("tumbler.brd");
		else if (e.getSource() == btnLShip)
			panel.addPrefab("lspaceship.brd");
	}

	/**
	 * Toggle button handling
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == pauseButton) // Pause button
			if (e.getStateChange() == ItemEvent.SELECTED)
				panel.stopTimer();
			else
				panel.startTimer();
		else if (e.getSource() == rainbowButton) // RAINBOWS!
			if (e.getStateChange() == ItemEvent.SELECTED)
				panel.setCycle(true); // Starts color cycling (handled by BoardPanel.cycleRainbow())
			else
				panel.setCycle(false); // Stops color cycling
		else if (e.getSource() == wrapBox) // Wrapping on/off
			if (e.getStateChange() == ItemEvent.SELECTED)
				panel.b.setWrap(true); // wrapping on
			else
				panel.b.setWrap(false); // wrapping off
		else if (e.getSource() == darkBox) // Dark theme on/off
			if (e.getStateChange() == ItemEvent.SELECTED)
				panel.dark(); // dark on
			else
				panel.dark(); // dark off
		else if (e.getSource() == persistBox) // Prefab persisting on/off
			if (e.getStateChange() == ItemEvent.SELECTED)
				panel.persist(); 
			else
				panel.persist();
	}
}