package kop;

import kop.ui.StartGameWindow;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/18/11
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
	private static JFrame frame;
	private static JInternalFrame internalFrame;
	private static BorderLayout manager;
	private static JPanel contentPane;
	private static boolean frameInitDone=false;

	public static void main(String[] args) {
		initFrame();
		displayFrame(new StartGameWindow());
	}

	private static void initFrame() {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(1024,768));
		manager = new BorderLayout();
		frame.setLayout(manager);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frameInitDone = true;
	}

	// TODO rewrite this so the calling party doesn't have to create a new instance, especially of MainWindow.
	public static void displayFrame(kop.ui.Window window) {
		// used for form debugging, the main method might not be in this class, it might be in a class bound to a form
		if (!frameInitDone) {
			initFrame();
		}
		frame.getContentPane().removeAll();
		frame.getContentPane().add(window.getContentPane(), CENTER);
		frame.getContentPane().validate();
		frame.setTitle(window.getTitle());
	}

	public static void exit() {
		frame.dispose();
	}
}
