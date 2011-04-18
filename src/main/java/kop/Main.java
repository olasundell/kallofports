package kop;

import kop.ui.MainWindow;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/18/11
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
	public static void main(String[] args) {
		displayFrame("kop.ui.MainWindow", new MainWindow().getContentPane());
	}

	public static void displayFrame(String title, JPanel contentPane) {
		JFrame frame = new JFrame(title);
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
