package ye;

import javax.swing.JFrame;

public class Draw extends JFrame {
	private static final long serialVersionUID = 1L;

	public Draw() {
		getContentPane().add(new Surface());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		setVisible(true);
	}

	public static void main(String arg[]) {
		new Draw();
	}
}